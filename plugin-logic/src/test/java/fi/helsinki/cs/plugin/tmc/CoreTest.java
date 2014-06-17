package fi.helsinki.cs.plugin.tmc;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Field;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fi.helsinki.cs.plugin.tmc.async.BackgroundTaskRunner;

public class CoreTest {
    private Core core;
    private MockServiceFactory factory;

    @Before
    public void setUp() {

        factory = new MockServiceFactory();
        core = Core.getInstance(factory);
    }

    // due to Core being singleton, we need to set its instance variable to null
    // to ensure every test has fresh copy of it
    @After
    public void tearDown() throws NoSuchFieldException, SecurityException, IllegalArgumentException,
            IllegalAccessException {
        Field coreField = Core.class.getDeclaredField("core");
        coreField.setAccessible(true);
        coreField.set(null, null);
    }

    @Test
    public void getInstanceReturnSameObjectOnFurtherCalls() {
        assertEquals(core, Core.getInstance());
    }

    @Test
    public void settingsCorrectAfterSingletonInitialization() {
        assertEquals(factory.getSettings(), Core.getSettings());
    }

    @Test
    public void courseDAOCorrectAfterSingletonInitialization() {
        assertEquals(factory.getCourseDAO(), Core.getCourseDAO());
    }

    @Test
    public void canSetTaskRunner() {
        BackgroundTaskRunner runner = mock(BackgroundTaskRunner.class);
        Core.setTaskRunner(runner);
        assertEquals(runner, Core.getTaskRunner());
    }

    @Test
    public void projectDAOCorrectAfterSingletonInitialization() {
        assertEquals(factory.getProjectDAO(), Core.getProjectDAO());
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
