/*
 * Copyright 2010-2018 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.gradle.internal

import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.tasks.*
import org.gradle.api.tasks.incremental.IncrementalTaskInputs
import org.gradle.workers.IsolationMode
import org.gradle.workers.WorkAction
import org.gradle.workers.WorkParameters
import org.gradle.workers.WorkerExecutor
import org.jetbrains.kotlin.gradle.internal.Kapt3GradleSubplugin.Companion.KAPT_WORKER_DEPENDENCIES_CONFIGURATION_NAME
import org.jetbrains.kotlin.gradle.internal.kapt.incremental.KaptIncrementalChanges
import org.jetbrains.kotlin.gradle.plugin.KotlinAndroidPluginWrapper
import org.jetbrains.kotlin.gradle.tasks.CompilerPluginOptions
import org.jetbrains.kotlin.gradle.tasks.findKotlinStdlibClasspath
import org.jetbrains.kotlin.gradle.tasks.findToolsJar
import org.jetbrains.kotlin.gradle.utils.isGradleVersionAtLeast
import org.jetbrains.kotlin.utils.PathUtil
import java.io.File
import java.io.Serializable
import java.net.URL
import java.net.URLClassLoader
import javax.inject.Inject

abstract class KaptWithoutKotlincTask @Inject constructor(
    objectFactory: ObjectFactory,
    private val workerExecutor: WorkerExecutor
) : KaptTask(objectFactory) {
    @get:InputFiles
    @get:Classpath
    @Suppress("unused")
    val kaptJars: Collection<File> by lazy {
        project.configurations.getByName(KAPT_WORKER_DEPENDENCIES_CONFIGURATION_NAME).resolve()
    }

    @get:Input
    var isVerbose: Boolean = false

    @get:Input
    var mapDiagnosticLocations: Boolean = false

    @get:Input
    lateinit var annotationProcessorFqNames: List<String>

    @get:Internal
    internal val processorOptions = CompilerPluginOptions()

    @get:Input
    lateinit var javacOptions: Map<String, String>

    @get:Input
    internal val kotlinAndroidPluginWrapperPluginDoesNotExist = project.plugins.none { it is KotlinAndroidPluginWrapper }

    @get:Classpath
    internal val kotlinStdlibClasspath = findKotlinStdlibClasspath(project)

    @get:Internal
    internal val projectDir = project.projectDir

    @get:Internal
    internal val providers = project.providers

    private fun getAnnotationProcessorOptions(): Map<String, String> {
        val options = processorOptions.subpluginOptionsByPluginId[Kapt3GradleSubplugin.KAPT_SUBPLUGIN_ID] ?: return emptyMap()

        val result = mutableMapOf<String, String>()
        for (option in options) {
            result[option.key] = option.value
        }
        return result
    }

    @TaskAction
    fun compile(inputs: IncrementalTaskInputs) {
        logger.info("Running kapt annotation processing using the Gradle Worker API")
        checkAnnotationProcessorClasspath()

        val incrementalChanges = getIncrementalChanges(inputs)
        val (changedFiles, classpathChanges) = when (incrementalChanges) {
            is KaptIncrementalChanges.Unknown -> Pair(emptyList<File>(), emptyList<String>())
            is KaptIncrementalChanges.Known -> Pair(incrementalChanges.changedSources.toList(), incrementalChanges.changedClasspathJvmNames)
        }

        val compileClasspath = classpath.files.toMutableList()
        if (kotlinAndroidPluginWrapperPluginDoesNotExist) {
            compileClasspath.addAll(0, PathUtil.getJdkClassesRootsFromCurrentJre())
        }

        val kaptFlagsForWorker = mutableSetOf<String>().apply {
            if (isVerbose) add("VERBOSE")
            if (mapDiagnosticLocations) add("MAP_DIAGNOSTIC_LOCATIONS")
            if (includeCompileClasspath) add("INCLUDE_COMPILE_CLASSPATH")
            if (incrementalChanges is KaptIncrementalChanges.Known) add("INCREMENTAL_APT")
        }

        val optionsForWorker = KaptOptionsForWorker(
            projectDir,
            compileClasspath,
            source.files.toList(),

            changedFiles,
            compiledSources,
            incAptCache.orNull?.asFile,
            classpathChanges.toList(),

            destinationDir,
            classesDir,
            stubsDir.asFile.get(),

            kaptClasspath.files.toList(),
            annotationProcessorFqNames,

            getAnnotationProcessorOptions(),
            javacOptions,

            kaptFlagsForWorker
        )

        // Skip annotation processing if no annotation processors were provided.
        if (annotationProcessorFqNames.isEmpty() && kaptClasspath.isEmpty()) {
            logger.info("No annotation processors provided. Skip KAPT processing.")
            return
        }

        val kaptClasspath = kaptJars + kotlinStdlibClasspath

        //TODO for gradle < 6.5
        val isolationModeStr = getValue("kapt.workers.isolation") ?: "none"
        val isolationMode = when (isolationModeStr.toLowerCase()) {
            "process" -> IsolationMode.PROCESS
            "none" -> IsolationMode.NONE
            else -> IsolationMode.NONE
        }
        val toolsJarURLSpec = findToolsJar()?.toURI()?.toURL()?.toString().orEmpty()

        submitWork(
            isolationMode,
            optionsForWorker,
            toolsJarURLSpec,
            kaptClasspath
        )
    }

    private fun submitWork(
        isolationMode: IsolationMode,
        optionsForWorker: KaptOptionsForWorker,
        toolsJarURLSpec: String,
        kaptClasspath: List<File>
    ) {
        val workQueue = when (isolationMode) {
            IsolationMode.PROCESS -> workerExecutor.processIsolation {
                if (getValue("kapt.workers.log.classloading") == "true") {
                    // for tests
                    it.forkOptions.jvmArgs("-verbose:class")
                }
                logger.info("Kapt worker classpath: ${it.classpath}")
            }
            IsolationMode.CLASSLOADER -> workerExecutor.classLoaderIsolation() {
                logger.info("Kapt worker classpath: ${it.classpath}")
            }
            IsolationMode.NONE -> workerExecutor.noIsolation()
            IsolationMode.AUTO -> throw UnsupportedOperationException(
                "Kapt worker compilation does not support $isolationMode"
            )
        }

        workQueue.submit(KaptExecutionWorkAction::class.java) {
            it.workerOptions.set(optionsForWorker)
            it.toolsJarURLSpec.set(toolsJarURLSpec)
            it.kaptClasspath.setFrom(kaptClasspath)
        }
    }

    internal fun getValue(propertyName: String): String? =
        if (isGradleVersionAtLeast(6, 5)) {
            providers.gradleProperty(propertyName).forUseAtConfigurationTime().orNull
        } else {
            project.findProperty(propertyName) as String?
        }

    internal interface KaptWorkParameters : WorkParameters {
        val workerOptions: Property<KaptOptionsForWorker>
        val toolsJarURLSpec: Property<String>
        val kaptClasspath: ConfigurableFileCollection
    }

    internal abstract class KaptExecutionWorkAction : WorkAction<KaptWorkParameters> {
        override fun execute() {
            KaptExecution(
                parameters.workerOptions.get(),
                parameters.toolsJarURLSpec.get(),
                parameters.kaptClasspath.toList()
            ).run()
        }
    }
}


private class KaptExecution @Inject constructor(
    val optionsForWorker: KaptOptionsForWorker,
    val toolsJarURLSpec: String,
    val kaptClasspath: List<File>
) : Runnable {
    private companion object {
        private const val JAVAC_CONTEXT_CLASS = "com.sun.tools.javac.util.Context"

        private fun kaptClass(classLoader: ClassLoader) = Class.forName("org.jetbrains.kotlin.kapt3.base.Kapt", true, classLoader)
        private var cachedClassLoaderWithToolsJar: ClassLoader? = null
        private var cachedKaptClassLoader: ClassLoader? = null
    }

    override fun run(): Unit = with(optionsForWorker) {
        val kaptClasspathUrls = kaptClasspath.map { it.toURI().toURL() }.toTypedArray()
        val rootClassLoader = findRootClassLoader()

        val classLoaderWithToolsJar = cachedClassLoaderWithToolsJar ?: if (!toolsJarURLSpec.isEmpty() && !javacIsAlreadyHere()) {
            URLClassLoader(arrayOf(URL(toolsJarURLSpec)), rootClassLoader)
        } else {
            rootClassLoader
        }
        cachedClassLoaderWithToolsJar = classLoaderWithToolsJar

        val kaptClassLoader = cachedKaptClassLoader ?: URLClassLoader(kaptClasspathUrls, classLoaderWithToolsJar)
        cachedKaptClassLoader = kaptClassLoader

        val kaptMethod = kaptClass(kaptClassLoader).declaredMethods.single { it.name == "kapt" }
        kaptMethod.invoke(null, createKaptOptions(kaptClassLoader))
    }

    private fun javacIsAlreadyHere(): Boolean {
        return try {
            Class.forName(JAVAC_CONTEXT_CLASS, false, KaptExecution::class.java.classLoader) != null
        } catch (e: Throwable) {
            false
        }
    }

    private fun createKaptOptions(classLoader: ClassLoader) = with(optionsForWorker) {
        val flags = kaptClass(classLoader).declaredMethods.single { it.name == "kaptFlags" }.invoke(null, flags)

        val mode = Class.forName("org.jetbrains.kotlin.base.kapt3.AptMode", true, classLoader)
            .enumConstants.single { (it as Enum<*>).name == "APT_ONLY" }

        val detectMemoryLeaksMode = Class.forName("org.jetbrains.kotlin.base.kapt3.DetectMemoryLeaksMode", true, classLoader)
            .enumConstants.single { (it as Enum<*>).name == "NONE" }

        Class.forName("org.jetbrains.kotlin.base.kapt3.KaptOptions", true, classLoader).constructors.single().newInstance(
            projectBaseDir,
            compileClasspath,
            javaSourceRoots,

            changedFiles,
            compiledSources,
            incAptCache,
            classpathChanges,

            sourcesOutputDir,
            classesOutputDir,
            stubsOutputDir,
            stubsOutputDir, // sic!

            processingClasspath,
            processors,

            processingOptions,
            javacOptions,

            flags,
            mode,
            detectMemoryLeaksMode
        )
    }

    private fun findRootClassLoader(): ClassLoader {
        tailrec fun parentOrSelf(classLoader: ClassLoader): ClassLoader {
            val parent = classLoader.parent ?: return classLoader
            return parentOrSelf(parent)
        }
        return parentOrSelf(KaptExecution::class.java.classLoader)
    }
}

internal data class KaptOptionsForWorker(
    val projectBaseDir: File,
    val compileClasspath: List<File>,
    val javaSourceRoots: List<File>,

    val changedFiles: List<File>,
    val compiledSources: List<File>,
    val incAptCache: File?,
    val classpathChanges: List<String>,

    val sourcesOutputDir: File,
    val classesOutputDir: File,
    val stubsOutputDir: File,

    val processingClasspath: List<File>,
    val processors: List<String>,

    val processingOptions: Map<String, String>,
    val javacOptions: Map<String, String>,

    val flags: Set<String>
) : Serializable
