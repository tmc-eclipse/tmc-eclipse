package fi.helsinki.cs.plugin.tmc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

public class CoreTest {
    Core core;

    @Before
    public void setUp() {
        core = Core.getInstance();
    }

    @Test
    public void getInstanceReturnSameObjectOnFurtherCalls() {
        core = Core.getInstance();
        assertEquals(core, Core.getInstance());
    }

    @Test
    public void settingsNotNullAfterSingletonInitialization() {
        assertNotNull(core.getSettings());
    }

    @Test
    public void coursesNotNullAfterSingletonInitialization() {
        assertNotNull(core.getCourseDAO());
    }

    @Test
    public void canSetErrorHandler() {
        TMCErrorHandler eh = mock(TMCErrorHandler.class);
        Core.setErrorHandler(eh);
        assertEquals(eh, Core.getErrorHandler());
    }
}
