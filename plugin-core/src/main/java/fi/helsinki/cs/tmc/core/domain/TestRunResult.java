package fi.helsinki.cs.tmc.core.domain;

import java.util.List;

/**
 * Results of a single test run
 * 
 */
public class TestRunResult {
    private final List<TestCaseResult> testCaseResults;

    public TestRunResult(List<TestCaseResult> testCaseResults) {
        this.testCaseResults = testCaseResults;
    }

    public List<TestCaseResult> getTestCaseResults() {
        return testCaseResults;
    }
}