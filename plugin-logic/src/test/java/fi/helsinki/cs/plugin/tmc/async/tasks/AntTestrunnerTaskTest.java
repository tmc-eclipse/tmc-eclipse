package fi.helsinki.cs.plugin.tmc.async.tasks;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import fi.helsinki.cs.plugin.tmc.async.BackgroundTask;

public class AntTestrunnerTaskTest {

    private AntTestrunnerTask runner;

    @Before
    public void setUp() {
        this.runner = new AntTestrunnerTask("", "", "", 0);
    }

    @Test
    public void testImplementsInterfaces() {
        assertTrue(runner instanceof BackgroundTask && runner instanceof TestrunnerTask);
    }

    @Test
    public void testDescriptionIsValid() {
        assertNotNull(runner.getDescription());
    }

}
