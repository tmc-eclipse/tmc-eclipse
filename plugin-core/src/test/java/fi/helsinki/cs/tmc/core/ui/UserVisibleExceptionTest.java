package fi.helsinki.cs.tmc.core.ui;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.Test;

public class UserVisibleExceptionTest {

    @Test
    public void messageIsSet() {
        assertEquals("msg", new UserVisibleException("msg").getMessage());
    }

    @Test
    public void messageAndCauseAreSet() {
        Throwable t = mock(Throwable.class);
        UserVisibleException e = new UserVisibleException("msg", t);
        assertEquals("msg", e.getMessage());
        assertEquals(t, e.getCause());
    }

}
