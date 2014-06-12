package fi.helsinki.cs.plugin.tmc.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import fi.helsinki.cs.plugin.tmc.domain.TestCaseResult;
import fi.helsinki.cs.tmc.testrunner.TestCase;
import fi.helsinki.cs.tmc.testrunner.TestCaseList;

public class TestResultParserTest {

    private String json = "[{\"className\":\"testClass\",\"methodName\":\"testMethod\",\"pointNames\":[\"a\",\"b\",\"c\"],\"status\":\"NOT_STARTED\"},{\"className\":\"anotherTestClass\",\"methodName\":\"anotherTestMethod\",\"pointNames\":[\"d\",\"e\",\"f\"],\"status\":\"NOT_STARTED\"}]";

    private TestResultParser parser;
    private TestCaseList list;

    @Before
    public void setUp() {
        parser = new TestResultParser();

        list = new TestCaseList();

        String[] points = {"a", "b", "c"};
        TestCase a = new TestCase("testClass", "testMethod", points);

        String[] points2 = {"d", "e", "f"};
        TestCase b = new TestCase("anotherTestClass", "anotherTestMethod", points2);

        list.add(a);
        list.add(b);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseTestResultsWithInvalidJson() {
        List<TestCaseResult> results = parser.parseTestResults("").getTestCaseResults();
    }

    @Test
    public void testParseTestResultsWithValidJson() throws IOException {
        List<TestCaseResult> results = parser.parseTestResults(json).getTestCaseResults();

        for (int i = 0; i < results.size(); i++) {
            TestCaseResult res = results.get(i);
            assertEquals(res.getName(), list.get(i).className + " " + list.get(i).methodName);
            assert (res.getMessage() == null);
            assertFalse(res.isSuccessful());
        }
    }
}
