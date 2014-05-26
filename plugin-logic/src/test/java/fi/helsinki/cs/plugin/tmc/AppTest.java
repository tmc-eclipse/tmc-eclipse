package fi.helsinki.cs.plugin.tmc;

import fi.helsinki.cs.tmc.testrunner.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
    /**
     * Create the test case
     * 
     * @param testName
     *            name of the test case
     */
    static String[] a={"ASd"};
    
    public AppTest() {    
        super("asd","asd", a);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(AppTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    
    public void testApp() {
        junit.framework.TestCase.assertTrue(true);
    }
}
