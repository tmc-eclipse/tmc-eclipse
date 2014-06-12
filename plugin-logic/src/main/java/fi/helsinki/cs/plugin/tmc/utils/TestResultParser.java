package fi.helsinki.cs.plugin.tmc.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import fi.helsinki.cs.plugin.tmc.domain.TestCaseResult;
import fi.helsinki.cs.plugin.tmc.domain.TestRunResult;
import fi.helsinki.cs.tmc.testrunner.StackTraceSerializer;
import fi.helsinki.cs.tmc.testrunner.TestCase;
import fi.helsinki.cs.tmc.testrunner.TestCaseList;

public class TestResultParser {
    public TestRunResult parseTestResults(File resultsFile) throws IOException {
        String resultsJson = FileUtils.readFileToString(resultsFile, "UTF-8");
        return parseTestResults(resultsJson);
    }

    public TestRunResult parseTestResults(String resultsJson) {
        Gson gson = new GsonBuilder().registerTypeAdapter(StackTraceElement.class, new StackTraceSerializer()).create();

        TestCaseList testCaseRecords = gson.fromJson(resultsJson, TestCaseList.class);
        if (testCaseRecords == null) {
            throw new IllegalArgumentException("Empty result from test runner");
        }

        List<TestCaseResult> testCaseResults = new ArrayList<TestCaseResult>();
        for (TestCase tc : testCaseRecords) {
            testCaseResults.add(TestCaseResult.fromTestCaseRecord(tc));
        }
        return new TestRunResult(testCaseResults);
    }
}
