package fi.helsinki.cs.plugin.tmc;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Field;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
        assertEquals(factory.getSettings(), core.getSettings());
    }

    @Test
    public void coursesCorrectAfterSingletonInitialization() {
        assertEquals(factory.getCourseDAO(), core.getCourseDAO());
    }

    @Test
    public void canSetErrorHandler() {
        TMCErrorHandler eh = mock(TMCErrorHandler.class);
        Core.setErrorHandler(eh);
        assertEquals(eh, Core.getErrorHandler());
    }
}
