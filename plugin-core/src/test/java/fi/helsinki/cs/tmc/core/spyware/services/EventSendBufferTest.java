package fi.helsinki.cs.tmc.core.spyware.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.junit.Before;
import org.junit.Test;

import fi.helsinki.cs.tmc.core.async.tasks.SingletonTask;
import fi.helsinki.cs.tmc.core.services.Settings;
import fi.helsinki.cs.tmc.core.services.http.ServerManager;
import fi.helsinki.cs.tmc.core.spyware.async.SavingTask;
import fi.helsinki.cs.tmc.core.spyware.async.SendingTask;

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
        this.eventsToRemoveAfterSend = new SharedInteger();
        
        this.sendingTask = mock(SendingTask.class);
        this.savingTask = mock(SavingTask.class);

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
        this.buffer = new EventSendBuffer(store, settings, sendQueue, new SingletonTask(sendingTask, scheduler),
                new SingletonTask(savingTask, scheduler), eventsToRemoveAfterSend);

    }

    @Test
    public void DoesNotReceiveEventIfSpywareIsDisabled() {
        when(settings.isSpywareEnabled()).thenReturn(false);

        buffer.receiveEvent(new LoggableEvent("a", "a", "a", new byte[1], "a"));
        
        assertEquals(6, sendQueue.size());
    }
    
    @Test
    public void receiveEventTest() {
    	buffer.setSendingInterval(1);
    	buffer.receiveEvent(new LoggableEvent("a", "a", "a", new byte[1], "a"));
    	
    	try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	
    	assertEquals(7, sendQueue.size());
    	verify(sendingTask, atLeastOnce()).run();
    }
    
    @Test
    public void closeTest() {
    	buffer.setSavingInterval(1);
    	buffer.close();
    	
    	verify(savingTask, times(1)).run();
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
