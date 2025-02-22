/*
 * Copyright 2010-2020 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.fir.session

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElementFinder
import com.intellij.psi.search.GlobalSearchScope
import org.jetbrains.annotations.TestOnly
import org.jetbrains.kotlin.analyzer.ModuleInfo
import org.jetbrains.kotlin.config.LanguageVersionSettings
import org.jetbrains.kotlin.config.LanguageVersionSettingsImpl
import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.PrivateSessionConstructor
import org.jetbrains.kotlin.fir.SessionConfiguration
import org.jetbrains.kotlin.fir.analysis.checkers.declaration.DeclarationCheckers
import org.jetbrains.kotlin.fir.analysis.checkers.expression.ExpressionCheckers
import org.jetbrains.kotlin.fir.analysis.checkersComponent
import org.jetbrains.kotlin.fir.analysis.extensions.additionalCheckers
import org.jetbrains.kotlin.fir.caches.FirCachesFactory
import org.jetbrains.kotlin.fir.caches.FirThreadUnsafeCachesFactory
import org.jetbrains.kotlin.fir.checkers.registerCommonCheckers
import org.jetbrains.kotlin.fir.checkers.registerJvmCheckers
import org.jetbrains.kotlin.fir.extensions.BunchOfRegisteredExtensions
import org.jetbrains.kotlin.fir.extensions.extensionService
import org.jetbrains.kotlin.fir.extensions.registerExtensions
import org.jetbrains.kotlin.fir.java.*
import org.jetbrains.kotlin.fir.java.deserialization.KotlinDeserializedJvmSymbolsProvider
import org.jetbrains.kotlin.fir.resolve.providers.FirProvider
import org.jetbrains.kotlin.fir.resolve.providers.FirSymbolProvider
import org.jetbrains.kotlin.fir.resolve.providers.impl.*
import org.jetbrains.kotlin.fir.resolve.scopes.wrapScopeWithJvmMapped
import org.jetbrains.kotlin.fir.scopes.KotlinScopeProvider
import org.jetbrains.kotlin.load.java.JavaClassFinderImpl
import org.jetbrains.kotlin.load.kotlin.PackagePartProvider
import org.jetbrains.kotlin.load.kotlin.VirtualFileFinderFactory

@OptIn(PrivateSessionConstructor::class, SessionConfiguration::class)
object FirSessionFactory {
    class FirSessionConfigurator(private val session: FirSession) {
        private val registeredExtensions = mutableListOf<BunchOfRegisteredExtensions>(BunchOfRegisteredExtensions.empty())

        fun registerExtensions(extensions: BunchOfRegisteredExtensions) {
            registeredExtensions += extensions
        }

        fun useCheckers(checkers: ExpressionCheckers) {
            session.checkersComponent.register(checkers)
        }

        fun useCheckers(checkers: DeclarationCheckers) {
            session.checkersComponent.register(checkers)
        }

        @SessionConfiguration
        fun configure() {
            session.extensionService.registerExtensions(registeredExtensions.reduce(BunchOfRegisteredExtensions::plus))
            session.extensionService.additionalCheckers.forEach(session.checkersComponent::register)
        }
    }

    fun createJavaModuleBasedSession(
        moduleInfo: ModuleInfo,
        sessionProvider: FirProjectSessionProvider,
        scope: GlobalSearchScope,
        project: Project,
        dependenciesProvider: FirSymbolProvider? = null,
        languageVersionSettings: LanguageVersionSettings = LanguageVersionSettingsImpl.DEFAULT,
        init: FirSessionConfigurator.() -> Unit = {}
    ): FirJavaModuleBasedSession {
        return FirJavaModuleBasedSession(moduleInfo, sessionProvider).apply {
            registerThreadUnsafeCaches()
            registerCommonComponents(languageVersionSettings)
            registerResolveComponents()
            registerJavaSpecificResolveComponents()

            val kotlinScopeProvider = KotlinScopeProvider(::wrapScopeWithJvmMapped)

            val firProvider = FirProviderImpl(this, kotlinScopeProvider)
            register(FirProvider::class, firProvider)

            register(
                FirSymbolProvider::class,
                FirCompositeSymbolProvider(
                    this,
                    listOf(
                        firProvider.symbolProvider,
                        JavaSymbolProvider(this, project, scope),
                        dependenciesProvider ?: FirDependenciesSymbolProviderImpl(this)
                    )
                ) as FirSymbolProvider
            )

            FirSessionConfigurator(this).apply {
                registerCommonCheckers()
                registerJvmCheckers()
                init()
            }.configure()

            PsiElementFinder.EP.getPoint(project).registerExtension(FirJavaElementFinder(this, project), project)
        }
    }

    fun createLibrarySession(
        moduleInfo: ModuleInfo,
        sessionProvider: FirProjectSessionProvider,
        scope: GlobalSearchScope,
        project: Project,
        packagePartProvider: PackagePartProvider,
        languageVersionSettings: LanguageVersionSettings = LanguageVersionSettingsImpl.DEFAULT,
    ): FirLibrarySession {
        val javaClassFinder = JavaClassFinderImpl().apply {
            this.setProjectInstance(project)
            this.setScope(scope)
        }

        val kotlinClassFinder = VirtualFileFinderFactory.getInstance(project).create(scope)
        return FirLibrarySession(moduleInfo, sessionProvider).apply {
            registerThreadUnsafeCaches()
            registerCommonComponents(languageVersionSettings)

            val javaSymbolProvider = JavaSymbolProvider(this, project, scope)

            val kotlinScopeProvider = KotlinScopeProvider(::wrapScopeWithJvmMapped)

            val symbolProvider = FirCompositeSymbolProvider(
                this,
                listOf(
                    KotlinDeserializedJvmSymbolsProvider(
                        this, project,
                        packagePartProvider,
                        javaSymbolProvider,
                        kotlinClassFinder,
                        javaClassFinder,
                        kotlinScopeProvider
                    ),
                    FirBuiltinSymbolProvider(this, kotlinScopeProvider),
                    FirCloneableSymbolProvider(this, kotlinScopeProvider),
                    javaSymbolProvider,
                    FirDependenciesSymbolProviderImpl(this)
                )
            )
            register(FirSymbolProvider::class, symbolProvider)
            register(FirProvider::class, FirLibrarySessionProvider(symbolProvider))
        }
    }

    @TestOnly
    fun createEmptySession(): FirSession {
        return object : FirSession(null) {}
    }
}
