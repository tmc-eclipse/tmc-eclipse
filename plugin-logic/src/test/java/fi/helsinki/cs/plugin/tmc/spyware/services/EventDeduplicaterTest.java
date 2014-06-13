package fi.helsinki.cs.plugin.tmc.spyware.services;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import fi.helsinki.cs.plugin.tmc.spyware.services.EventReceiver;
import fi.helsinki.cs.plugin.tmc.spyware.services.LoggableEvent;

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
