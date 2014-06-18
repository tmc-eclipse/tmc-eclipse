package fi.helsinki.cs.plugin.tmc.spyware.services;

import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.junit.Before;
import org.junit.Test;

import fi.helsinki.cs.plugin.tmc.async.tasks.SingletonTask;
import fi.helsinki.cs.plugin.tmc.domain.Course;
import fi.helsinki.cs.plugin.tmc.services.CourseDAO;
import fi.helsinki.cs.plugin.tmc.services.Settings;
import fi.helsinki.cs.plugin.tmc.services.http.ServerManager;
import fi.helsinki.cs.plugin.tmc.storage.DataSource;

public class EventSendBufferTest {
    private EventSendBuffer buffer;
    private EventStore store;
    private Settings settings;
    private ServerManager serverManager;
    private ArrayDeque<LoggableEvent> sendQueue;
    private SharedInteger eventsToRemoveAfterSend;

    private SendingTask sendingTask;
    private SavingTask savingTask;

    @Before
    public void setUp() throws Exception {
        this.serverManager = mock(ServerManager.class);
        this.settings = mock(Settings.class);
        when(settings.isSpywareEnabled()).thenReturn(true);
        initializeEventStore();
        initializeSendQueue();
        this.serverManager = mock(ServerManager.class);
        this.eventsToRemoveAfterSend = new SharedInteger();
        //this.sendQueue = mock(ArrayDeque.class);

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
        this.buffer = new EventSendBuffer(store, settings, sendQueue, new SingletonTask(sendingTask, scheduler),
                new SingletonTask(savingTask, scheduler), eventsToRemoveAfterSend);

    }

    @Test
    public void DoesNotReceiveEventIfSpywareIsDisabled() {
        when(settings.isSpywareEnabled()).thenReturn(false);

        buffer.receiveEvent(new LoggableEvent("a", "a", "a", new byte[1], "a"));
        
        verify(sendQueue, times(0)).add(any(LoggableEvent.class));
    }

    private void initializeEventStore() throws IOException {
        this.store = mock(EventStore.class);
        LoggableEvent[] arr = new LoggableEvent[3];
        arr[0] = new LoggableEvent("course1", "exercise1", "eventType1", new byte[1], "metadata1");
        arr[1] = new LoggableEvent("course2", "exercise2", "eventType2", new byte[2], "metadata2");
        arr[2] = new LoggableEvent("course3", "exercise3", "eventType3", new byte[3], "metadata3");

        when(store.load()).thenReturn(arr);
    }

    private void initializeSendQueue() {
        this.sendQueue = new ArrayDeque<LoggableEvent>();
        sendQueue.add(new LoggableEvent("course1", "exercise1", "eventType1", new byte[1], "metadata1"));
        sendQueue.add(new LoggableEvent("course2", "exercise2", "eventType2", new byte[2], "metadata2"));
        sendQueue.add(new LoggableEvent("course3", "exercise3", "eventType3", new byte[3], "metadata3"));
    }
}
