package fi.helsinki.cs.plugin.tmc.spyware;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import fi.helsinki.cs.plugin.tmc.io.FileIO;
import fi.helsinki.cs.plugin.tmc.io.IO;
import fi.helsinki.cs.plugin.tmc.spyware.services.EventStore;
import fi.helsinki.cs.plugin.tmc.spyware.services.LoggableEvent;

class MockIO implements IO {

    StringWriter writer = new StringWriter();

    @Override
    public Writer getWriter() {
        try {
            writer.close();
        } catch (IOException e) {
        }
        writer = new StringWriter();
        return writer;
    }

    @Override
    public Reader getReader() {
        return new StringReader(writer.toString());
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getPath() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean fileExists() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean directoryExists() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public OutputStream getOutputStream() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public InputStream getInputStream() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void write(byte[] bytes) throws IOException {
        // TODO Auto-generated method stub

    }

    @Override
    public void createFolderTree(boolean onlyParents) {
        // TODO Auto-generated method stub

    }

    @Override
    public List<FileIO> getChildren() {
        // TODO Auto-generated method stub
        return null;
    }
}

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
