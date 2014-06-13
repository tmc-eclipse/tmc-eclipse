package fi.helsinki.cs.plugin.tmc.async.tasks;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import fi.helsinki.cs.plugin.tmc.async.BackgroundTask;
import fi.helsinki.cs.plugin.tmc.services.Settings;
import fi.helsinki.cs.plugin.tmc.ui.IdeUIInvoker;

public class AntTestrunnerTaskTest {

    private AntTestrunnerTask runner;

    @Before
    public void setUp() {
        Settings settings = mock(Settings.class);
        IdeUIInvoker invoker = mock(IdeUIInvoker.class);
        this.runner = new AntTestrunnerTask("", "", "", 0, settings, invoker);
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
