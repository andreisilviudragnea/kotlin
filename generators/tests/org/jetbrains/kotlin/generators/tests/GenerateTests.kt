/*
 * Copyright 2010-2020 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.generators.tests

import org.jetbrains.kotlin.AbstractDataFlowValueRenderingTest
import org.jetbrains.kotlin.addImport.AbstractAddImportTest
import org.jetbrains.kotlin.addImportAlias.AbstractAddImportAliasTest
import org.jetbrains.kotlin.allopen.AbstractBytecodeListingTestForAllOpen
import org.jetbrains.kotlin.allopen.AbstractIrBytecodeListingTestForAllOpen
import org.jetbrains.kotlin.android.parcel.AbstractParcelBoxTest
import org.jetbrains.kotlin.android.parcel.AbstractParcelBytecodeListingTest
import org.jetbrains.kotlin.android.parcel.AbstractParcelIrBoxTest
import org.jetbrains.kotlin.android.parcel.AbstractParcelIrBytecodeListingTest
import org.jetbrains.kotlin.android.synthetic.test.AbstractAndroidBoxTest
import org.jetbrains.kotlin.android.synthetic.test.AbstractAndroidBytecodeShapeTest
import org.jetbrains.kotlin.android.synthetic.test.AbstractAndroidIrBoxTest
import org.jetbrains.kotlin.android.synthetic.test.AbstractAndroidSyntheticPropertyDescriptorTest
import org.jetbrains.kotlin.asJava.classes.*
import org.jetbrains.kotlin.checkers.*
import org.jetbrains.kotlin.copyright.AbstractUpdateKotlinCopyrightTest
import org.jetbrains.kotlin.findUsages.*
import org.jetbrains.kotlin.fir.plugin.AbstractFirAllOpenDiagnosticTest
import org.jetbrains.kotlin.formatter.AbstractFormatterTest
import org.jetbrains.kotlin.formatter.AbstractTypingIndentationTestBase
import org.jetbrains.kotlin.generators.TestGroup
import org.jetbrains.kotlin.generators.impl.generateTestGroupSuite
import org.jetbrains.kotlin.generators.util.TestGeneratorUtil.KT_OR_KTS
import org.jetbrains.kotlin.generators.util.TestGeneratorUtil.KT_OR_KTS_WITHOUT_DOTS_IN_NAME
import org.jetbrains.kotlin.generators.util.TestGeneratorUtil.KT_WITHOUT_DOTS_IN_NAME
import org.jetbrains.kotlin.idea.AbstractExpressionSelectionTest
import org.jetbrains.kotlin.idea.AbstractSmartSelectionTest
import org.jetbrains.kotlin.idea.actions.AbstractGotoTestOrCodeActionTest
import org.jetbrains.kotlin.idea.caches.resolve.*
import org.jetbrains.kotlin.idea.codeInsight.*
import org.jetbrains.kotlin.idea.codeInsight.codevision.AbstractKotlinCodeVisionProviderTest
import org.jetbrains.kotlin.idea.codeInsight.generate.AbstractCodeInsightActionTest
import org.jetbrains.kotlin.idea.codeInsight.generate.AbstractGenerateHashCodeAndEqualsActionTest
import org.jetbrains.kotlin.idea.codeInsight.generate.AbstractGenerateTestSupportMethodActionTest
import org.jetbrains.kotlin.idea.codeInsight.generate.AbstractGenerateToStringActionTest
import org.jetbrains.kotlin.idea.codeInsight.hints.AbstractKotlinReferenceTypeHintsProviderTest
import org.jetbrains.kotlin.idea.codeInsight.moveUpDown.AbstractMoveLeftRightTest
import org.jetbrains.kotlin.idea.codeInsight.moveUpDown.AbstractMoveStatementTest
import org.jetbrains.kotlin.idea.codeInsight.postfix.AbstractPostfixTemplateProviderTest
import org.jetbrains.kotlin.idea.codeInsight.surroundWith.AbstractSurroundWithTest
import org.jetbrains.kotlin.idea.codeInsight.unwrap.AbstractUnwrapRemoveTest
import org.jetbrains.kotlin.idea.completion.AbstractHighLevelJvmBasicCompletionTest
import org.jetbrains.kotlin.idea.completion.AbstractHighLevelMultiFileJvmBasicCompletionTest
import org.jetbrains.kotlin.idea.completion.test.*
import org.jetbrains.kotlin.idea.completion.test.handlers.*
import org.jetbrains.kotlin.idea.completion.test.weighers.AbstractBasicCompletionWeigherTest
import org.jetbrains.kotlin.idea.completion.test.weighers.AbstractSmartCompletionWeigherTest
import org.jetbrains.kotlin.idea.completion.wheigher.AbstractHighLevelWeigherTest
import org.jetbrains.kotlin.idea.configuration.AbstractGradleConfigureProjectByChangingFileTest
import org.jetbrains.kotlin.idea.conversion.copy.AbstractJavaToKotlinCopyPasteConversionTest
import org.jetbrains.kotlin.idea.conversion.copy.AbstractLiteralKotlinToKotlinCopyPasteTest
import org.jetbrains.kotlin.idea.conversion.copy.AbstractLiteralTextToKotlinCopyPasteTest
import org.jetbrains.kotlin.idea.conversion.copy.AbstractTextJavaToKotlinCopyPasteConversionTest
import org.jetbrains.kotlin.idea.coverage.AbstractKotlinCoverageOutputFilesTest
import org.jetbrains.kotlin.idea.debugger.evaluate.AbstractCodeFragmentAutoImportTest
import org.jetbrains.kotlin.idea.debugger.evaluate.AbstractCodeFragmentCompletionHandlerTest
import org.jetbrains.kotlin.idea.debugger.evaluate.AbstractCodeFragmentCompletionTest
import org.jetbrains.kotlin.idea.debugger.evaluate.AbstractCodeFragmentHighlightingTest
import org.jetbrains.kotlin.idea.debugger.test.*
import org.jetbrains.kotlin.idea.debugger.test.sequence.exec.AbstractSequenceTraceTestCase
import org.jetbrains.kotlin.idea.decompiler.navigation.AbstractNavigateJavaToLibrarySourceTest
import org.jetbrains.kotlin.idea.decompiler.navigation.AbstractNavigateToDecompiledLibraryTest
import org.jetbrains.kotlin.idea.decompiler.navigation.AbstractNavigateToLibrarySourceTest
import org.jetbrains.kotlin.idea.decompiler.navigation.AbstractNavigateToLibrarySourceTestWithJS
import org.jetbrains.kotlin.idea.decompiler.stubBuilder.AbstractClsStubBuilderTest
import org.jetbrains.kotlin.idea.decompiler.stubBuilder.AbstractLoadJavaClsStubTest
import org.jetbrains.kotlin.idea.decompiler.textBuilder.AbstractCommonDecompiledTextFromJsMetadataTest
import org.jetbrains.kotlin.idea.decompiler.textBuilder.AbstractCommonDecompiledTextTest
import org.jetbrains.kotlin.idea.decompiler.textBuilder.AbstractJsDecompiledTextFromJsMetadataTest
import org.jetbrains.kotlin.idea.decompiler.textBuilder.AbstractJvmDecompiledTextTest
import org.jetbrains.kotlin.idea.editor.AbstractEnterAfterUnmatchedBraceHandlerTest
import org.jetbrains.kotlin.idea.editor.AbstractMultiLineStringIndentTest
import org.jetbrains.kotlin.idea.editor.backspaceHandler.AbstractBackspaceHandlerTest
import org.jetbrains.kotlin.idea.editor.quickDoc.AbstractQuickDocProviderTest
import org.jetbrains.kotlin.idea.filters.AbstractKotlinExceptionFilterTest
import org.jetbrains.kotlin.idea.fir.AbstractKtDeclarationAndFirDeclarationEqualityChecker
import org.jetbrains.kotlin.idea.fir.low.level.api.*
import org.jetbrains.kotlin.idea.fir.low.level.api.AbstractFirLazyDeclarationResolveTest
import org.jetbrains.kotlin.idea.fir.low.level.api.AbstractFirLazyResolveTest
import org.jetbrains.kotlin.idea.fir.low.level.api.AbstractFirMultiModuleLazyResolveTest
import org.jetbrains.kotlin.idea.fir.low.level.api.AbstractFirMultiModuleResolveTest
import org.jetbrains.kotlin.idea.fir.low.level.api.diagnostic.AbstractDiagnosticTraversalCounterTest
import org.jetbrains.kotlin.idea.fir.low.level.api.file.structure.AbstractFileStructureAndOutOfBlockModificationTrackerConsistencyTest
import org.jetbrains.kotlin.idea.fir.low.level.api.file.structure.AbstractFileStructureTest
import org.jetbrains.kotlin.idea.fir.low.level.api.resolve.AbstractInnerDeclarationsResolvePhaseTest
import org.jetbrains.kotlin.idea.fir.low.level.api.sessions.AbstractSessionsInvalidationTest
import org.jetbrains.kotlin.idea.fir.low.level.api.trackers.AbstractProjectWideOutOfBlockKotlinModificationTrackerTest
import org.jetbrains.kotlin.idea.folding.AbstractKotlinFoldingTest
import org.jetbrains.kotlin.idea.frontend.api.components.AbstractExpectedExpressionTypeTest
import org.jetbrains.kotlin.idea.frontend.api.components.AbstractHLExpressionTypeTest
import org.jetbrains.kotlin.idea.frontend.api.components.AbstractOverriddenDeclarationProviderTest
import org.jetbrains.kotlin.idea.frontend.api.components.AbstractReturnExpressionTargetTest
import org.jetbrains.kotlin.idea.frontend.api.fir.AbstractResolveCallTest
import org.jetbrains.kotlin.idea.frontend.api.scopes.AbstractFileScopeTest
import org.jetbrains.kotlin.idea.frontend.api.scopes.AbstractMemberScopeByFqNameTest
import org.jetbrains.kotlin.idea.frontend.api.symbols.*
import org.jetbrains.kotlin.idea.hierarchy.AbstractHierarchyTest
import org.jetbrains.kotlin.idea.hierarchy.AbstractHierarchyWithLibTest
import org.jetbrains.kotlin.idea.highlighter.*
import org.jetbrains.kotlin.idea.imports.AbstractJsOptimizeImportsTest
import org.jetbrains.kotlin.idea.imports.AbstractJvmOptimizeImportsTest
import org.jetbrains.kotlin.idea.index.AbstractKotlinTypeAliasByExpansionShortNameIndexTest
import org.jetbrains.kotlin.idea.inspections.AbstractHLInspectionTest
import org.jetbrains.kotlin.idea.inspections.AbstractHLLocalInspectionTest
import org.jetbrains.kotlin.idea.inspections.AbstractLocalInspectionTest
import org.jetbrains.kotlin.idea.inspections.AbstractMultiFileLocalInspectionTest
import org.jetbrains.kotlin.idea.intentions.*
import org.jetbrains.kotlin.idea.intentions.declarations.AbstractJoinLinesTest
import org.jetbrains.kotlin.idea.internal.AbstractBytecodeToolWindowTest
import org.jetbrains.kotlin.idea.kdoc.AbstractKDocHighlightingTest
import org.jetbrains.kotlin.idea.kdoc.AbstractKDocTypingTest
import org.jetbrains.kotlin.idea.maven.AbstractKotlinMavenInspectionTest
import org.jetbrains.kotlin.idea.maven.configuration.AbstractMavenConfigureProjectByChangingFileTest
import org.jetbrains.kotlin.idea.navigation.*
import org.jetbrains.kotlin.idea.navigationToolbar.AbstractKotlinNavBarTest
import org.jetbrains.kotlin.idea.parameterInfo.AbstractParameterInfoTest
import org.jetbrains.kotlin.idea.perf.*
import org.jetbrains.kotlin.idea.quickfix.AbstractHighLevelQuickFixTest
import org.jetbrains.kotlin.idea.quickfix.AbstractQuickFixMultiFileTest
import org.jetbrains.kotlin.idea.quickfix.AbstractQuickFixMultiModuleTest
import org.jetbrains.kotlin.idea.quickfix.AbstractQuickFixTest
import org.jetbrains.kotlin.idea.refactoring.AbstractNameSuggestionProviderTest
import org.jetbrains.kotlin.idea.refactoring.copy.AbstractCopyTest
import org.jetbrains.kotlin.idea.refactoring.copy.AbstractMultiModuleCopyTest
import org.jetbrains.kotlin.idea.refactoring.inline.AbstractInlineTest
import org.jetbrains.kotlin.idea.refactoring.introduce.AbstractExtractionTest
import org.jetbrains.kotlin.idea.refactoring.move.AbstractMoveTest
import org.jetbrains.kotlin.idea.refactoring.move.AbstractMultiModuleMoveTest
import org.jetbrains.kotlin.idea.refactoring.pullUp.AbstractPullUpTest
import org.jetbrains.kotlin.idea.refactoring.pushDown.AbstractPushDownTest
import org.jetbrains.kotlin.idea.refactoring.rename.AbstractMultiModuleRenameTest
import org.jetbrains.kotlin.idea.refactoring.rename.AbstractRenameTest
import org.jetbrains.kotlin.idea.refactoring.safeDelete.AbstractMultiModuleSafeDeleteTest
import org.jetbrains.kotlin.idea.refactoring.safeDelete.AbstractSafeDeleteTest
import org.jetbrains.kotlin.idea.repl.AbstractIdeReplCompletionTest
import org.jetbrains.kotlin.idea.resolve.*
import org.jetbrains.kotlin.idea.scratch.AbstractScratchLineMarkersTest
import org.jetbrains.kotlin.idea.scratch.AbstractScratchRunActionTest
import org.jetbrains.kotlin.idea.script.*
import org.jetbrains.kotlin.idea.slicer.AbstractSlicerLeafGroupingTest
import org.jetbrains.kotlin.idea.slicer.AbstractSlicerMultiplatformTest
import org.jetbrains.kotlin.idea.slicer.AbstractSlicerNullnessGroupingTest
import org.jetbrains.kotlin.idea.slicer.AbstractSlicerTreeTest
import org.jetbrains.kotlin.idea.structureView.AbstractKotlinFileStructureTest
import org.jetbrains.kotlin.idea.stubs.AbstractMultiFileHighlightingTest
import org.jetbrains.kotlin.idea.stubs.AbstractResolveByStubTest
import org.jetbrains.kotlin.idea.stubs.AbstractStubBuilderTest
import org.jetbrains.kotlin.incremental.*
import org.jetbrains.kotlin.j2k.AbstractJavaToKotlinConverterForWebDemoTest
import org.jetbrains.kotlin.j2k.AbstractJavaToKotlinConverterMultiFileTest
import org.jetbrains.kotlin.j2k.AbstractJavaToKotlinConverterSingleFileTest
import org.jetbrains.kotlin.jps.build.*
import org.jetbrains.kotlin.jps.build.dependeciestxt.actualizeMppJpsIncTestCaseDirs
import org.jetbrains.kotlin.jps.incremental.AbstractJsProtoComparisonTest
import org.jetbrains.kotlin.jps.incremental.AbstractJvmProtoComparisonTest
import org.jetbrains.kotlin.jvm.abi.AbstractCompareJvmAbiTest
import org.jetbrains.kotlin.jvm.abi.AbstractCompileAgainstJvmAbiTest
import org.jetbrains.kotlin.jvm.abi.AbstractJvmAbiContentTest
import org.jetbrains.kotlin.kapt.cli.test.AbstractArgumentParsingTest
import org.jetbrains.kotlin.kapt.cli.test.AbstractKaptToolIntegrationTest
import org.jetbrains.kotlin.kapt3.test.AbstractClassFileToSourceStubConverterTest
import org.jetbrains.kotlin.kapt3.test.AbstractIrClassFileToSourceStubConverterTest
import org.jetbrains.kotlin.kapt3.test.AbstractIrKotlinKaptContextTest
import org.jetbrains.kotlin.kapt3.test.AbstractKotlinKaptContextTest
import org.jetbrains.kotlin.nj2k.AbstractNewJavaToKotlinConverterMultiFileTest
import org.jetbrains.kotlin.nj2k.AbstractNewJavaToKotlinConverterSingleFileTest
import org.jetbrains.kotlin.nj2k.AbstractNewJavaToKotlinCopyPasteConversionTest
import org.jetbrains.kotlin.nj2k.AbstractTextNewJavaToKotlinCopyPasteConversionTest
import org.jetbrains.kotlin.nj2k.inference.common.AbstractCommonConstraintCollectorTest
import org.jetbrains.kotlin.nj2k.inference.mutability.AbstractMutabilityInferenceTest
import org.jetbrains.kotlin.nj2k.inference.nullability.AbstractNullabilityInferenceTest
import org.jetbrains.kotlin.noarg.*
import org.jetbrains.kotlin.pacelize.ide.test.AbstractParcelizeCheckerTest
import org.jetbrains.kotlin.pacelize.ide.test.AbstractParcelizeQuickFixTest
import org.jetbrains.kotlin.parcelize.test.AbstractParcelizeBoxTest
import org.jetbrains.kotlin.parcelize.test.AbstractParcelizeBytecodeListingTest
import org.jetbrains.kotlin.parcelize.test.AbstractParcelizeIrBoxTest
import org.jetbrains.kotlin.parcelize.test.AbstractParcelizeIrBytecodeListingTest
import org.jetbrains.kotlin.psi.patternMatching.AbstractPsiUnifierTest
import org.jetbrains.kotlin.samWithReceiver.AbstractSamWithReceiverScriptTest
import org.jetbrains.kotlin.samWithReceiver.AbstractSamWithReceiverTest
import org.jetbrains.kotlin.search.AbstractAnnotatedMembersSearchTest
import org.jetbrains.kotlin.search.AbstractInheritorsSearchTest
import org.jetbrains.kotlin.shortenRefs.AbstractFirShortenRefsTest
import org.jetbrains.kotlin.shortenRefs.AbstractShortenRefsTest
import org.jetbrains.kotlin.test.TargetBackend
import org.jetbrains.kotlin.tools.projectWizard.cli.AbstractProjectTemplateBuildFileGenerationTest
import org.jetbrains.kotlin.tools.projectWizard.cli.AbstractYamlBuildFileGenerationTest
import org.jetbrains.kotlin.tools.projectWizard.wizard.AbstractProjectTemplateNewWizardProjectImportTest
import org.jetbrains.kotlin.tools.projectWizard.wizard.AbstractYamlNewWizardProjectImportTest
import org.jetbrains.kotlinx.serialization.AbstractSerializationIrBytecodeListingTest
import org.jetbrains.kotlinx.serialization.AbstractSerializationPluginBytecodeListingTest
import org.jetbrains.kotlinx.serialization.AbstractSerializationPluginDiagnosticTest
import org.jetbrains.kotlinx.serialization.idea.AbstractSerializationPluginIdeDiagnosticTest
import org.jetbrains.kotlinx.serialization.idea.AbstractSerializationQuickFixTest

fun main(args: Array<String>) {
    System.setProperty("java.awt.headless", "true")
    generateTestGroupSuite(args) {
        testGroup("idea/jvm-debugger/jvm-debugger-test/test", "idea/jvm-debugger/jvm-debugger-test/testData") {
            testClass<AbstractKotlinSteppingTest> {
                model(
                    "stepping/stepIntoAndSmartStepInto",
                    pattern = KT_WITHOUT_DOTS_IN_NAME,
                    testMethod = "doStepIntoTest",
                    testClassName = "StepInto"
                )
                model(
                    "stepping/stepIntoAndSmartStepInto",
                    pattern = KT_WITHOUT_DOTS_IN_NAME,
                    testMethod = "doSmartStepIntoTest",
                    testClassName = "SmartStepInto"
                )
                model(
                    "stepping/stepInto",
                    pattern = KT_WITHOUT_DOTS_IN_NAME,
                    testMethod = "doStepIntoTest",
                    testClassName = "StepIntoOnly"
                )
                model("stepping/stepOut", pattern = KT_WITHOUT_DOTS_IN_NAME, testMethod = "doStepOutTest")
                model("stepping/stepOver", pattern = KT_WITHOUT_DOTS_IN_NAME, testMethod = "doStepOverTest")
                model("stepping/filters", pattern = KT_WITHOUT_DOTS_IN_NAME, testMethod = "doStepIntoTest")
                model("stepping/custom", pattern = KT_WITHOUT_DOTS_IN_NAME, testMethod = "doCustomTest")
            }

            testClass<AbstractIrKotlinSteppingTest> {
                model(
                    "stepping/stepIntoAndSmartStepInto",
                    pattern = KT_WITHOUT_DOTS_IN_NAME,
                    testMethod = "doStepIntoTest",
                    testClassName = "StepInto"
                )
                model(
                    "stepping/stepIntoAndSmartStepInto",
                    pattern = KT_WITHOUT_DOTS_IN_NAME,
                    testMethod = "doSmartStepIntoTest",
                    testClassName = "SmartStepInto"
                )
                model(
                    "stepping/stepInto",
                    pattern = KT_WITHOUT_DOTS_IN_NAME,
                    testMethod = "doStepIntoTest",
                    testClassName = "StepIntoOnly"
                )
                model("stepping/stepOut", pattern = KT_WITHOUT_DOTS_IN_NAME, testMethod = "doStepOutTest")
                model("stepping/stepOver", pattern = KT_WITHOUT_DOTS_IN_NAME, testMethod = "doStepOverTest")
                model("stepping/filters", pattern = KT_WITHOUT_DOTS_IN_NAME, testMethod = "doStepIntoTest")
                model("stepping/custom", pattern = KT_WITHOUT_DOTS_IN_NAME, testMethod = "doCustomTest")
            }

            testClass<AbstractKotlinEvaluateExpressionTest> {
                model("evaluation/singleBreakpoint", testMethod = "doSingleBreakpointTest")
                model("evaluation/multipleBreakpoints", testMethod = "doMultipleBreakpointsTest")
            }

            testClass<AbstractIrKotlinEvaluateExpressionTest> {
                model("evaluation/singleBreakpoint", testMethod = "doSingleBreakpointTest", targetBackend = TargetBackend.JVM_IR)
                model("evaluation/multipleBreakpoints", testMethod = "doMultipleBreakpointsTest", targetBackend = TargetBackend.JVM_IR)
            }

            testClass<AbstractSelectExpressionForDebuggerTest> {
                model("selectExpression", recursive = false)
                model("selectExpression/disallowMethodCalls", testMethod = "doTestWoMethodCalls")
            }

            testClass<AbstractPositionManagerTest> {
                model("positionManager", recursive = false, extension = "kt", testClassName = "SingleFile")
                model("positionManager", recursive = false, extension = null, testClassName = "MultiFile")
            }

            testClass<AbstractSmartStepIntoTest> {
                model("smartStepInto")
            }

            testClass<AbstractBreakpointApplicabilityTest> {
                model("breakpointApplicability")
            }

            testClass<AbstractFileRankingTest> {
                model("fileRanking")
            }

            testClass<AbstractAsyncStackTraceTest> {
                model("asyncStackTrace")
            }

            testClass<AbstractCoroutineDumpTest> {
                model("coroutines")
            }

            testClass<AbstractSequenceTraceTestCase> {
                // TODO: implement mapping logic for terminal operations
                model("sequence/streams/sequence", excludeDirs = listOf("terminal"))
            }

            testClass<AbstractContinuationStackTraceTest> {
                model("continuation")
            }

            testClass<AbstractXCoroutinesStackTraceTest> {
                model("xcoroutines")
            }
        }

        testGroup("idea/tests", "idea/testData") {
            testClass<AbstractAdditionalResolveDescriptorRendererTest> {
                model("resolve/additionalLazyResolve")
            }

            testClass<AbstractPartialBodyResolveTest> {
                model("resolve/partialBodyResolve")
            }

            testClass<AbstractResolveModeComparisonTest> {
                model("resolve/resolveModeComparison")
            }

            testClass<AbstractKotlinHighlightingPassTest> {
                model("checker", recursive = false)
                model("checker/regression")
                model("checker/recovery")
                model("checker/rendering")
                model("checker/scripts", extension = "kts")
                model("checker/duplicateJvmSignature")
                model("checker/infos", testMethod = "doTestWithInfos")
                model("checker/diagnosticsMessage")
            }

            testClass<AbstractKotlinHighlightWolfPassTest> {
                model("checker/wolf")
            }

            testClass<AbstractJavaAgainstKotlinSourceCheckerTest> {
                model("kotlinAndJavaChecker/javaAgainstKotlin")
                model("kotlinAndJavaChecker/javaWithKotlin")
            }

            testClass<AbstractJavaAgainstKotlinSourceCheckerWithoutUltraLightTest> {
                model("kotlinAndJavaChecker/javaAgainstKotlin")
                model("kotlinAndJavaChecker/javaWithKotlin")
            }

            testClass<AbstractJavaAgainstKotlinBinariesCheckerTest> {
                model("kotlinAndJavaChecker/javaAgainstKotlin")
            }

            testClass<AbstractPsiUnifierTest> {
                model("unifier")
            }

            testClass<AbstractCodeFragmentHighlightingTest> {
                model("checker/codeFragments", extension = "kt", recursive = false)
                model("checker/codeFragments/imports", testMethod = "doTestWithImport", extension = "kt")
            }

            testClass<AbstractCodeFragmentAutoImportTest> {
                model("quickfix.special/codeFragmentAutoImport", extension = "kt", recursive = false)
            }

            testClass<AbstractJsCheckerTest> {
                model("checker/js")
            }

            testClass<AbstractQuickFixTest> {
                model("quickfix", pattern = "^([\\w\\-_]+)\\.kt$", filenameStartsLowerCase = true)
            }

            testClass<AbstractGotoSuperTest> {
                model("navigation/gotoSuper", extension = "test", recursive = false)
            }

            testClass<AbstractGotoTypeDeclarationTest> {
                model("navigation/gotoTypeDeclaration", extension = "test")
            }

            testClass<AbstractGotoDeclarationTest> {
                model("navigation/gotoDeclaration", extension = "test")
            }

            testClass<AbstractParameterInfoTest> {
                model(
                    "parameterInfo",
                    pattern = "^([\\w\\-_]+)\\.kt$", recursive = true,
                    excludeDirs = listOf("withLib1/sharedLib", "withLib2/sharedLib", "withLib3/sharedLib")
                )
            }

            testClass<AbstractKotlinGotoTest> {
                model("navigation/gotoClass", testMethod = "doClassTest")
                model("navigation/gotoSymbol", testMethod = "doSymbolTest")
            }

            testClass<AbstractNavigateToLibrarySourceTest>() {
                model("decompiler/navigation/usercode")
            }

            testClass<AbstractNavigateJavaToLibrarySourceTest>() {
                model("decompiler/navigation/userJavaCode", pattern = "^(.+)\\.java$")
            }

            testClass<AbstractNavigateToLibrarySourceTestWithJS>() {
                model("decompiler/navigation/usercode", testClassName = "UsercodeWithJSModule")
            }

            testClass<AbstractNavigateToDecompiledLibraryTest> {
                model("decompiler/navigation/usercode")
            }

            testClass<AbstractKotlinGotoImplementationTest> {
                model("navigation/implementations", recursive = false)
            }

            testClass<AbstractGotoTestOrCodeActionTest> {
                model("navigation/gotoTestOrCode", pattern = "^(.+)\\.main\\..+\$")
            }

            testClass<AbstractInheritorsSearchTest> {
                model("search/inheritance")
            }

            testClass<AbstractAnnotatedMembersSearchTest> {
                model("search/annotations")
            }

            testClass<AbstractQuickFixMultiFileTest> {
                model("quickfix", pattern = """^(\w+)\.((before\.Main\.\w+)|(test))$""", testMethod = "doTestWithExtraFile")
            }

            testClass<AbstractKotlinTypeAliasByExpansionShortNameIndexTest> {
                model("typealiasExpansionIndex")
            }

            testClass<AbstractHighlightingTest> {
                model("highlighter")
            }

            testClass<AbstractDslHighlighterTest> {
                model("dslHighlighter")
            }

            testClass<AbstractUsageHighlightingTest> {
                model("usageHighlighter")
            }

            testClass<AbstractKotlinFoldingTest> {
                model("folding/noCollapse")
                model("folding/checkCollapse", testMethod = "doSettingsFoldingTest")
            }

            testClass<AbstractSurroundWithTest> {
                model("codeInsight/surroundWith/if", testMethod = "doTestWithIfSurrounder")
                model("codeInsight/surroundWith/ifElse", testMethod = "doTestWithIfElseSurrounder")
                model("codeInsight/surroundWith/ifElseExpression", testMethod = "doTestWithIfElseExpressionSurrounder")
                model("codeInsight/surroundWith/ifElseExpressionBraces", testMethod = "doTestWithIfElseExpressionBracesSurrounder")
                model("codeInsight/surroundWith/not", testMethod = "doTestWithNotSurrounder")
                model("codeInsight/surroundWith/parentheses", testMethod = "doTestWithParenthesesSurrounder")
                model("codeInsight/surroundWith/stringTemplate", testMethod = "doTestWithStringTemplateSurrounder")
                model("codeInsight/surroundWith/when", testMethod = "doTestWithWhenSurrounder")
                model("codeInsight/surroundWith/tryCatch", testMethod = "doTestWithTryCatchSurrounder")
                model("codeInsight/surroundWith/tryCatchExpression", testMethod = "doTestWithTryCatchExpressionSurrounder")
                model("codeInsight/surroundWith/tryCatchFinally", testMethod = "doTestWithTryCatchFinallySurrounder")
                model("codeInsight/surroundWith/tryCatchFinallyExpression", testMethod = "doTestWithTryCatchFinallyExpressionSurrounder")
                model("codeInsight/surroundWith/tryFinally", testMethod = "doTestWithTryFinallySurrounder")
                model("codeInsight/surroundWith/functionLiteral", testMethod = "doTestWithFunctionLiteralSurrounder")
                model("codeInsight/surroundWith/withIfExpression", testMethod = "doTestWithSurroundWithIfExpression")
                model("codeInsight/surroundWith/withIfElseExpression", testMethod = "doTestWithSurroundWithIfElseExpression")
            }

            testClass<AbstractJoinLinesTest> {
                model("joinLines")
            }

            testClass<AbstractBreadcrumbsTest> {
                model("codeInsight/breadcrumbs")
            }

            testClass<AbstractIntentionTest> {
                model("intentions", pattern = "^([\\w\\-_]+)\\.(kt|kts)$")
            }

            testClass<AbstractIntentionTest2> {
                model("intentions/loopToCallChain", pattern = "^([\\w\\-_]+)\\.kt$")
            }

            testClass<AbstractConcatenatedStringGeneratorTest> {
                model("concatenatedStringGenerator", pattern = "^([\\w\\-_]+)\\.kt$")
            }

            testClass<AbstractInspectionTest> {
                model("intentions", pattern = "^(inspections\\.test)$", singleClass = true)
                model("inspections", pattern = "^(inspections\\.test)$", singleClass = true)
                model("inspectionsLocal", pattern = "^(inspections\\.test)$", singleClass = true)
            }

            testClass<AbstractLocalInspectionTest> {
                model("inspectionsLocal", pattern = "^([\\w\\-_]+)\\.(kt|kts)$")
            }

            testClass<AbstractHierarchyTest> {
                model("hierarchy/class/type", extension = null, recursive = false, testMethod = "doTypeClassHierarchyTest")
                model("hierarchy/class/super", extension = null, recursive = false, testMethod = "doSuperClassHierarchyTest")
                model("hierarchy/class/sub", extension = null, recursive = false, testMethod = "doSubClassHierarchyTest")
                model("hierarchy/calls/callers", extension = null, recursive = false, testMethod = "doCallerHierarchyTest")
                model("hierarchy/calls/callersJava", extension = null, recursive = false, testMethod = "doCallerJavaHierarchyTest")
                model("hierarchy/calls/callees", extension = null, recursive = false, testMethod = "doCalleeHierarchyTest")
                model("hierarchy/overrides", extension = null, recursive = false, testMethod = "doOverrideHierarchyTest")
            }

            testClass<AbstractHierarchyWithLibTest> {
                model("hierarchy/withLib", extension = null, recursive = false)
            }

            testClass<AbstractMoveStatementTest> {
                model("codeInsight/moveUpDown/classBodyDeclarations", pattern = KT_OR_KTS, testMethod = "doTestClassBodyDeclaration")
                model("codeInsight/moveUpDown/closingBraces", testMethod = "doTestExpression")
                model("codeInsight/moveUpDown/expressions", pattern = KT_OR_KTS, testMethod = "doTestExpression")
                model("codeInsight/moveUpDown/line", testMethod = "doTestLine")
                model("codeInsight/moveUpDown/parametersAndArguments", testMethod = "doTestExpression")
                model("codeInsight/moveUpDown/trailingComma", testMethod = "doTestExpressionWithTrailingComma")
            }

            testClass<AbstractMoveLeftRightTest> {
                model("codeInsight/moveLeftRight")
            }

            testClass<AbstractInlineTest> {
                model("refactoring/inline", pattern = "^(\\w+)\\.kt$")
            }

            testClass<AbstractUnwrapRemoveTest> {
                model("codeInsight/unwrapAndRemove/removeExpression", testMethod = "doTestExpressionRemover")
                model("codeInsight/unwrapAndRemove/unwrapThen", testMethod = "doTestThenUnwrapper")
                model("codeInsight/unwrapAndRemove/unwrapElse", testMethod = "doTestElseUnwrapper")
                model("codeInsight/unwrapAndRemove/removeElse", testMethod = "doTestElseRemover")
                model("codeInsight/unwrapAndRemove/unwrapLoop", testMethod = "doTestLoopUnwrapper")
                model("codeInsight/unwrapAndRemove/unwrapTry", testMethod = "doTestTryUnwrapper")
                model("codeInsight/unwrapAndRemove/unwrapCatch", testMethod = "doTestCatchUnwrapper")
                model("codeInsight/unwrapAndRemove/removeCatch", testMethod = "doTestCatchRemover")
                model("codeInsight/unwrapAndRemove/unwrapFinally", testMethod = "doTestFinallyUnwrapper")
                model("codeInsight/unwrapAndRemove/removeFinally", testMethod = "doTestFinallyRemover")
                model("codeInsight/unwrapAndRemove/unwrapLambda", testMethod = "doTestLambdaUnwrapper")
                model("codeInsight/unwrapAndRemove/unwrapFunctionParameter", testMethod = "doTestFunctionParameterUnwrapper")
            }

            testClass<AbstractExpressionTypeTest> {
                model("codeInsight/expressionType")
            }

            testClass<AbstractRenderingKDocTest> {
                model("codeInsight/renderingKDoc")
            }

            testClass<AbstractBackspaceHandlerTest> {
                model("editor/backspaceHandler")
            }

            testClass<AbstractEnterAfterUnmatchedBraceHandlerTest> {
                model("editor/enterHandler/afterUnmatchedBrace")
            }

            testClass<AbstractMultiLineStringIndentTest> {
                model("editor/enterHandler/multilineString")
            }

            testClass<AbstractQuickDocProviderTest> {
                model("editor/quickDoc", pattern = """^([^_]+)\.(kt|java)$""")
            }

            testClass<AbstractSafeDeleteTest> {
                model("refactoring/safeDelete/deleteClass/kotlinClass", testMethod = "doClassTest")
                model("refactoring/safeDelete/deleteClass/kotlinClassWithJava", testMethod = "doClassTestWithJava")
                model("refactoring/safeDelete/deleteClass/javaClassWithKotlin", extension = "java", testMethod = "doJavaClassTest")
                model("refactoring/safeDelete/deleteObject/kotlinObject", testMethod = "doObjectTest")
                model("refactoring/safeDelete/deleteFunction/kotlinFunction", testMethod = "doFunctionTest")
                model("refactoring/safeDelete/deleteFunction/kotlinFunctionWithJava", testMethod = "doFunctionTestWithJava")
                model("refactoring/safeDelete/deleteFunction/javaFunctionWithKotlin", testMethod = "doJavaMethodTest")
                model("refactoring/safeDelete/deleteProperty/kotlinProperty", testMethod = "doPropertyTest")
                model("refactoring/safeDelete/deleteProperty/kotlinPropertyWithJava", testMethod = "doPropertyTestWithJava")
                model("refactoring/safeDelete/deleteProperty/javaPropertyWithKotlin", testMethod = "doJavaPropertyTest")
                model("refactoring/safeDelete/deleteTypeAlias/kotlinTypeAlias", testMethod = "doTypeAliasTest")
                model("refactoring/safeDelete/deleteTypeParameter/kotlinTypeParameter", testMethod = "doTypeParameterTest")
                model("refactoring/safeDelete/deleteTypeParameter/kotlinTypeParameterWithJava", testMethod = "doTypeParameterTestWithJava")
                model("refactoring/safeDelete/deleteValueParameter/kotlinValueParameter", testMethod = "doValueParameterTest")
                model(
                    "refactoring/safeDelete/deleteValueParameter/kotlinValueParameterWithJava",
                    testMethod = "doValueParameterTestWithJava"
                )
            }

            testClass<AbstractReferenceResolveTest> {
                model("resolve/references", pattern = KT_WITHOUT_DOTS_IN_NAME)
            }

            testClass<AbstractReferenceResolveInJavaTest> {
                model("resolve/referenceInJava/binaryAndSource", extension = "java")
                model("resolve/referenceInJava/sourceOnly", extension = "java")
            }

            testClass<AbstractReferenceToCompiledKotlinResolveInJavaTest> {
                model("resolve/referenceInJava/binaryAndSource", extension = "java")
            }

            testClass<AbstractReferenceResolveWithLibTest> {
                model("resolve/referenceWithLib", recursive = false)
            }

            testClass<AbstractReferenceResolveInLibrarySourcesTest> {
                model("resolve/referenceInLib", recursive = false)
            }

            testClass<AbstractReferenceToJavaWithWrongFileStructureTest> {
                model("resolve/referenceToJavaWithWrongFileStructure", recursive = false)
            }

            testClass<AbstractFindUsagesTest> {
                model("findUsages/kotlin", pattern = """^(.+)\.0\.(kt|kts)$""")
                model("findUsages/java", pattern = """^(.+)\.0\.java$""")
                model("findUsages/propertyFiles", pattern = """^(.+)\.0\.properties$""")
            }

            testClass<AbstractFindUsagesWithDisableComponentSearchTest> {
                model("findUsages/kotlin/conventions/components", pattern = """^(.+)\.0\.(kt|kts)$""")
            }

            testClass<AbstractKotlinFindUsagesWithLibraryTest> {
                model("findUsages/libraryUsages", pattern = """^(.+)\.0\.kt$""")
            }

            testClass<AbstractKotlinFindUsagesWithStdlibTest> {
                model("findUsages/stdlibUsages", pattern = """^(.+)\.0\.kt$""")
            }

            testClass<AbstractMoveTest> {
                model("refactoring/move", extension = "test", singleClass = true)
            }

            testClass<AbstractCopyTest> {
                model("refactoring/copy", extension = "test", singleClass = true)
            }

            testClass<AbstractMultiModuleMoveTest> {
                model("refactoring/moveMultiModule", extension = "test", singleClass = true)
            }

            testClass<AbstractMultiModuleCopyTest> {
                model("refactoring/copyMultiModule", extension = "test", singleClass = true)
            }

            testClass<AbstractMultiModuleSafeDeleteTest> {
                model("refactoring/safeDeleteMultiModule", extension = "test", singleClass = true)
            }

            testClass<AbstractMultiFileIntentionTest> {
                model("multiFileIntentions", extension = "test", singleClass = true, filenameStartsLowerCase = true)
            }

            testClass<AbstractMultiFileLocalInspectionTest> {
                model("multiFileLocalInspections", extension = "test", singleClass = true, filenameStartsLowerCase = true)
            }

            testClass<AbstractMultiFileInspectionTest> {
                model("multiFileInspections", extension = "test", singleClass = true)
            }

            testClass<AbstractFormatterTest> {
                model("formatter", pattern = """^([^\.]+)\.after\.kt.*$""")
                model(
                    "formatter/trailingComma", pattern = """^([^\.]+)\.call\.after\.kt.*$""",
                    testMethod = "doTestCallSite", testClassName = "FormatterCallSite"
                )
                model(
                    "formatter", pattern = """^([^\.]+)\.after\.inv\.kt.*$""",
                    testMethod = "doTestInverted", testClassName = "FormatterInverted"
                )
                model(
                    "formatter/trailingComma", pattern = """^([^\.]+)\.call\.after\.inv\.kt.*$""",
                    testMethod = "doTestInvertedCallSite", testClassName = "FormatterInvertedCallSite"
                )
            }

            testClass<AbstractTypingIndentationTestBase> {
                model(
                    "indentationOnNewline", pattern = """^([^\.]+)\.after\.kt.*$""", testMethod = "doNewlineTest",
                    testClassName = "DirectSettings"
                )
                model(
                    "indentationOnNewline", pattern = """^([^\.]+)\.after\.inv\.kt.*$""", testMethod = "doNewlineTestWithInvert",
                    testClassName = "InvertedSettings"
                )
            }

            testClass<AbstractDiagnosticMessageTest> {
                model("diagnosticMessage", recursive = false)
            }

            testClass<AbstractDiagnosticMessageJsTest> {
                model("diagnosticMessage/js", recursive = false, targetBackend = TargetBackend.JS)
            }

            testClass<AbstractRenameTest> {
                model("refactoring/rename", extension = "test", singleClass = true)
            }

            testClass<AbstractMultiModuleRenameTest> {
                model("refactoring/renameMultiModule", extension = "test", singleClass = true)
            }

            testClass<AbstractOutOfBlockModificationTest> {
                model("codeInsight/outOfBlock", pattern = KT_OR_KTS)
            }

            testClass<AbstractChangeLocalityDetectorTest> {
                model("codeInsight/changeLocality", pattern = KT_OR_KTS)
            }

            testClass<AbstractDataFlowValueRenderingTest> {
                model("dataFlowValueRendering")
            }

            testClass<AbstractJavaToKotlinCopyPasteConversionTest> {
                model("copyPaste/conversion", pattern = """^([^\.]+)\.java$""")
            }

            testClass<AbstractTextJavaToKotlinCopyPasteConversionTest> {
                model("copyPaste/plainTextConversion", pattern = """^([^\.]+)\.txt$""")
            }

            testClass<AbstractLiteralTextToKotlinCopyPasteTest> {
                model("copyPaste/plainTextLiteral", pattern = """^([^\.]+)\.txt$""")
            }

            testClass<AbstractLiteralKotlinToKotlinCopyPasteTest> {
                model("copyPaste/literal", pattern = """^([^\.]+)\.kt$""")
            }

            testClass<AbstractInsertImportOnPasteTest> {
                model(
                    "copyPaste/imports",
                    pattern = KT_WITHOUT_DOTS_IN_NAME,
                    testMethod = "doTestCopy",
                    testClassName = "Copy",
                    recursive = false
                )
                model(
                    "copyPaste/imports",
                    pattern = KT_WITHOUT_DOTS_IN_NAME,
                    testMethod = "doTestCut",
                    testClassName = "Cut",
                    recursive = false
                )
            }

            testClass<AbstractMoveOnCutPasteTest> {
                model("copyPaste/moveDeclarations", pattern = KT_WITHOUT_DOTS_IN_NAME, testMethod = "doTest")
            }

            testClass<AbstractUpdateKotlinCopyrightTest> {
                model("copyright", pattern = KT_OR_KTS, testMethod = "doTest")
            }

            testClass<AbstractHighlightExitPointsTest> {
                model("exitPoints")
            }

            testClass<AbstractLineMarkersTest> {
                model("codeInsight/lineMarker")
            }

            testClass<AbstractLineMarkersTestInLibrarySources> {
                model("codeInsightInLibrary/lineMarker", testMethod = "doTestWithLibrary")
            }

            testClass<AbstractMultiModuleLineMarkerTest> {
                model("multiModuleLineMarker", extension = null, recursive = false)
            }

            testClass<AbstractShortenRefsTest> {
                model("shortenRefs", pattern = KT_WITHOUT_DOTS_IN_NAME)
            }
            testClass<AbstractAddImportTest> {
                model("addImport", pattern = KT_WITHOUT_DOTS_IN_NAME)
            }

            testClass<AbstractAddImportAliasTest> {
                model("addImportAlias", pattern = KT_WITHOUT_DOTS_IN_NAME)
            }

            testClass<AbstractSmartSelectionTest> {
                model("smartSelection", testMethod = "doTestSmartSelection", pattern = KT_WITHOUT_DOTS_IN_NAME)
            }

            testClass<AbstractKotlinFileStructureTest> {
                model("structureView/fileStructure", pattern = KT_WITHOUT_DOTS_IN_NAME)
            }

            testClass<AbstractExpressionSelectionTest> {
                model("expressionSelection", testMethod = "doTestExpressionSelection", pattern = KT_WITHOUT_DOTS_IN_NAME)
            }

            testClass<AbstractCommonDecompiledTextTest> {
                model("decompiler/decompiledText", pattern = """^([^\.]+)$""")
            }

            testClass<AbstractJvmDecompiledTextTest> {
                model("decompiler/decompiledTextJvm", pattern = """^([^\.]+)$""")
            }

            testClass<AbstractCommonDecompiledTextFromJsMetadataTest> {
                model("decompiler/decompiledText", pattern = """^([^\.]+)$""", targetBackend = TargetBackend.JS)
            }

            testClass<AbstractJsDecompiledTextFromJsMetadataTest> {
                model("decompiler/decompiledTextJs", pattern = """^([^\.]+)$""", targetBackend = TargetBackend.JS)
            }

            testClass<AbstractClsStubBuilderTest> {
                model("decompiler/stubBuilder", extension = null, recursive = false)
            }

            testClass<AbstractJvmOptimizeImportsTest> {
                model("editor/optimizeImports/jvm", pattern = KT_OR_KTS_WITHOUT_DOTS_IN_NAME)
                model("editor/optimizeImports/common", pattern = KT_WITHOUT_DOTS_IN_NAME)
            }
            testClass<AbstractJsOptimizeImportsTest> {
                model("editor/optimizeImports/js", pattern = KT_WITHOUT_DOTS_IN_NAME)
                model("editor/optimizeImports/common", pattern = KT_WITHOUT_DOTS_IN_NAME)
            }

            testClass<AbstractKotlinExceptionFilterTest> {
                model("debugger/exceptionFilter", pattern = """^([^\.]+)$""", recursive = false)
            }

            testClass<AbstractStubBuilderTest> {
                model("stubs", extension = "kt")
            }

            testClass<AbstractMultiFileHighlightingTest> {
                model("multiFileHighlighting", recursive = false)
            }

            testClass<AbstractMultiPlatformHighlightingTest> {
                model("multiModuleHighlighting/multiplatform/", recursive = false, extension = null)
            }

            testClass<AbstractMultiplatformAnalysisTest> {
                model("multiplatform", recursive = false, extension = null)
            }

            testClass<AbstractQuickFixMultiModuleTest> {
                model("multiModuleQuickFix", extension = null, deep = 1)
            }

            testClass<AbstractKotlinGotoImplementationMultiModuleTest> {
                model("navigation/implementations/multiModule", recursive = false, extension = null)
            }

            testClass<AbstractKotlinGotoRelatedSymbolMultiModuleTest> {
                model("navigation/relatedSymbols/multiModule", recursive = false, extension = null)
            }

            testClass<AbstractKotlinGotoSuperMultiModuleTest> {
                model("navigation/gotoSuper/multiModule", recursive = false, extension = null)
            }

            testClass<AbstractExtractionTest> {
                model("refactoring/introduceVariable", pattern = KT_OR_KTS, testMethod = "doIntroduceVariableTest")
                model("refactoring/extractFunction", pattern = KT_OR_KTS, testMethod = "doExtractFunctionTest")
                model("refactoring/introduceProperty", pattern = KT_OR_KTS, testMethod = "doIntroducePropertyTest")
                model("refactoring/introduceParameter", pattern = KT_OR_KTS, testMethod = "doIntroduceSimpleParameterTest")
                model("refactoring/introduceLambdaParameter", pattern = KT_OR_KTS, testMethod = "doIntroduceLambdaParameterTest")
                model("refactoring/introduceJavaParameter", extension = "java", testMethod = "doIntroduceJavaParameterTest")
                model("refactoring/introduceTypeParameter", pattern = KT_OR_KTS, testMethod = "doIntroduceTypeParameterTest")
                model("refactoring/introduceTypeAlias", pattern = KT_OR_KTS, testMethod = "doIntroduceTypeAliasTest")
                model("refactoring/extractSuperclass", pattern = KT_OR_KTS_WITHOUT_DOTS_IN_NAME, testMethod = "doExtractSuperclassTest")
                model("refactoring/extractInterface", pattern = KT_OR_KTS_WITHOUT_DOTS_IN_NAME, testMethod = "doExtractInterfaceTest")
            }

            testClass<AbstractPullUpTest> {
                model("refactoring/pullUp/k2k", extension = "kt", singleClass = true, testClassName = "K2K", testMethod = "doKotlinTest")
                model("refactoring/pullUp/k2j", extension = "kt", singleClass = true, testClassName = "K2J", testMethod = "doKotlinTest")
                model("refactoring/pullUp/j2k", extension = "java", singleClass = true, testClassName = "J2K", testMethod = "doJavaTest")
            }

            testClass<AbstractPushDownTest> {
                model("refactoring/pushDown/k2k", extension = "kt", singleClass = true, testClassName = "K2K", testMethod = "doKotlinTest")
                model("refactoring/pushDown/k2j", extension = "kt", singleClass = true, testClassName = "K2J", testMethod = "doKotlinTest")
                model("refactoring/pushDown/j2k", extension = "java", singleClass = true, testClassName = "J2K", testMethod = "doJavaTest")
            }

            testClass<AbstractKotlinCoverageOutputFilesTest> {
                model("coverage/outputFiles")
            }

            testClass<AbstractBytecodeToolWindowTest> {
                model("internal/toolWindow", recursive = false, extension = null)
            }

            testClass<AbstractReferenceResolveTest>("org.jetbrains.kotlin.idea.kdoc.KdocResolveTestGenerated") {
                model("kdoc/resolve")
            }

            testClass<AbstractKDocHighlightingTest> {
                model("kdoc/highlighting")
            }

            testClass<AbstractKDocTypingTest> {
                model("kdoc/typing")
            }

            testClass<AbstractGenerateTestSupportMethodActionTest> {
                model("codeInsight/generate/testFrameworkSupport")
            }

            testClass<AbstractGenerateHashCodeAndEqualsActionTest> {
                model("codeInsight/generate/equalsWithHashCode")
            }

            testClass<AbstractCodeInsightActionTest> {
                model("codeInsight/generate/secondaryConstructors")
            }

            testClass<AbstractGenerateToStringActionTest> {
                model("codeInsight/generate/toString")
            }

            testClass<AbstractIdeReplCompletionTest> {
                model("repl/completion")
            }

            testClass<AbstractPostfixTemplateProviderTest> {
                model("codeInsight/postfix")
            }

            testClass<AbstractKotlinCodeVisionProviderTest> {
                model("codeInsight/codeVision")
            }

            testClass<AbstractKotlinReferenceTypeHintsProviderTest> {
                model("codeInsight/hints/types")
            }

            testClass<AbstractScriptConfigurationHighlightingTest> {
                model("script/definition/highlighting", extension = null, recursive = false)
                model("script/definition/complex", extension = null, recursive = false, testMethod = "doComplexTest")
            }

            testClass<AbstractScriptConfigurationNavigationTest> {
                model("script/definition/navigation", extension = null, recursive = false)
            }

            testClass<AbstractScriptConfigurationCompletionTest> {
                model("script/definition/completion", extension = null, recursive = false)
            }

            testClass<AbstractScriptConfigurationInsertImportOnPasteTest> {
                model(
                    "script/definition/imports",
                    testMethod = "doTestCopy",
                    testClassName = "Copy",
                    extension = null,
                    recursive = false
                )
                model(
                    "script/definition/imports",
                    testMethod = "doTestCut",
                    testClassName = "Cut",
                    extension = null,
                    recursive = false
                )
            }

            testClass<AbstractScriptDefinitionsOrderTest> {
                model("script/definition/order", extension = null, recursive = false)
            }

            testClass<AbstractNameSuggestionProviderTest> {
                model("refactoring/nameSuggestionProvider")
            }

            testClass<AbstractSlicerTreeTest> {
                model("slicer", excludeDirs = listOf("mpp"))
            }

            testClass<AbstractSlicerLeafGroupingTest> {
                model("slicer/inflow", singleClass = true)
            }

            testClass<AbstractSlicerNullnessGroupingTest> {
                model("slicer/inflow", singleClass = true)
            }

            testClass<AbstractSlicerMultiplatformTest> {
                model("slicer/mpp", recursive = false, extension = null)
            }

            testClass<AbstractKotlinNavBarTest> {
                model("navigationToolbar", recursive = false)
            }
        }

        testGroup("idea/idea-frontend-fir/tests", "idea/idea-frontend-fir/testData") {
            testClass<AbstractKtDeclarationAndFirDeclarationEqualityChecker> {
                model("ktDeclarationAndFirDeclarationEqualityChecker")
            }

            testClass<AbstractResolveCallTest> {
                model("analysisSession/resolveCall")
            }

            testClass<AbstractMemberScopeByFqNameTest> {
                model("memberScopeByFqName")
            }

            testClass<AbstractFileScopeTest> {
                model("fileScopeTest", extension = "kt")
            }

            testClass<AbstractSymbolByPsiTest> {
                model("symbols/symbolByPsi")
            }

            testClass<AbstractSymbolByFqNameTest> {
                model("symbols/symbolByFqName")
            }

            testClass<AbstractSymbolByReferenceTest> {
                model("symbols/symbolByReference")
            }

            testClass<AbstractMemoryLeakInSymbolsTest> {
                model("symbolMemoryLeak")
            }

            testClass<AbstractReturnExpressionTargetTest> {
                model("components/returnExpressionTarget")
            }

            testClass<AbstractExpectedExpressionTypeTest> {
                model("components/expectedExpressionType")
            }

            testClass<AbstractOverriddenDeclarationProviderTest> {
                model("components/overridenDeclarations")
            }

            testClass<AbstractHLExpressionTypeTest> {
                model("components/expressionType")
            }
        }

        testGroup("idea/idea-frontend-fir/idea-fir-low-level-api/tests", "idea/testData") {
            testClass<AbstractFirMultiModuleResolveTest> {
                model("fir/multiModule", recursive = false, extension = null)
            }

            testClass<AbstractFirLazyResolveTest> {
                model("fir/lazyResolve", extension = "test", singleClass = true, filenameStartsLowerCase = true)
            }
        }

        testGroup("idea/idea-frontend-fir/idea-fir-low-level-api/tests", "compiler/fir/raw-fir/psi2fir/testData") {
            testClass<AbstractFirLazyBodiesCalculatorTest> {
                model("rawBuilder", testMethod = "doTest")
            }
        }

        testGroup("idea/idea-frontend-fir/idea-fir-low-level-api/tests", "idea/idea-frontend-fir/idea-fir-low-level-api/testdata") {
            testClass<AbstractFirMultiModuleLazyResolveTest> {
                model("multiModuleLazyResolve", recursive = false, extension = null)
            }
            testClass<AbstractFirSealedInheritorsTest> {
                model("resolveSealed", recursive = false, extension = null)
            }
            testClass<AbstractFirLazyDeclarationResolveTest> {
                model("lazyResolve")
            }
            testClass<AbstractProjectWideOutOfBlockKotlinModificationTrackerTest> {
                model("outOfBlockProjectWide")
            }
            testClass<AbstractFileStructureAndOutOfBlockModificationTrackerConsistencyTest> {
                model("outOfBlockProjectWide")
            }
            testClass<AbstractFileStructureTest> {
                model("fileStructure")
            }
            testClass<AbstractDiagnosticTraversalCounterTest> {
                model("diagnosticTraversalCounter")
            }
            testClass<AbstractSessionsInvalidationTest> {
                model("sessionInvalidation", recursive = false, extension = null)
            }
            testClass<AbstractInnerDeclarationsResolvePhaseTest> {
                model("innerDeclarationsResolve")
            }
        }

        testGroup("idea/idea-fir/tests", "idea") {
            testClass<AbstractFirHighlightingTest> {
                model("testData/highlighter")
                model("idea-fir/testData/highlighterFir", pattern = KT_WITHOUT_DOTS_IN_NAME)
            }
        }

        testGroup("idea/idea-fir-performance-tests/tests", "idea") {
            testClass<AbstractFirHighlightingPerformanceTest> {
                model("testData/highlighter")
            }
        }

        testGroup("idea/idea-fir-performance-tests/tests", "idea/idea-completion/testData") {
            testClass<AbstractHighLevelPerformanceBasicCompletionHandlerTest> {
                model("handlers/basic", testMethod = "doPerfTest", pattern = KT_WITHOUT_DOTS_IN_NAME)
            }
        }

        testGroup("idea/idea-fir/tests", "idea/testData") {
            testClass<AbstractFirReferenceResolveTest> {
                model("resolve/references", pattern = KT_WITHOUT_DOTS_IN_NAME)
            }

            testClass<AbstractFirKotlinHighlightingPassTest> {
                model("checker", recursive = false)
                model("checker/regression")
                model("checker/recovery")
                model("checker/rendering")
                model("checker/infos")
                model("checker/diagnosticsMessage")
            }


            testClass<AbstractHighLevelQuickFixTest> {
                val pattern = "^([\\w\\-_]+)\\.kt$"
                model("quickfix/abstract", pattern = pattern, filenameStartsLowerCase = true)
                model("quickfix/expressions", pattern = pattern, filenameStartsLowerCase = true)
                model("quickfix/lateinit", pattern = pattern, filenameStartsLowerCase = true)
                model("quickfix/modifiers", pattern = pattern, filenameStartsLowerCase = true, recursive = false)
                model("quickfix/override/typeMismatchOnOverride", pattern = pattern, filenameStartsLowerCase = true, recursive = false)
                model("quickfix/replaceWithSafeCall", pattern = pattern, filenameStartsLowerCase = true)
                model("quickfix/variables/changeMutability", pattern = pattern, filenameStartsLowerCase = true)
            }

            testClass<AbstractHLInspectionTest> {
                val pattern = "^(inspections\\.test)$"
                model("inspections/redundantUnitReturnType", pattern = pattern, singleClass = true)
            }


            testClass<AbstractHLIntentionTest> {
                val pattern = "^([\\w\\-_]+)\\.(kt|kts)$"
                model("intentions/specifyTypeExplicitly", pattern = pattern)
            }

            testClass<AbstractFirShortenRefsTest> {
                model("shortenRefsFir", pattern = KT_WITHOUT_DOTS_IN_NAME, testMethod = "doTestWithMuting")
            }
        }

        testGroup("idea/idea-fir/tests", "idea") {
            testClass<AbstractHLLocalInspectionTest> {
                val pattern = "^([\\w\\-_]+)\\.(kt|kts)$"
                model("testData/inspectionsLocal/redundantVisibilityModifier", pattern = pattern)
                model("idea-fir/testData/inspectionsLocal", pattern = pattern)
            }
        }

        testGroup("idea/idea-fir/tests", "idea/idea-completion/testData") {
            testClass<AbstractHighLevelJvmBasicCompletionTest> {
                model("basic/common")
                model("basic/java")
            }

            testClass<AbstractHighLevelBasicCompletionHandlerTest> {
                model("handlers/basic", pattern = KT_WITHOUT_DOTS_IN_NAME)
            }

            testClass<AbstractHighLevelWeigherTest> {
                model("weighers/basic", pattern = KT_OR_KTS_WITHOUT_DOTS_IN_NAME)
            }

            testClass<AbstractHighLevelMultiFileJvmBasicCompletionTest> {
                model("basic/multifile", extension = null, recursive = false)
            }
        }

        testGroup("idea/idea-fir/tests", "idea/testData/findUsages") {

            testClass<AbstractFindUsagesFirTest> {
                model("kotlin", pattern = """^(.+)\.0\.(kt|kts)$""")
                model("java", pattern = """^(.+)\.0\.java$""")
                model("propertyFiles", pattern = """^(.+)\.0\.properties$""")
            }

            testClass<AbstractFindUsagesWithDisableComponentSearchFirTest> {
                model("kotlin/conventions/components", pattern = """^(.+)\.0\.(kt|kts)$""")
            }

            testClass<AbstractKotlinFindUsagesWithLibraryFirTest> {
                model("libraryUsages", pattern = """^(.+)\.0\.kt$""")
            }

            testClass<AbstractKotlinFindUsagesWithStdlibFirTest> {
                model("stdlibUsages", pattern = """^(.+)\.0\.kt$""")
            }
        }

        testGroup("idea/scripting-support/test", "idea/scripting-support/testData") {
            testClass<AbstractScratchRunActionTest> {
                model(
                    "scratch",
                    extension = "kts",
                    testMethod = "doScratchCompilingTest",
                    testClassName = "ScratchCompiling",
                    recursive = false
                )
                model("scratch", extension = "kts", testMethod = "doScratchReplTest", testClassName = "ScratchRepl", recursive = false)
                model(
                    "scratch/multiFile",
                    extension = null,
                    testMethod = "doScratchMultiFileTest",
                    testClassName = "ScratchMultiFile",
                    recursive = false
                )

                model(
                    "worksheet",
                    extension = "ws.kts",
                    testMethod = "doWorksheetCompilingTest",
                    testClassName = "WorksheetCompiling",
                    recursive = false
                )
                model(
                    "worksheet",
                    extension = "ws.kts",
                    testMethod = "doWorksheetReplTest",
                    testClassName = "WorksheetRepl",
                    recursive = false
                )
                model(
                    "worksheet/multiFile",
                    extension = null,
                    testMethod = "doWorksheetMultiFileTest",
                    testClassName = "WorksheetMultiFile",
                    recursive = false
                )

                model(
                    "scratch/rightPanelOutput",
                    extension = "kts",
                    testMethod = "doRightPreviewPanelOutputTest",
                    testClassName = "ScratchRightPanelOutput",
                    recursive = false
                )
            }

            testClass<AbstractScratchLineMarkersTest> {
                model("scratch/lineMarker", testMethod = "doScratchTest", pattern = KT_OR_KTS)
            }

            testClass<AbstractScriptTemplatesFromDependenciesTest> {
                model("script/templatesFromDependencies", extension = null, recursive = false)
            }
        }

        testGroup("idea/idea-maven/test", "idea/idea-maven/testData") {
            testClass<AbstractMavenConfigureProjectByChangingFileTest> {
                model("configurator/jvm", extension = null, recursive = false, testMethod = "doTestWithMaven")
                model("configurator/js", extension = null, recursive = false, testMethod = "doTestWithJSMaven")
            }

            testClass<AbstractKotlinMavenInspectionTest> {
                model("maven-inspections", pattern = "^([\\w\\-]+).xml$", singleClass = true)
            }
        }

        testGroup("idea/idea-gradle/tests", "idea/testData") {
            testClass<AbstractGradleConfigureProjectByChangingFileTest> {
                model("configuration/gradle", extension = null, recursive = false, testMethod = "doTestGradle")
                model("configuration/gsk", extension = null, recursive = false, testMethod = "doTestGradle")
            }
        }

        testGroup("idea/tests", "compiler/testData") {
            testClass<AbstractResolveByStubTest> {
                model("loadJava/compiledKotlin")
            }

            testClass<AbstractLoadJavaClsStubTest> {
                model("loadJava/compiledKotlin", testMethod = "doTestCompiledKotlin")
            }

            testClass<AbstractIdeLightClassTest> {
                model("asJava/lightClasses", excludeDirs = listOf("delegation", "script"), pattern = KT_WITHOUT_DOTS_IN_NAME)
            }

            testClass<AbstractIdeLightClassForScriptTest> {
                model("asJava/script/ide", pattern = KT_OR_KTS_WITHOUT_DOTS_IN_NAME)
            }

            testClass<AbstractUltraLightClassSanityTest> {
                model("asJava/lightClasses", pattern = KT_OR_KTS)
            }
            testClass<AbstractUltraLightClassLoadingTest> {
                model("asJava/ultraLightClasses", pattern = KT_OR_KTS)
            }
            testClass<AbstractUltraLightScriptLoadingTest> {
                model("asJava/ultraLightScripts", pattern = KT_OR_KTS)
            }
            testClass<AbstractUltraLightFacadeClassTest> {
                model("asJava/ultraLightFacades", pattern = KT_OR_KTS)
            }

            testClass<AbstractIdeCompiledLightClassTest> {
                model(
                    "asJava/lightClasses",
                    excludeDirs = listOf("local", "compilationErrors", "ideRegression"),
                    pattern = KT_OR_KTS_WITHOUT_DOTS_IN_NAME
                )
            }
        }

        testGroup("idea/idea-fir/tests", "compiler/testData") {
            testClass<AbstractFirLightClassTest> {
                model("asJava/lightClasses", excludeDirs = listOf("delegation", "script"), pattern = KT_WITHOUT_DOTS_IN_NAME)
            }

            testClass<AbstractFirClassLoadingTest> {
                model("asJava/ultraLightClasses", pattern = KT_OR_KTS)
            }

            testClass<AbstractFirLightFacadeClassTest> {
                model("asJava/ultraLightFacades", pattern = KT_OR_KTS)
            }
        }

        testGroup("idea/idea-completion/tests", "idea/idea-completion/testData") {
            testClass<AbstractCompiledKotlinInJavaCompletionTest> {
                model("injava", extension = "java", recursive = false)
            }

            testClass<AbstractKotlinSourceInJavaCompletionTest> {
                model("injava", extension = "java", recursive = false)
            }

            testClass<AbstractKotlinStdLibInJavaCompletionTest> {
                model("injava/stdlib", extension = "java", recursive = false)
            }

            testClass<AbstractBasicCompletionWeigherTest> {
                model("weighers/basic", pattern = KT_OR_KTS_WITHOUT_DOTS_IN_NAME)
            }

            testClass<AbstractSmartCompletionWeigherTest> {
                model("weighers/smart", pattern = KT_WITHOUT_DOTS_IN_NAME)
            }

            testClass<AbstractJSBasicCompletionTest> {
                model("basic/common")
                model("basic/js")
            }

            testClass<AbstractJvmBasicCompletionTest> {
                model("basic/common")
                model("basic/java")
            }

            testClass<AbstractJvmSmartCompletionTest> {
                model("smart")
            }

            testClass<AbstractKeywordCompletionTest> {
                model("keywords", recursive = false)
            }

            testClass<AbstractJvmWithLibBasicCompletionTest> {
                model("basic/withLib", recursive = false)
            }

            testClass<AbstractBasicCompletionHandlerTest> {
                model("handlers/basic", pattern = KT_WITHOUT_DOTS_IN_NAME)
            }

            testClass<AbstractSmartCompletionHandlerTest> {
                model("handlers/smart")
            }

            testClass<AbstractKeywordCompletionHandlerTest> {
                model("handlers/keywords")
            }

            testClass<AbstractCompletionCharFilterTest> {
                model("handlers/charFilter", pattern = KT_WITHOUT_DOTS_IN_NAME)
            }

            testClass<AbstractMultiFileJvmBasicCompletionTest> {
                model("basic/multifile", extension = null, recursive = false)
            }

            testClass<AbstractMultiFileSmartCompletionTest> {
                model("smartMultiFile", extension = null, recursive = false)
            }

            testClass<AbstractJvmBasicCompletionTest>("KDocCompletionTestGenerated") {
                model("kdoc")
            }

            testClass<AbstractJava8BasicCompletionTest> {
                model("basic/java8")
            }

            testClass<AbstractCompletionIncrementalResolveTest> {
                model("incrementalResolve")
            }

            testClass<AbstractMultiPlatformCompletionTest> {
                model("multiPlatform", recursive = false, extension = null)
            }
        }

        testGroup(
            "libraries/tools/new-project-wizard/new-project-wizard-cli/tests",
            "libraries/tools/new-project-wizard/new-project-wizard-cli/testData"
        ) {
            testClass<AbstractYamlBuildFileGenerationTest> {
                model("buildFileGeneration", recursive = false, extension = null)
            }
            testClass<AbstractProjectTemplateBuildFileGenerationTest> {
                model("projectTemplatesBuildFileGeneration", recursive = false, extension = null)
            }
        }

        testGroup(
            "idea/idea-new-project-wizard/tests",
            "libraries/tools/new-project-wizard/new-project-wizard-cli/testData"
        ) {
            fun TestGroup.TestClass.allBuildSystemTests(relativeRootPath: String) {
                for (testClass in listOf("GradleKts", "GradleGroovy", "Maven")) {
                    model(
                        relativeRootPath,
                        recursive = false,
                        extension = null,
                        testMethod = "doTest${testClass}",
                        testClassName = testClass
                    )
                }
            }
            testClass<AbstractYamlNewWizardProjectImportTest> {
                allBuildSystemTests("buildFileGeneration")
            }
            testClass<AbstractProjectTemplateNewWizardProjectImportTest> {
                allBuildSystemTests("projectTemplatesBuildFileGeneration")
            }
        }

        //TODO: move these tests into idea-completion module
        testGroup("idea/tests", "idea/idea-completion/testData") {
            testClass<AbstractCodeFragmentCompletionHandlerTest> {
                model("handlers/runtimeCast")
            }

            testClass<AbstractCodeFragmentCompletionTest> {
                model("basic/codeFragments", extension = "kt")
            }
        }

        testGroup("j2k/tests", "j2k/testData") {
            testClass<AbstractJavaToKotlinConverterSingleFileTest> {
                model("fileOrElement", extension = "java")
            }
        }
        testGroup("j2k/tests", "j2k/testData") {
            testClass<AbstractJavaToKotlinConverterMultiFileTest> {
                model("multiFile", extension = null, recursive = false)
            }
        }
        testGroup("j2k/tests", "j2k/testData") {
            testClass<AbstractJavaToKotlinConverterForWebDemoTest> {
                model("fileOrElement", extension = "java")
            }
        }

        testGroup("nj2k/tests", "nj2k/testData") {
            testClass<AbstractNewJavaToKotlinConverterSingleFileTest> {
                model("newJ2k", pattern = """^([^\.]+)\.java$""")
            }
            testClass<AbstractCommonConstraintCollectorTest> {
                model("inference/common")
            }
            testClass<AbstractNullabilityInferenceTest> {
                model("inference/nullability")
            }
            testClass<AbstractMutabilityInferenceTest> {
                model("inference/mutability")
            }
            testClass<AbstractNewJavaToKotlinCopyPasteConversionTest> {
                model("copyPaste", pattern = """^([^\.]+)\.java$""")
            }
            testClass<AbstractTextNewJavaToKotlinCopyPasteConversionTest> {
                model("copyPastePlainText", pattern = """^([^\.]+)\.txt$""")
            }
            testClass<AbstractNewJavaToKotlinConverterMultiFileTest> {
                model("multiFile", extension = null, recursive = false)
            }
        }

        testGroup("jps-plugin/jps-tests/test", "jps-plugin/testData") {
            testClass<AbstractIncrementalJvmJpsTest> {
                model("incremental/multiModule/common", extension = null, excludeParentDirs = true, targetBackend = TargetBackend.JVM_IR)
                model("incremental/multiModule/jvm", extension = null, excludeParentDirs = true, targetBackend = TargetBackend.JVM_IR)
                model(
                    "incremental/multiModule/multiplatform/custom", extension = null, excludeParentDirs = true,
                    targetBackend = TargetBackend.JVM_IR
                )
                model("incremental/pureKotlin", extension = null, recursive = false, targetBackend = TargetBackend.JVM_IR)
                model("incremental/withJava", extension = null, excludeParentDirs = true, targetBackend = TargetBackend.JVM_IR)
                model("incremental/inlineFunCallSite", extension = null, excludeParentDirs = true, targetBackend = TargetBackend.JVM_IR)
                model(
                    "incremental/classHierarchyAffected", extension = null, excludeParentDirs = true, targetBackend = TargetBackend.JVM_IR
                )
            }

            actualizeMppJpsIncTestCaseDirs(testDataRoot, "incremental/multiModule/multiplatform/withGeneratedContent")

            testClass<AbstractIncrementalJsJpsTest> {
                model("incremental/multiModule/common", extension = null, excludeParentDirs = true)
            }

            testClass<AbstractMultiplatformJpsTestWithGeneratedContent> {
                model(
                    "incremental/multiModule/multiplatform/withGeneratedContent", extension = null, excludeParentDirs = true,
                    testClassName = "MultiplatformMultiModule", recursive = true
                )
            }

            testClass<AbstractJvmLookupTrackerTest> {
                model("incremental/lookupTracker/jvm", extension = null, recursive = false)
            }
            testClass<AbstractJsLookupTrackerTest> {
                model("incremental/lookupTracker/js", extension = null, recursive = false)
            }
            testClass<AbstractJsKlibLookupTrackerTest> {
                // todo: investigate why lookups are different from non-klib js
                model("incremental/lookupTracker/jsKlib", extension = null, recursive = false)
            }

            testClass<AbstractIncrementalLazyCachesTest> {
                model("incremental/lazyKotlinCaches", extension = null, excludeParentDirs = true)
                model("incremental/changeIncrementalOption", extension = null, excludeParentDirs = true)
            }

            testClass<AbstractIncrementalCacheVersionChangedTest> {
                model("incremental/cacheVersionChanged", extension = null, excludeParentDirs = true)
            }

            testClass<AbstractDataContainerVersionChangedTest> {
                model("incremental/cacheVersionChanged", extension = null, excludeParentDirs = true)
            }
        }

        testGroup("jps-plugin/jps-tests/test", "jps-plugin/testData") {
            fun TestGroup.TestClass.commonProtoComparisonTests() {
                model("comparison/classSignatureChange", extension = null, excludeParentDirs = true)
                model("comparison/classPrivateOnlyChange", extension = null, excludeParentDirs = true)
                model("comparison/classMembersOnlyChanged", extension = null, excludeParentDirs = true)
                model("comparison/packageMembers", extension = null, excludeParentDirs = true)
                model("comparison/unchanged", extension = null, excludeParentDirs = true)
            }

            testClass<AbstractJvmProtoComparisonTest> {
                commonProtoComparisonTests()
                model("comparison/jvmOnly", extension = null, excludeParentDirs = true)
            }

            testClass<AbstractJsProtoComparisonTest> {
                commonProtoComparisonTests()
                model("comparison/jsOnly", extension = null, excludeParentDirs = true)
            }
        }

        testGroup("compiler/incremental-compilation-impl/test", "jps-plugin/testData") {
            fun incrementalJvmTestData(targetBackend: TargetBackend): TestGroup.TestClass.() -> Unit = {
                model("incremental/pureKotlin", extension = null, recursive = false, targetBackend = targetBackend)
                model("incremental/classHierarchyAffected", extension = null, recursive = false, targetBackend = targetBackend)
                model("incremental/inlineFunCallSite", extension = null, excludeParentDirs = true, targetBackend = targetBackend)
                model("incremental/withJava", extension = null, excludeParentDirs = true, targetBackend = targetBackend)
                model("incremental/incrementalJvmCompilerOnly", extension = null, excludeParentDirs = true, targetBackend = targetBackend)
            }
            testClass<AbstractIncrementalJvmCompilerRunnerTest>(init = incrementalJvmTestData(TargetBackend.JVM_IR))
            testClass<AbstractIncrementalJvmOldBackendCompilerRunnerTest>(init = incrementalJvmTestData(TargetBackend.JVM))

            testClass<AbstractIncrementalJsCompilerRunnerTest> {
                model("incremental/pureKotlin", extension = null, recursive = false)
                model("incremental/classHierarchyAffected", extension = null, recursive = false)
                model("incremental/js", extension = null, excludeParentDirs = true)
            }

            testClass<AbstractIncrementalJsKlibCompilerRunnerTest>() {
                // IC of sealed interfaces are not supported in JS
                model("incremental/pureKotlin", extension = null, recursive = false, excludedPattern = "^sealed.*")
                model("incremental/classHierarchyAffected", extension = null, recursive = false)
                model("incremental/js", extension = null, excludeParentDirs = true)
            }

            testClass<AbstractIncrementalMultiModuleJsCompilerRunnerTest> {
                model("incremental/multiModule/common", extension = null, excludeParentDirs = true)
            }

            testClass<AbstractIncrementalMultiModuleJsKlibCompilerRunnerTest> {
                model("incremental/multiModule/common", extension = null, excludeParentDirs = true)
            }

            testClass<AbstractIncrementalJsCompilerRunnerWithMetadataOnlyTest> {
                model("incremental/pureKotlin", extension = null, recursive = false)
                model("incremental/classHierarchyAffected", extension = null, recursive = false)
                model("incremental/js", extension = null, excludeParentDirs = true)
            }

            testClass<AbstractIncrementalJsKlibCompilerWithScopeExpansionRunnerTest> {
                // IC of sealed interfaces are not supported in JS
                model("incremental/pureKotlin", extension = null, recursive = false, excludedPattern = "^sealed.*")
                model("incremental/classHierarchyAffected", extension = null, recursive = false)
                model("incremental/js", extension = null, excludeParentDirs = true)
                model("incremental/scopeExpansion", extension = null, excludeParentDirs = true)
            }

            testClass<AbstractIncrementalJsCompilerRunnerWithFriendModulesDisabledTest> {
                model("incremental/js/friendsModuleDisabled", extension = null, recursive = false)
            }

            testClass<AbstractIncrementalMultiplatformJvmCompilerRunnerTest> {
                model("incremental/mpp/allPlatforms", extension = null, excludeParentDirs = true)
                model("incremental/mpp/jvmOnly", extension = null, excludeParentDirs = true)
            }
            testClass<AbstractIncrementalMultiplatformJsCompilerRunnerTest> {
                model("incremental/mpp/allPlatforms", extension = null, excludeParentDirs = true)
            }
        }

        testGroup(
            "plugins/android-extensions/android-extensions-compiler/test",
            "plugins/android-extensions/android-extensions-compiler/testData"
        ) {
            testClass<AbstractAndroidSyntheticPropertyDescriptorTest> {
                model("descriptors", recursive = false, extension = null)
            }

            testClass<AbstractAndroidBoxTest> {
                model("codegen/android", recursive = false, extension = null, testMethod = "doCompileAgainstAndroidSdkTest")
                model("codegen/android", recursive = false, extension = null, testMethod = "doFakeInvocationTest", testClassName = "Invoke")
            }

            testClass<AbstractAndroidIrBoxTest> {
                model(
                    "codegen/android", recursive = false, extension = null, testMethod = "doCompileAgainstAndroidSdkTest",
                    targetBackend = TargetBackend.JVM_IR
                )
                model(
                    "codegen/android", recursive = false, extension = null, testMethod = "doFakeInvocationTest", testClassName = "Invoke",
                    targetBackend = TargetBackend.JVM_IR
                )
            }

            testClass<AbstractAndroidBytecodeShapeTest> {
                model("codegen/bytecodeShape", recursive = false, extension = null)
            }

            testClass<AbstractParcelBoxTest> {
                model("parcel/box", targetBackend = TargetBackend.JVM)
            }

            testClass<AbstractParcelIrBoxTest> {
                model("parcel/box", targetBackend = TargetBackend.JVM_IR)
            }

            testClass<AbstractParcelBytecodeListingTest> {
                model("parcel/codegen", targetBackend = TargetBackend.JVM)
            }

            testClass<AbstractParcelIrBytecodeListingTest> {
                model("parcel/codegen", targetBackend = TargetBackend.JVM_IR)
            }
        }

        testGroup("plugins/parcelize/parcelize-compiler/tests", "plugins/parcelize/parcelize-compiler/testData") {
            testClass<AbstractParcelizeBoxTest> {
                model("box", targetBackend = TargetBackend.JVM)
            }

            testClass<AbstractParcelizeIrBoxTest> {
                model("box", targetBackend = TargetBackend.JVM_IR)
            }

            testClass<AbstractParcelizeBytecodeListingTest> {
                model("codegen", targetBackend = TargetBackend.JVM)
            }

            testClass<AbstractParcelizeIrBytecodeListingTest> {
                model("codegen", targetBackend = TargetBackend.JVM_IR)
            }
        }

        testGroup("plugins/parcelize/parcelize-ide/tests", "plugins/parcelize/parcelize-ide/testData") {
            testClass<AbstractParcelizeQuickFixTest> {
                model("quickfix", pattern = "^([\\w\\-_]+)\\.kt$", filenameStartsLowerCase = true)
            }

            testClass<AbstractParcelizeCheckerTest> {
                model("checker", extension = "kt")
            }
        }

        testGroup("plugins/jvm-abi-gen/test", "plugins/jvm-abi-gen/testData") {
            testClass<AbstractCompareJvmAbiTest> {
                model("compare", recursive = false, extension = null)
            }

            testClass<AbstractJvmAbiContentTest> {
                model("content", recursive = false, extension = null)
            }

            testClass<AbstractCompileAgainstJvmAbiTest> {
                model("compile", recursive = false, extension = null)
            }
        }

        testGroup("plugins/kapt3/kapt3-compiler/test", "plugins/kapt3/kapt3-compiler/testData") {
            testClass<AbstractClassFileToSourceStubConverterTest> {
                model("converter")
            }

            testClass<AbstractKotlinKaptContextTest> {
                model("kotlinRunner")
            }

            testClass<AbstractIrClassFileToSourceStubConverterTest> {
                model("converter", targetBackend = TargetBackend.JVM_IR)
            }

            testClass<AbstractIrKotlinKaptContextTest> {
                model("kotlinRunner", targetBackend = TargetBackend.JVM_IR)
            }
        }

        testGroup("plugins/kapt3/kapt3-cli/test", "plugins/kapt3/kapt3-cli/testData") {
            testClass<AbstractArgumentParsingTest> {
                model("argumentParsing", extension = "txt")
            }

            testClass<AbstractKaptToolIntegrationTest> {
                model("integration", recursive = false, extension = null)
            }
        }

        testGroup("plugins/allopen/allopen-cli/test", "plugins/allopen/allopen-cli/testData") {
            testClass<AbstractBytecodeListingTestForAllOpen> {
                model("bytecodeListing", extension = "kt")
            }
            testClass<AbstractIrBytecodeListingTestForAllOpen> {
                model("bytecodeListing", extension = "kt")
            }
        }

        testGroup("plugins/noarg/noarg-cli/test", "plugins/noarg/noarg-cli/testData") {
            testClass<AbstractDiagnosticsTestForNoArg> { model("diagnostics", extension = "kt") }

            testClass<AbstractBytecodeListingTestForNoArg> {
                model("bytecodeListing", extension = "kt", targetBackend = TargetBackend.JVM)
            }
            testClass<AbstractIrBytecodeListingTestForNoArg> {
                model("bytecodeListing", extension = "kt", targetBackend = TargetBackend.JVM_IR)
            }

            testClass<AbstractBlackBoxCodegenTestForNoArg> { model("box", targetBackend = TargetBackend.JVM) }
            testClass<AbstractIrBlackBoxCodegenTestForNoArg> { model("box", targetBackend = TargetBackend.JVM_IR) }
        }

        testGroup("plugins/sam-with-receiver/sam-with-receiver-cli/test", "plugins/sam-with-receiver/sam-with-receiver-cli/testData") {
            testClass<AbstractSamWithReceiverTest> {
                model("diagnostics")
            }
            testClass<AbstractSamWithReceiverScriptTest> {
                model("script", extension = "kts")
            }
        }

        testGroup(
            "plugins/kotlin-serialization/kotlin-serialization-compiler/test",
            "plugins/kotlin-serialization/kotlin-serialization-compiler/testData"
        ) {
            testClass<AbstractSerializationPluginDiagnosticTest> {
                model("diagnostics")
            }

            testClass<AbstractSerializationPluginBytecodeListingTest> {
                model("codegen")
            }

            testClass<AbstractSerializationIrBytecodeListingTest> {
                model("codegen")
            }
        }

        testGroup(
            "plugins/kotlin-serialization/kotlin-serialization-ide/test",
            "plugins/kotlin-serialization/kotlin-serialization-ide/testData"
        ) {
            testClass<AbstractSerializationPluginIdeDiagnosticTest> {
                model("diagnostics")
            }
            testClass<AbstractSerializationQuickFixTest> {
                model("quickfix", pattern = "^([\\w\\-_]+)\\.kt$", filenameStartsLowerCase = true)
            }
        }

        testGroup("plugins/fir/fir-plugin-prototype/tests", "plugins/fir/fir-plugin-prototype/testData") {
            testClass<AbstractFirAllOpenDiagnosticTest> {
                model("")
            }
        }

        testGroup("idea/performanceTests/test", "idea/testData") {
            testClass<AbstractPerformanceJavaToKotlinCopyPasteConversionTest> {
                model("copyPaste/conversion", testMethod = "doPerfTest", pattern = """^([^\.]+)\.java$""")
            }

            testClass<AbstractPerformanceNewJavaToKotlinCopyPasteConversionTest> {
                model("copyPaste/conversion", testMethod = "doPerfTest", pattern = """^([^\.]+)\.java$""")
            }

            testClass<AbstractPerformanceLiteralKotlinToKotlinCopyPasteTest> {
                model("copyPaste/literal", testMethod = "doPerfTest", pattern = """^([^\.]+)\.kt$""")
            }

            testClass<AbstractPerformanceHighlightingTest> {
                model("highlighter", testMethod = "doPerfTest")
            }

            testClass<AbstractPerformanceAddImportTest> {
                model("addImport", testMethod = "doPerfTest", pattern = KT_WITHOUT_DOTS_IN_NAME)
            }

            testClass<AbstractPerformanceTypingIndentationTest> {
                model("indentationOnNewline", testMethod = "doPerfTest", pattern = KT_OR_KTS_WITHOUT_DOTS_IN_NAME)
            }
        }

        testGroup("idea/performanceTests/test", "idea/idea-completion/testData") {
            testClass<AbstractPerformanceCompletionIncrementalResolveTest> {
                model("incrementalResolve", testMethod = "doPerfTest")
            }

            testClass<AbstractPerformanceBasicCompletionHandlerTest> {
                model("handlers/basic", testMethod = "doPerfTest", pattern = KT_WITHOUT_DOTS_IN_NAME)
            }

            testClass<AbstractPerformanceSmartCompletionHandlerTest> {
                model("handlers/smart", testMethod = "doPerfTest")
            }

            testClass<AbstractPerformanceKeywordCompletionHandlerTest> {
                model("handlers/keywords", testMethod = "doPerfTest")
            }

            testClass<AbstractPerformanceCompletionCharFilterTest> {
                model("handlers/charFilter", testMethod = "doPerfTest", pattern = KT_WITHOUT_DOTS_IN_NAME)
            }
        }
/*
    testGroup("plugins/android-extensions/android-extensions-idea/tests", "plugins/android-extensions/android-extensions-idea/testData") {
        testClass<AbstractAndroidCompletionTest> {
            model("android/completion", recursive = false, extension = null)
        }

        testClass<AbstractAndroidGotoTest> {
            model("android/goto", recursive = false, extension = null)
        }

        testClass<AbstractAndroidRenameTest> {
            model("android/rename", recursive = false, extension = null)
        }

        testClass<AbstractAndroidLayoutRenameTest> {
            model("android/renameLayout", recursive = false, extension = null)
        }

        testClass<AbstractAndroidFindUsagesTest> {
            model("android/findUsages", recursive = false, extension = null)
        }

        testClass<AbstractAndroidUsageHighlightingTest> {
            model("android/usageHighlighting", recursive = false, extension = null)
        }

        testClass<AbstractAndroidExtractionTest> {
            model("android/extraction", recursive = false, extension = null)
        }

        testClass<AbstractParcelCheckerTest> {
            model("android/parcel/checker", excludeParentDirs = true)
        }

        testClass<AbstractParcelQuickFixTest> {
            model("android/parcel/quickfix", pattern = """^(\w+)\.((before\.Main\.\w+)|(test))$""", testMethod = "doTestWithExtraFile")
        }
    }

    testGroup("idea/idea-android/tests", "idea/testData") {
        testClass<AbstractConfigureProjectTest> {
            model("configuration/android-gradle", pattern = """(\w+)_before\.gradle$""", testMethod = "doTestAndroidGradle")
            model("configuration/android-gsk", pattern = """(\w+)_before\.gradle.kts$""", testMethod = "doTestAndroidGradle")
        }

        testClass<AbstractAndroidIntentionTest> {
            model("android/intention", pattern = "^([\\w\\-_]+)\\.kt$")
        }

        testClass<AbstractAndroidResourceIntentionTest> {
            model("android/resourceIntention", extension = "test", singleClass = true)
        }

        testClass<AbstractAndroidQuickFixMultiFileTest> {
            model("android/quickfix", pattern = """^(\w+)\.((before\.Main\.\w+)|(test))$""", testMethod = "doTestWithExtraFile")
        }

        testClass<AbstractKotlinLintTest> {
            model("android/lint", excludeParentDirs = true)
        }

        testClass<AbstractAndroidLintQuickfixTest> {
            model("android/lintQuickfix", pattern = "^([\\w\\-_]+)\\.kt$")
        }

        testClass<AbstractAndroidResourceFoldingTest> {
            model("android/folding")
        }

        testClass<AbstractAndroidGutterIconTest> {
            model("android/gutterIcon")
        }
    }
*/
    }
}
