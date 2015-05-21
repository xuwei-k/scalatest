/*
* Copyright 2001-2015 Artima, Inc.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

import io.Source
import java.io.{File, FileWriter, BufferedWriter}

object GenScalaTestJS {

  private def uncommentJsExport(line: String): String =
    if (line.trim.startsWith("//SCALATESTJS-ONLY "))
      line.substring(line.indexOf("//SCALATESTJS-ONLY ") + 19)
    else
      line

  private def transformLine(line: String): String =
    uncommentJsExport(line)

  private def copyFile(sourceFile: File, destFile: File): File = {
    val destWriter = new BufferedWriter(new FileWriter(destFile))
    try {
      val lines = Source.fromFile(sourceFile).getLines.toList
      var skipMode = false
      for (line <- lines) {
        if (line.trim == "// SKIP-SCALATESTJS-START")
          skipMode = true
        else if (line.trim == "// SKIP-SCALATESTJS-END")
          skipMode = false
        else if (!skipMode) {
          destWriter.write(transformLine(line))
          destWriter.newLine()
        }
      }
      destFile
    }
    finally {
      destWriter.flush()
      destWriter.close()
      println("Copied " + destFile.getAbsolutePath)
    }
  }

  def copyDir(sourceDirName: String, packageDirName: String, files: List[String], targetDir: File): Seq[File] = {
    val packageDir = new File(targetDir, packageDirName)
    packageDir.mkdirs()
    val sourceDir = new File(sourceDirName)
    files.map { sourceFileName =>
      val sourceFile = new File(sourceDir, sourceFileName)
      val destFile = new File(packageDir, sourceFile.getName)
      copyFile(sourceFile, destFile)
    }
  }

  def genJava(targetDir: File, version: String, scalaVersion: String): Seq[File] = {
    copyDir("scalatest/src/main/java/org/scalatest", "org/scalatest",
            List(
              "Finders.java",
              "TagAnnotation.java",
              "WrapWith.java",
              "DoNotDiscover.java",
              "Ignore.java"
            ), targetDir)
  }

  def genScala(targetDir: File, version: String, scalaVersion: String): Seq[File] = {

    copyDir("scalatest/src/main/scala/org/scalatest", "org/scalatest",
            List(
              "Suite.scala",
              "OutcomeOf.scala",
              "Assertions.scala",
              "Outcome.scala",
              "TestData.scala",
              "ConfigMap.scala",
              "Reporter.scala",
              //"DispatchReporter.scala",
              "CatchReporter.scala",
              //"ConfigMapWrapperSuite.scala",    // skipped because depends on java reflection.
              "ResourcefulReporter.scala",
              "Tracker.scala",
              "Filter.scala",
              "DynaTags.scala",
              "Status.scala",
              "Args.scala",
              "Stopper.scala",
              "Distributor.scala",
              "DistributedTestSorter.scala",
              "DistributedSuiteSorter.scala",
              "Informer.scala",
              "EncodedOrdering.scala",
              "ConcurrentInformer.scala",
              "Documenter.scala",
              "SuiteHelpers.scala",
              "PendingNothing.scala",
              "AssertionsMacro.scala",
              "CompileMacro.scala",
              "AppendedClues.scala",
              "Notifier.scala",
              "Alerter.scala",
              "SlowpokeDetector.scala",
              "Slowpoke.scala",
              "RunningTest.scala",
              "ParallelTestExecution.scala",
              "OneInstancePerTest.scala",
              "SuiteMixin.scala",
              "Engine.scala",
              "Tag.scala",
              "FunSuiteLike.scala",
              "FunSuite.scala",
              "TestRegistration.scala",
              "Informing.scala",
              "Notifying.scala",
              "Alerting.scala",
              "Documenting.scala",
              "Transformer.scala",
              "DeferredAbortedSuite.scala",
              "Suites.scala",
              "FunSpecLike.scala",
              "FunSpec.scala",
              "UnquotedString.scala",
              "FlatSpecLike.scala",
              "FlatSpec.scala",
              "WordSpecLike.scala",
              "WordSpec.scala",
              "FreeSpecLike.scala",
              "FreeSpec.scala",
              "PropSpecLike.scala",
              "PropSpec.scala",
              "FeatureSpecLike.scala",
              "FeatureSpec.scala",
              "MatchersHelper.scala",
              "Matchers.scala",
              "Entry.scala",
              "Inspectors.scala",
              "OptionValues.scala",
              "Inside.scala",
              "NonImplicitAssertions.scala",
              "BeforeAndAfterAll.scala",
              "BeforeAndAfterEachTestData.scala",
              "BeforeAndAfterAllConfigMap.scala",
              "BeforeAndAfterEach.scala", 
              "GivenWhenThen.scala",
              "SeveredStackTraces.scala",
              "BeforeAndAfter.scala",
              "CancelAfterFailure.scala",
              "StopOnFailureReporter.scala",
              //"ConfigMapWrapperSuite.scala",     // skipped because depends on java reflection
              "Checkpoints.scala",
              "DiagrammedAssertions.scala",
              "DiagrammedExpr.scala",
              "DiagrammedAssertionsMacro.scala",
              "DiagrammedExprMacro.scala",
              "EitherValues.scala",
              "LoneElement.scala",
              "Retries.scala",
              "PartialFunctionValues.scala",
              "RandomTestOrder.scala",
              "SequentialNestedSuiteExecution.scala"
            ), targetDir) ++
    copyDir("scalatest/src/main/scala/org/scalatest/fixture", "org/scalatest/fixture",
            List(
              "Suite.scala",
              "TestDataFixture.scala",
              "TestRegistration.scala",
              "Transformer.scala",
              "UnitFixture.scala",
              "NoArg.scala",
              "NoArgTestWrapper.scala",
              "FixtureNodeFamily.scala",
              "FunSuiteLike.scala",
              "FunSuite.scala",
              "FlatSpecLike.scala",
              "FlatSpec.scala",
              "FunSpecLike.scala",
              "FunSpec.scala",
              "WordSpecLike.scala",
              "WordSpec.scala",
              "FreeSpecLike.scala",
              "FreeSpec.scala",
              "PropSpecLike.scala",
              "PropSpec.scala",
              "FeatureSpecLike.scala",
              "FeatureSpec.scala"
            ), targetDir) ++
    copyDir("scalatest/src/main/scala/org/scalatest/events", "org/scalatest/events",
            List(
              "Event.scala",
              "Ordinal.scala",
              "Formatter.scala",
              "Location.scala",
              "Summary.scala",
              "NameInfo.scala"
            ), targetDir) ++
    copyDir("scalatest/src/main/scala/org/scalatest/matchers", "org/scalatest/matchers",
            List(
              "MatchResult.scala",
              "AMatcher.scala",
              "AnMatcher.scala",
              "BeMatcher.scala",
              "BePropertyMatchResult.scala",
              "BePropertyMatcher.scala",
              "HavePropertyMatchResult.scala",
              "HavePropertyMatcher.scala",
              "LazyArg.scala",
              "LazyMessage.scala",
              "Matcher.scala",
              "TypeMatcherMacro.scala",
              "MatchPatternMacro.scala",
              "MatcherProducers.scala",
              "MatchFailed.scala",
              "MatchPatternHelper.scala",
              "MatchSucceeded.scala",
              "TypeMatcherHelper.scala"
            ), targetDir) ++
    copyDir("scalatest/src/main/scala/org/scalatest/tools", "org/scalatest/tools",
      List(
        "SuiteDiscoveryHelper.scala",
        "StringReporter.scala",
        "SuiteRunner.scala",
        "Fragment.scala",
        "ParsedArgs.scala",
        "ReporterConfiguration.scala",
        "AnsiColor.scala",
        "ReporterConfigParam.scala",
        "EventToPresent.scala",
        "DiscoverySuite.scala",
        "SuiteSortingReporter.scala",
        //"ConcurrentDistributor.scala",
        "FilterReporter.scala",
        "SuiteResult.scala",
        "SuiteParam.scala",
        "NestedSuiteParam.scala",
        "TestSpec.scala",
        "DistributedTestRunnerSuite.scala",
        "SuiteResultHolder.scala",
        "Durations.scala",
        "TestSortingReporter.scala",
        "RunDoneListener.scala",
        "SbtDispatchReporter.scala",
        "FriendlyParamsTranslator.scala"
      ), targetDir) ++
    copyDir("scalatest/src/main/scala/org/scalatest/exceptions", "org/scalatest/exceptions",
      List(
        "StackDepthException.scala",
        "NotAllowedException.scala",
        "StackDepth.scala",
        "TestPendingException.scala",
        "TestCanceledException.scala",
        "ModifiableMessage.scala",
        "PayloadField.scala",
        "ModifiablePayload.scala",
        "TestFailedException.scala",
        "PropertyCheckFailedException.scala",
        "TableDrivenPropertyCheckFailedException.scala",
        "DuplicateTestNameException.scala",
        "TestRegistrationClosedException.scala",
        "GeneratorDrivenPropertyCheckFailedException.scala",
        "DiscardedEvaluationException.scala",
        "TimeoutField.scala",
        "TestFailedDueToTimeoutException.scala"
      ), targetDir) ++
    copyDir("scalatest/src/main/scala/org/scalatest/time", "org/scalatest/time",
      List(
        "Now.scala",
        "Span.scala",
        "SpanSugar.scala",
        "Units.scala"
      ), targetDir) ++
    copyDir("scalatest/src/main/scala/org/scalatest/words", "org/scalatest/words",
      List(
        "TypeCheckWord.scala",
        "CompileWord.scala",
        "ArrayWrapper.scala",
        "BehaveWord.scala",
        "ResultOfTaggedAsInvocation.scala",
        "ResultOfStringPassedToVerb.scala",
        "ShouldVerb.scala",
        "MustVerb.scala",
        "CanVerb.scala",
        "StringVerbBlockRegistration.scala",
        "ResultOfAfterWordApplication.scala",
        "RegexWithGroups.scala",
        "DefinedWord.scala",
        "ResultOfOnlyApplication.scala",
        "ResultOfTheSameInstanceAsApplication.scala",
        "EmptyWord.scala",
        "ReadableWord.scala",
        "WritableWord.scala",
        "ResultOfNotExist.scala",
        "ExistWord.scala",
        "ResultOfATypeInvocation.scala",
        "ResultOfAnTypeInvocation.scala",
        "SortedWord.scala",
        "ResultOfAtMostOneOfApplication.scala",
        "ResultOfValueWordApplication.scala",
        "ResultOfKeyWordApplication.scala",
        "ResultOfInOrderApplication.scala",
        "ResultOfInOrderOnlyApplication.scala",
        "ResultOfAllOfApplication.scala",
        "ResultOfTheSameElementsInOrderAsApplication.scala",
        "ResultOfTheSameElementsAsApplication.scala",
        "ResultOfNoneOfApplication.scala",
        "ResultOfAtLeastOneOfApplication.scala",
        "ResultOfOneOfApplication.scala",
        "ResultOfDefinedAt.scala",
        "ResultOfRegexWordApplication.scala",
        "ResultOfAnWordToAnMatcherApplication.scala",
        "ResultOfAnWordToBePropertyMatcherApplication.scala",
        "ResultOfAWordToAMatcherApplication.scala",
        "ResultOfAnWordToSymbolApplication.scala",
        "ResultOfAWordToBePropertyMatcherApplication.scala",
        "ResultOfAWordToSymbolApplication.scala",
        "ResultOfGreaterThanOrEqualToComparison.scala",
        "ResultOfLessThanOrEqualToComparison.scala",
        "ResultOfGreaterThanComparison.scala",
        "ResultOfLessThanComparison.scala",
        "ResultOfMessageWordApplication.scala",
        "ResultOfSizeWordApplication.scala",
        "ResultOfLengthWordApplication.scala",
        "ContainWord.scala",
        "NotWord.scala",
        "BeWord.scala",
        "HaveWord.scala",
        "IncludeWord.scala",
        "EndWithWord.scala",
        "StartWithWord.scala",
        "FullyMatchWord.scala",
        "MatcherWords.scala",
        "PleaseUseNoExceptionShouldSyntaxInstead.scala",
        "ResultOfOfTypeInvocation.scala",
        "ResultOfThrownByApplication.scala",
        "ResultOfBeWordForAnType.scala",
        "ResultOfBeWordForAType.scala",
        "MatchPatternWord.scala",
        "NoExceptionWord.scala",
        "SizeWord.scala",
        "LengthWord.scala",
        "ResultOfBeWordForNoException.scala",
        "ResultOfContainWord.scala",
        "ResultOfNotWordForAny.scala",
        "ResultOfTheTypeInvocation.scala",
        "ResultOfAllElementsOfApplication.scala",
        "ResultOfOneElementOfApplication.scala",
        "ResultOfAtLeastOneElementOfApplication.scala",
        "ResultOfNoElementsOfApplication.scala"
      ), targetDir) ++
    copyDir("scalatest/src/main/scala/org/scalatest/enablers", "org/scalatest/enablers",
      List(
        "Containing.scala",
        "Aggregating.scala",
        "KeyMapping.scala",
        "ValueMapping.scala",
        "Sequencing.scala",
        "Sortable.scala",
        "Readability.scala",
        "Writability.scala",
        "Emptiness.scala",
        "Definition.scala",
        "Length.scala",
        "Existence.scala",
        "Size.scala",
        "Messaging.scala",
        "Collecting.scala"
      ), targetDir) ++
    copyDir("scalatest/src/main/scala/org/scalatest/prop", "org/scalatest/prop",
      List(
        //"Configuration.scala",
        //"Checkers.scala",
        //"PropertyChecks.scala",
        "GenDrivenPropertyChecks.scala", 
        "Generator.scala", 
        "Configuration.scala", 
        "Rnd.scala", 
        "Edges.scala", 
        "Whenever.scala"
      ), targetDir) ++
    copyDir("scalatest/src/main/scala/org/scalatest/concurrent", "org/scalatest/concurrent",
      List(
        "ScalaFutures.scala",
        "Futures.scala",
        "PatienceConfiguration.scala",
        "AbstractPatienceConfiguration.scala",
        //"Eventually.scala",      // not supported because js is single thread and does not share memory.
        "ScaledTimeSpans.scala"
      ), targetDir) ++
    copyDir("scalatest/src/main/scala/org/scalatest/path", "org/scalatest/path",
      List(
        "FreeSpec.scala",
        "FreeSpecLike.scala",
        "FunSpec.scala",
        "FunSpecLike.scala"
      ), targetDir) ++
    copyDir("scalatest/src/main/scala/org/scalatest/tagobjects", "org/scalatest/tagobjects",
      List(
        "Retryable.scala"
      ), targetDir)
  }

  def genTest(targetDir: File, version: String, scalaVersion: String): Seq[File] = {
    copyDir("scalatest-test/src/test/scala/org/scalatest", "org/scalatest",
      List(
        "AlerterSpec.scala",
        "AllElementsOfContainMatcherDeciderSpec.scala",
        "AllElementsOfContainMatcherEqualitySpec.scala",
        "AllElementsOfContainMatcherSpec.scala",
        "AllOfContainMatcherDeciderSpec.scala",
        "AllOfContainMatcherEqualitySpec.scala",
        "AllOfContainMatcherSpec.scala",
        "AllSuiteProp.scala",
        "AMatcherSpec.scala",
        "AnMatcherSpec.scala",
        "AnyValMatchersSpec.scala",
        "AppendedCluesSpec.scala",
        "ArgsSpec.scala",
        "AssertionsSpec.scala",
        "BeforeAndAfterAllConfigMapSpec.scala",
        "BeforeAndAfterAllProp.scala",
        "BeforeAndAfterAllSpec.scala",
        "BeforeAndAfterConfigSuite.scala",
        "BeforeAndAfterEachTestDataSuite.scala",
        "BeforeAndAfterSuite.scala",
        "BeforeNAfterSuite.scala",
        "BigSuite.scala",
        //"BigSuiteSuite.scala",
        "CancelAfterFailureSpec.scala",
        //"CatchReporterProp.scala",   // skipped because heavily depends on java reflection
        "CatchReporterSpec.scala",
        "CheckpointsSpec.scala",
        //"ClassTaggingProp.scala",    // skipped because annotation not supported
        "ClueSpec.scala",
        "ConfigMapSpec.scala",
        //"ConfigMapWrapperSuiteSpec.scala",    // skipped because depends on java reflection
        "ContainMatcherAndOrDeciderSpec.scala",
        "ContainMatcherAndOrEqualitySpec.scala",
        "ContainMatcherAndOrExplicitEqualitySpec.scala",
        "ContainMatcherAndOrSpec.scala",
        "ConversionCheckedAssertionsSpec.scala",
        "CustomMatcherSpec.scala",
        "DiagrammedAssertionsSpec.scala",
        //"DispatchReporterSpec.scala",   // skipped because DispatchReporter uses thread.
        //"DocSpecSpec.scala",   // skipped because DocSpecSpec is not supported yet
        "EasySuite.scala",
        "EitherValuesSpec.scala",
        //"EncodedOrderingSpec.scala",  // skipped because use scala.reflect.NameTransformer.encode
        "EngineSpec.scala",
        //"EntrySpec.scala",    // skipped because Entry extends java.util.Map
        "EventHelpers.scala",
        "EveryLoneElementSpec.scala",
        "EveryShouldContainAllElementsOfLogicalAndSpec.scala",
        "EveryShouldContainAllElementsOfLogicalOrSpec.scala",
        "EveryShouldContainAllElementsOfSpec.scala",
        "EveryShouldContainAllOfLogicalAndSpec.scala",
        "EveryShouldContainAllOfLogicalOrSpec.scala",
        "EveryShouldContainAllOfSpec.scala",
        "EveryShouldContainAtLeastOneElementOfLogicalAndSpec.scala",
        "EveryShouldContainAtLeastOneElementOfLogicalOrSpec.scala",
        "EveryShouldContainAtLeastOneElementOfSpec.scala",
        "EveryShouldContainAtLeastOneOfLogicalAndSpec.scala",
        "EveryShouldContainAtLeastOneOfLogicalOrSpec.scala",
        "EveryShouldContainAtLeastOneOfSpec.scala",
        "EveryShouldContainAtMostOneOfLogicalAndSpec.scala",
        "EveryShouldContainAtMostOneOfLogicalOrSpec.scala",
        "EveryShouldContainAtMostOneOfSpec.scala",
        "EveryShouldContainInOrderLogicalAndSpec.scala",
        "EveryShouldContainInOrderLogicalOrSpec.scala",
        "EveryShouldContainInOrderOnlyLogicalAndSpec.scala",
        "EveryShouldContainInOrderOnlyLogicalOrSpec.scala",
        "EveryShouldContainInOrderOnlySpec.scala",
        "EveryShouldContainInOrderSpec.scala",
        "EveryShouldContainNoElementsOfLogicalAndSpec.scala",
        "EveryShouldContainNoElementsOfLogicalOrSpec.scala",
        "EveryShouldContainNoElementsOfSpec.scala",
        "EveryShouldContainNoneOfLogicalAndSpec.scala",
        "EveryShouldContainNoneOfLogicalOrSpec.scala",
        "EveryShouldContainNoneOfSpec.scala",
        "EveryShouldContainOneElementOfLogicalAndSpec.scala",
        "EveryShouldContainOneElementOfLogicalOrSpec.scala",
        "EveryShouldContainOneElementOfSpec.scala",
        "EveryShouldContainOneOfLogicalAndSpec.scala",
        "EveryShouldContainOneOfLogicalOrSpec.scala",
        "EveryShouldContainOneOfSpec.scala",
        "EveryShouldContainOnlyLogicalAndSpec.scala",
        "EveryShouldContainOnlyLogicalOrSpec.scala",
        "EveryShouldContainOnlySpec.scala",
        "EveryShouldContainSpec.scala",
        "EveryShouldContainTheSameElementsAsLogicalAndSpec.scala",
        "EveryShouldContainTheSameElementsAsLogicalOrSpec.scala",
        "EveryShouldContainTheSameElementsAsSpec.scala",
        "EveryShouldContainTheSameElementsInOrderAsLogicalAndSpec.scala",
        "EveryShouldContainTheSameElementsInOrderAsLogicalOrSpec.scala",
        "EveryShouldContainTheSameElementsInOrderAsSpec.scala",
        "ExampleBeforeAfterParallelSpec.scala",
        "ExampleParallelSpec.scala",
        "ExamplesSuite.scala",
        "ExampleStackSpec.scala",
        "ExampleSuiteTimeoutSpec.scala",
        "ExampleTimeoutParallelSpec.scala",
        "FailureMessagesSuite.scala",
        "FeatureSpecSpec.scala",
        "FilterProp.scala",
        "FilterSpec.scala",
        "FlatSpecImportedMatchersSpec.scala",
        "FlatSpecMixedInMatchersSpec.scala",
        "FlatSpecSpec.scala",
        "FreeSpecSpec.scala",
        "FunctionSuiteProp.scala",
        "FunctionSuiteExamples.scala",
        "FunSpecSpec.scala",
        "FunSuiteSpec.scala",
        //"FunSuiteSuite.scala",          // skipped because depends on java reflection
        "GivenWhenThenSpec.scala",
        "InformerSpec.scala",
        //"InheritedTagProp.scala",         // skipped because depends on java reflection
        "InOrderContainMatcherDeciderSpec.scala",
        "InOrderContainMatcherEqualitySpec.scala",
        "InOrderContainMatcherSpec.scala",
        "InOrderOnlyContainMatcherDeciderSpec.scala",
        "InOrderOnlyContainMatcherEqualitySpec.scala",
        "InOrderOnlyContainMatcherSpec.scala",
        "InsideMixinSpec.scala",
        "InsideSpec.scala",
        "InspectorsForMapSpec.scala",
        "InspectorShorthandsRegexWithGroupsSpec.scala",
        "InspectorShorthandsSpec.scala",
        "InspectorsSpec.scala",
        "ListLoneElementSpec.scala",
        "ListShouldBeEmptyLogicalAndSpec.scala",
        "ListShouldBeEmptyLogicalOrSpec.scala",
        "ListShouldBeEmptySpec.scala",
        "ListShouldContainAllElementsOfLogicalAndSpec.scala",
        "ListShouldContainAllElementsOfLogicalOrSpec.scala",
        "ListShouldContainAllElementsOfSpec.scala",
        "ListShouldContainAllOfLogicalAndSpec.scala",
        "ListShouldContainAllOfLogicalOrSpec.scala",
        "ListShouldContainAllOfSpec.scala",
        "ListShouldContainAtLeastOneElementOfLogicalAndSpec.scala",
        "ListShouldContainAtLeastOneElementOfLogicalOrSpec.scala",
        "ListShouldContainAtLeastOneElementOfSpec.scala",
        "ListShouldContainAtLeastOneOfLogicalAndSpec.scala",
        "ListShouldContainAtLeastOneOfLogicalOrSpec.scala",
        "ListShouldContainAtLeastOneOfSpec.scala",
        "ListShouldContainAtMostOneOfLogicalAndSpec.scala",
        "ListShouldContainAtMostOneOfLogicalOrSpec.scala",
        "ListShouldContainAtMostOneOfSpec.scala",
        "ListShouldContainInOrderLogicalAndSpec.scala",
        "ListShouldContainInOrderLogicalOrSpec.scala",
        "ListShouldContainInOrderOnlyLogicalAndSpec.scala",
        "ListShouldContainInOrderOnlyLogicalOrSpec.scala",
        "ListShouldContainInOrderOnlySpec.scala",
        "ListShouldContainInOrderSpec.scala",
        "ListShouldContainNoElementsOfLogicalAndSpec.scala",
        "ListShouldContainNoElementsOfLogicalOrSpec.scala",
        "ListShouldContainNoElementsOfSpec.scala",
        "ListShouldContainNoneOfLogicalAndSpec.scala",
        "ListShouldContainNoneOfLogicalOrSpec.scala",
        "ListShouldContainNoneOfSpec.scala",
        "ListShouldContainOneElementOfLogicalAndSpec.scala",
        "ListShouldContainOneElementOfLogicalOrSpec.scala",
        "ListShouldContainOneElementOfSpec.scala",
        "ListShouldContainOneOfLogicalAndSpec.scala",
        "ListShouldContainOneOfLogicalOrSpec.scala",
        "ListShouldContainOneOfSpec.scala",
        "ListShouldContainOnlyLogicalAndSpec.scala",
        "ListShouldContainOnlyLogicalOrSpec.scala",
        "ListShouldContainOnlySpec.scala",
        "ListShouldContainSpec.scala",
        "ListShouldContainTheSameElementsAsLogicalAndSpec.scala",
        "ListShouldContainTheSameElementsAsLogicalOrSpec.scala",
        "ListShouldContainTheSameElementsAsSpec.scala",
        "ListShouldContainTheSameElementsInOrderAsLogicalAndSpec.scala",
        "ListShouldContainTheSameElementsInOrderAsLogicalOrSpec.scala",
        "ListShouldContainTheSameElementsInOrderAsSpec.scala",
        "MapShouldBeDefinedAtSpec.scala",
        "MatcherGenSpec.scala",
        "MatchersSpec.scala",
        "MethodSuiteProp.scala",
        "MethodSuiteExamples.scala",
        "NoElementsOfContainMatcherDeciderSpec.scala",
        "NoElementsOfContainMatcherEqualitySpec.scala",
        "NoElementsOfContainMatcherSpec.scala",
        "NoneOfContainMatcherDeciderSpec.scala",
        "NoneOfContainMatcherEqualitySpec.scala",
        "NoneOfContainMatcherSpec.scala",
        "NonImplicitAssertionsSuite.scala",
        "NotifierSpec.scala",
        //"OldDocSpec.scala",             // Do we still need this?
        "OneElementOfContainMatcherDeciderSpec.scala",
        "OneElementOfContainMatcherEqualitySpec.scala",
        "OneElementOfContainMatcherSpec.scala",
        "OneInstancePerTestSpec.scala",
        "OneOfContainMatcherDeciderSpec.scala",
        "OneOfContainMatcherEqualitySpec.scala",
        "OneOfContainMatcherSpec.scala",
        "OnlyContainMatcherDeciderSpec.scala",
        "OnlyContainMatcherEqualitySpec.scala",
        "OnlyContainMatcherSpec.scala",
        "OptionShouldContainOneElementOfLogicalAndSpec.scala",
        "OptionShouldContainOneElementOfLogicalOrSpec.scala",
        "OptionShouldContainOneElementOfSpec.scala",
        "OptionShouldContainOneOfLogicalAndSpec.scala",
        "OptionShouldContainOneOfLogicalOrSpec.scala",
        "OptionShouldContainOneOfSpec.scala",
        "OptionShouldContainSpec.scala",
        "OptionValuesSpec.scala",
        "OutcomeSpec.scala",
        "ParallelTestExecutionInfoExamples.scala",
        "ParallelTestExecutionOrderExamples.scala",
        "ParallelTestExecutionParallelSuiteExamples.scala",
        "ParallelTestExecutionProp.scala",
        "ParallelTestExecutionSuiteTimeoutExamples.scala",
        "ParallelTestExecutionTestTimeoutExamples.scala",
        "ParallelTestExecutionSpec.scala",
        "PartialFunctionValuesSpec.scala",
        "PrettyAstSpec.scala",
        //"PrivateMethodTesterSpec.scala",   // skipped because depends on java reflection
        //"PropertyFunSuite.scala",   // skipped because depends on java reflection
        "PropSpecSpec.scala",
        "RandomTestOrderSpec.scala",
        "RetriesSpec.scala",
        "RunInSpurtsSpec1.scala",
        "RunInSpurtsSpec2.scala",
        "RunningTestSpec.scala",
        //"SavesConfigMapSuite.scala",    // skipped because depends on java reflection
        "WordSpecSpec.scala",
        "SequentialNestedSuiteExecutionSpec.scala",
        "SeveredStackTracesFailureSpec.scala",
        "SeveredStackTracesSpec.scala",
        //"ShellSuite.scala",             // skipped because execute is not supported for now, as it depends on Suite.execute, which in turns depends on StandardOutReporter, PrintReporter that depends on java classes.
        "ShorthandShouldBeThrownBySpec.scala",
        "StringFixture.scala",
        "SuiteExamples.scala",
        "SuiteProp.scala"
      ), targetDir) ++
    copyDir("scalatest-test/src/test/scala/org/scalatest/fixture", "org/scalatest/fixture",
      List(
        "FunSuiteSpec.scala",
        "FunSpecSpec.scala",
        "FeatureSpecSpec.scala", 
        "FlatSpecSpec.scala", 
        "FreeSpecSpec.scala", 
        "PropSpecSpec.scala", 
        "WordSpecSpec.scala"
      ), targetDir) ++
    copyDir("scalatest-test/src/test/scala/org/scalatest/words", "org/scalatest/words",
      List(
        "ResultOfAllElementsOfApplicationSpec.scala",
        "ResultOfAtLeastOneElementOfApplicationSpec.scala",
        "ResultOfNoElementsOfApplicationSpec.scala",
        "ResultOfOneElementOfApplicationSpec.scala"
      ), targetDir)
  }

  def genResource(targetDir: File, version: String, scalaVersion: String): Seq[File] = {
    val sourceResourceFile = new File("scalatest/src/main/resources/org/scalatest/ScalaTestBundle.properties")
    val destResourceDir = new File(targetDir, "resources/org/scalatest")
    destResourceDir.mkdirs()
    val destResourceFile = new File(destResourceDir, "ScalaTestBundle.properties")
    copyFile(sourceResourceFile, destResourceFile)
    List(destResourceFile)
  }
}