package fi.helsinki.cs.plugin.tmc.domain;

import fi.helsinki.cs.tmc.testrunner.CaughtException;
import fi.helsinki.cs.tmc.testrunner.TestCase;

public class TestCaseResult {

    private String name;
    private boolean successful;
    private String message;
    private CaughtException exception;
    private String detailedMessage;

    public TestCaseResult() {
    }

    public TestCaseResult(String name, boolean successful, String message) {
        this.name = name;
        this.successful = successful;
        this.message = message;
    }

    public TestCaseResult(String name, boolean successful, String message, String valgrindTrace) {
        this(name, successful, message);
        this.detailedMessage = valgrindTrace;
    }

    public String getName() {
        return name;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public String getMessage() {
        return message;
    }

    public CaughtException getException() {
        return exception;
    }

    public String getDetailedMessage() {
        return detailedMessage;
    }

    /**
     * Creates a TestCaseResult from a TestCase probably returned by a local run
     * of tmc-junit-runner.
     */
    public static TestCaseResult fromTestCaseRecord(TestCase tc) {
        TestCaseResult tcr = new TestCaseResult();
        tcr.name = tc.className + " " + tc.methodName;
        tcr.successful = (tc.status == TestCase.Status.PASSED);
        tcr.message = tc.message;
        tcr.exception = tc.exception;
        return tcr;
    }
}
