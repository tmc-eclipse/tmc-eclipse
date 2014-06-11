package fi.helsinki.cs.plugin.tmc.spyware.services;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import fi.helsinki.cs.plugin.tmc.domain.Exercise;

public class LoggableEventTest {

    private LoggableEvent le1;
    private LoggableEvent le2;

    private byte[] b = {1, 1, 1};

    @Before
    public void setUp() throws Exception {
        le1 = new LoggableEvent(Mockito.mock(Exercise.class), "a", b);
        le2 = new LoggableEvent(Mockito.mock(Exercise.class), "a", b, "b");
    }

    @Test
    public void testGetters() {
        assertEquals(le1.getData(), le2.getData());
        assertEquals(le2.getMetadata(), "b");
        assertEquals(le2.getHappenedAt(), System.currentTimeMillis());
        // assertEquals(le2.toString(), "LoggableEvent{" + "courseName=" +
        // courseName + ", exerciseName=" + exerciseName + ", eventType=" +
        // eventType + ", happenedAt=" + happenedAt + ", systemNanotime=" +
        // systemNanotime + ", key=" + key + ", metadata=" + metadata +
        // ", data=" + new String(data) + "}");
    }
}
