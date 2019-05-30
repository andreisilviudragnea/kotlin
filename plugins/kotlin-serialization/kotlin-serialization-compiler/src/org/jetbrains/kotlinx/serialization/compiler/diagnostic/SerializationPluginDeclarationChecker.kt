/*
 * Copyright 2010-2019 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlinx.serialization.compiler.diagnostic

import org.jetbrains.kotlin.descriptors.ClassDescriptor
import org.jetbrains.kotlin.descriptors.DeclarationDescriptor
import org.jetbrains.kotlin.descriptors.ModuleDescriptor
import org.jetbrains.kotlin.descriptors.PropertyDescriptor
import org.jetbrains.kotlin.diagnostics.DiagnosticFactory0
import org.jetbrains.kotlin.diagnostics.reportFromPlugin
import org.jetbrains.kotlin.js.resolve.diagnostics.findPsi
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.resolve.BindingTrace
import org.jetbrains.kotlin.resolve.checkers.DeclarationChecker
import org.jetbrains.kotlin.resolve.checkers.DeclarationCheckerContext
import org.jetbrains.kotlin.resolve.hasBackingField
import org.jetbrains.kotlin.resolve.isInlineClassType
import org.jetbrains.kotlin.types.KotlinType
import org.jetbrains.kotlin.types.typeUtil.supertypes
import org.jetbrains.kotlin.util.slicedMap.Slices
import org.jetbrains.kotlin.util.slicedMap.WritableSlice
import org.jetbrains.kotlinx.serialization.compiler.backend.common.AbstractSerialGenerator
import org.jetbrains.kotlinx.serialization.compiler.backend.common.bodyPropertiesDescriptorsMap
import org.jetbrains.kotlinx.serialization.compiler.backend.common.findTypeSerializerOrContext
import org.jetbrains.kotlinx.serialization.compiler.backend.common.primaryConstructorPropertiesDescriptorsMap
import org.jetbrains.kotlinx.serialization.compiler.resolve.*

internal val SERIALIZABLE_PROPERTIES: WritableSlice<ClassDescriptor, SerializableProperties> = Slices.createSimpleSlice()

class SerializationPluginDeclarationChecker : DeclarationChecker {
    override fun check(declaration: KtDeclaration, descriptor: DeclarationDescriptor, context: DeclarationCheckerContext) {
        if (descriptor !is ClassDescriptor) return

        checkCanBeSerializedInternally(descriptor, context.trace)
        val props = buildSerializableProperties(descriptor, context.trace) ?: return
        checkTransients(declaration as KtPureClassOrObject, context.trace)
        analyzePropertiesSerializers(context.trace, descriptor, props.serializableProperties)
    }

    private fun checkCanBeSerializedInternally(descriptor: ClassDescriptor, trace: BindingTrace) {
        if (!descriptor.annotations.hasAnnotation(SerializationAnnotations.serializableAnnotationFqName)) return

        if (descriptor.isInline) {
            trace.reportOnSerializableAnnotation(descriptor, SerializationErrors.INLINE_CLASSES_NOT_SUPPORTED)
            return
        }
        if (!descriptor.hasSerializableAnnotationWithoutArgs) return

        if (!descriptor.isInternalSerializable && !descriptor.hasCompanionObjectAsSerializer) {
            trace.reportOnSerializableAnnotation(descriptor, SerializationErrors.SERIALIZABLE_ANNOTATION_IGNORED)
        }
    }

    private fun buildSerializableProperties(descriptor: ClassDescriptor, trace: BindingTrace): SerializableProperties? {
        if (!descriptor.annotations.hasAnnotation(SerializationAnnotations.serializableAnnotationFqName)) return null
        if (!descriptor.isInternalSerializable) return null
        if (descriptor.hasCompanionObjectAsSerializer) return null // customized by user

        val props = SerializableProperties(descriptor, trace.bindingContext)
        if (!props.isExternallySerializable) trace.reportOnSerializableAnnotation(
            descriptor,
            SerializationErrors.PRIMARY_CONSTRUCTOR_PARAMETER_IS_NOT_A_PROPERTY
        )

        // check that all names are unique
        val namesSet = mutableSetOf<String>()
        props.serializableProperties.forEach {
            if (!namesSet.add(it.name)) {
                descriptor.safeReport { a ->
                    trace.reportFromPlugin(
                        SerializationErrors.DUPLICATE_SERIAL_NAME.on(a, it.name),
                        SerializationPluginErrorsRendering
                    )
                }
            }
        }

        trace.record(SERIALIZABLE_PROPERTIES, descriptor, props)
        return props
    }

    private fun checkTransients(declaration: KtPureClassOrObject, trace: BindingTrace) {
        val propertiesMap: Map<PropertyDescriptor, KtDeclaration> =
            declaration.bodyPropertiesDescriptorsMap(
                trace.bindingContext,
                filterUninitialized = false
            ) + declaration.primaryConstructorPropertiesDescriptorsMap(trace.bindingContext)
        propertiesMap.forEach { (descriptor, declaration) ->
            val isInitialized = declarationHasInitializer(declaration)
            val isMarkedTransient = descriptor.annotations.serialTransient
            val hasBackingField = descriptor.hasBackingField(trace.bindingContext)
            if (!hasBackingField && isMarkedTransient)
                trace.reportFromPlugin(
                    SerializationErrors.TRANSIENT_IS_REDUNDANT.on(declaration),
                    SerializationPluginErrorsRendering
                )

            if (isMarkedTransient && !isInitialized && hasBackingField)
                trace.reportFromPlugin(
                    SerializationErrors.TRANSIENT_MISSING_INITIALIZER.on(declaration),
                    SerializationPluginErrorsRendering
                )
        }
    }

    private fun declarationHasInitializer(declaration: KtDeclaration): Boolean = when (declaration) {
        is KtParameter -> declaration.hasDefaultValue()
        is KtProperty -> declaration.hasDelegateExpressionOrInitializer()
        else -> false
    }

    private fun analyzePropertiesSerializers(trace: BindingTrace, serializableClass: ClassDescriptor, props: List<SerializableProperty>) {
        val generatorContextForAnalysis = object : AbstractSerialGenerator(trace.bindingContext, serializableClass) {}
        props.forEach {
            val serializer = it.serializableWith?.toClassDescriptor
            val ktType = (it.descriptor.findPsi() as? KtCallableDeclaration)?.typeReference ?: return@forEach
            if (serializer != null) {
                val element = ktType.typeElement ?: return
                checkSerializerNullability(it.type, serializer.defaultType, element, trace)
                generatorContextForAnalysis.checkTypeArguments(it.module, it.type, element, trace)
            } else {
                generatorContextForAnalysis.checkType(it.module, it.type, ktType, trace)
            }
        }
    }

    private fun AbstractSerialGenerator.checkTypeArguments(
        module: ModuleDescriptor,
        type: KotlinType,
        element: KtTypeElement,
        trace: BindingTrace
    ) {
        type.arguments.forEachIndexed { i, it -> checkType(module, it.type, element.typeArgumentsAsTypes[i], trace) }
    }

    private fun AbstractSerialGenerator.checkType(
        module: ModuleDescriptor,
        type: KotlinType,
        ktType: KtTypeReference,
        trace: BindingTrace
    ) {
        if (type.genericIndex != null) return
        val element = ktType.typeElement ?: return
        if (type.isInlineClassType()) {
            trace.reportFromPlugin(
                SerializationErrors.INLINE_CLASSES_NOT_SUPPORTED.on(element),
                SerializationPluginErrorsRendering
            )
        }
        val serializer = findTypeSerializerOrContext(module, type)
        if (serializer != null) {
            checkSerializerNullability(type, serializer.defaultType, element, trace)
            checkTypeArguments(module, type, element, trace)
        } else {
            trace.reportFromPlugin(
                SerializationErrors.SERIALIZER_NOT_FOUND.on(element),
                SerializationPluginErrorsRendering
            )
        }
    }

    private fun checkSerializerNullability(
        classType: KotlinType,
        serializerType: KotlinType,
        element: KtTypeElement,
        trace: BindingTrace
    ) {
        // @Serializable annotation has proper signature so this error would be caught in type checker
        val castedToKSerial = serializerType.supertypes().find { isKSerializer(it) } ?: return

        if (!classType.isMarkedNullable && castedToKSerial.arguments.first().type.isMarkedNullable)
            trace.reportFromPlugin(
                SerializationErrors.SERIALIZER_NULLABILITY_INCOMPATIBLE.on(element, serializerType),
                SerializationPluginErrorsRendering
            )
    }

    private inline fun ClassDescriptor.safeReport(report: (KtAnnotationEntry) -> Unit) {
        findSerializableAnnotationDeclaration()?.let(report)
    }

    private fun BindingTrace.reportOnSerializableAnnotation(descriptor: ClassDescriptor, error: DiagnosticFactory0<in KtAnnotationEntry>) {
        descriptor.safeReport { e ->
            reportFromPlugin(
                error.on(e),
                SerializationPluginErrorsRendering
            )
        }
    }

}
