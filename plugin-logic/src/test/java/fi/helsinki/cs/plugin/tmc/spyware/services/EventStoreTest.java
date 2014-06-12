package fi.helsinki.cs.plugin.tmc.spyware.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import fi.helsinki.cs.plugin.tmc.spyware.MockIO;
import fi.helsinki.cs.plugin.tmc.spyware.services.EventStore;
import fi.helsinki.cs.plugin.tmc.spyware.services.LoggableEvent;

public class EventStoreTest {
    private LoggableEvent[] events;
    private MockIO io;
    private EventStore store;

    @Before
    public void setUp() {
        io = new MockIO();
        store = new EventStore(io);

        events = new LoggableEvent[2];
        events[0] = new LoggableEvent("foo", "bar", "event1", null);
        events[1] = new LoggableEvent("foo2", "bar2", "event2", null);
    }

    @Test
    public void eventStoreSavesData() throws IOException {
        store.save(events);
        assertTrue(io.writer.toString().length() > 0);
    }

    @Test
    public void eventStoreClearsDataCorrectly() throws IOException {
        store.save(events);
        store.clear();
        assertTrue(io.writer.toString().length() == 0);
    }

    @Test
    public void eventStoreLoadsDataCorrectlyAfterSaving() throws IOException {
        store.save(events);
        events = null;
        events = store.load();
        assertEquals(2, events.length);

        assertEquals("foo", events[0].getCourseName());
        assertEquals("bar", events[0].getExerciseName());
        assertEquals("event1", events[0].getEventType());

        assertEquals("foo2", events[1].getCourseName());
        assertEquals("bar2", events[1].getExerciseName());
        assertEquals("event2", events[1].getEventType());

    }

    @Test
    public void eventStoreLoadReturnsZeroArrayIfNoDataIsSaved() throws IOException {
        events = store.load();
        assertEquals(0, events.length);
    }

}
