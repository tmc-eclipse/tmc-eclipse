package fi.helsinki.cs.tmc.core.spyware.services;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class EventDeduplicaterTest {

    @Mock
    private EventReceiver receiver;
    @Mock
    private LoggableEvent event;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testReceiveEvent() {
        receiver.receiveEvent(event);
        Mockito.verify(receiver).receiveEvent(event);
    }
}
