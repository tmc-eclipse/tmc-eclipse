package fi.helsinki.cs.tmc.core;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Field;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fi.helsinki.cs.tmc.core.Core;
import fi.helsinki.cs.tmc.core.TMCErrorHandler;
import fi.helsinki.cs.tmc.core.async.BackgroundTaskRunner;

public class CoreTest {
    private Core core;
    private MockServiceFactory factory;

    @Before
    public void setUp() throws NoSuchFieldException, SecurityException, IllegalArgumentException,
            IllegalAccessException {

        // some other test may have set the singleton before running any tests
        // here
        unsetSingleton();
        factory = new MockServiceFactory();
        core = Core.getInstance(factory);
    }

    @After
    public void tearDown() throws NoSuchFieldException, SecurityException, IllegalArgumentException,
            IllegalAccessException {
        // ensure that singleton is unset even after running the last test
        unsetSingleton();
    }

    private void unsetSingleton() throws NoSuchFieldException, SecurityException, IllegalArgumentException,
            IllegalAccessException {
        Field coreField = Core.class.getDeclaredField("core");
        coreField.setAccessible(true);
        coreField.set(null, null);
    }

    @Test
    public void getInstanceReturnSameObjectOnFurtherCalls() {
        assertEquals(core, Core.getInstance(factory));
    }

    @Test
    public void settingsCorrectAfterSingletonInitialization() {
        assertEquals(factory.getSettings(), Core.getSettings());
    }

    @Test
    public void canSetTaskRunner() {
        BackgroundTaskRunner runner = mock(BackgroundTaskRunner.class);
        Core.setTaskRunner(runner);
        assertEquals(runner, Core.getTaskRunner());
    }

    @Test
    public void courseDAOCorrectAfterSingletonInitialization() {
        assertEquals(factory.getCourseDAO(), Core.getCourseDAO());
    }

    @Test
    public void projectDAOCorrectAfterSingletonInitialization() {
        assertEquals(factory.getProjectDAO(), Core.getProjectDAO());
    }

    @Test
    public void reviewDAOCorrectAfterSingletonInitialization() {
        assertEquals(factory.getReviewDAO(), Core.getReviewDAO());
    }

    @Test
    public void serverManagerCorrectAfterSingletonInitialization() {
        assertEquals(factory.getServerManager(), Core.getServerManager());
    }

    @Test
    public void updaterCorrectAfterSingletonInitialization() {
        assertEquals(factory.getUpdater(), Core.getUpdater());
    }

    @Test
    public void spywareCorrectAfterSingletonInitialization() {
        assertEquals(factory.getSpyware(), Core.getSpyware());
    }

    @Test
    public void projectEventHandlerCorrectAfterSingletonInitialization() {
        assertEquals(factory.getProjectEventHandler(), Core.getProjectEventHandler());
    }

    @Test
    public void canSetErrorHandler() {
        TMCErrorHandler eh = mock(TMCErrorHandler.class);
        Core.setErrorHandler(eh);
        assertEquals(eh, Core.getErrorHandler());
    }
}
