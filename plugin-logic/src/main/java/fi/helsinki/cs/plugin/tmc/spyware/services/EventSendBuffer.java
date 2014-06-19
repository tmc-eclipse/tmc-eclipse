package fi.helsinki.cs.plugin.tmc.spyware.services;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

import fi.helsinki.cs.plugin.tmc.async.tasks.SingletonTask;
import fi.helsinki.cs.plugin.tmc.services.Settings;
import fi.helsinki.cs.plugin.tmc.services.http.ServerManager;
import fi.helsinki.cs.plugin.tmc.spyware.utility.Cooldown;

/**
 * Buffers {@link LoggableEvent}s and sends them to the server and/or syncs them
 * to the disk periodically.
 * 
 */
public class EventSendBuffer implements EventReceiver {
    private static final Logger log = Logger.getLogger(EventSendBuffer.class.getName());

    public static final long DEFAULT_SEND_INTERVAL = 3 * 60 * 1000;
    public static final long DEFAULT_SAVE_INTERVAL = 1 * 60 * 1000;
    public static final int DEFAULT_MAX_EVENTS = 64 * 1024;
    public static final int DEFAULT_AUTOSEND_THREHSOLD = DEFAULT_MAX_EVENTS / 2;
    public static final int DEFAULT_AUTOSEND_COOLDOWN = 30 * 1000;

    private SingletonTask savingTask;
    private SingletonTask sendingTask;
    
    // private ServerAccess serverAccess;
    // private Courses courses;
    private final EventStore eventStore;
    private final Settings settings;
    

    // The following variables must only be accessed with a lock on sendQueue.
    private ArrayDeque<LoggableEvent> sendQueue;
    private int maxEvents = DEFAULT_MAX_EVENTS;
    private int autosendThreshold = DEFAULT_AUTOSEND_THREHSOLD;
    private Cooldown autosendCooldown;
    private SharedInteger eventsToRemoveAfterSend;

    public EventSendBuffer(EventStore store, Settings settings, ArrayDeque<LoggableEvent> sendQueue, SingletonTask sendingTask,
            SingletonTask savingTask, SharedInteger eventsToRemoveAfterSend) {
        this.eventStore = store;
        this.settings = settings;
        this.sendQueue = sendQueue;
        this.eventsToRemoveAfterSend = eventsToRemoveAfterSend;
        
        this.autosendCooldown = new Cooldown(DEFAULT_AUTOSEND_COOLDOWN);
        
        this.sendingTask = sendingTask;
        this.savingTask = savingTask;

        try {
            List<LoggableEvent> initialEvents = Arrays.asList(eventStore.load());
            initialEvents = initialEvents.subList(0, Math.min(maxEvents, initialEvents.size()));
            this.sendQueue.addAll(initialEvents);
        } catch (IOException ex) {
            log.log(Level.WARNING, "Failed to read events from event store", ex);
        } catch (RuntimeException ex) {
            log.log(Level.WARNING, "Failed to read events from event store", ex);
        }

        this.sendingTask.setInterval(DEFAULT_SEND_INTERVAL);
        this.savingTask.setInterval(DEFAULT_SAVE_INTERVAL);
    }

    public void setSendingInterval(long interval) {
        sendingTask.setInterval(interval);
    }

    public void setSavingInterval(long interval) {
        savingTask.setInterval(interval);
    }

    private void sendNow() {
        sendingTask.start();
    }

    public void waitUntilCurrentSendingFinished(long timeout) throws TimeoutException, InterruptedException {
        sendingTask.waitUntilFinished(timeout);
    }

    @Override
    public void receiveEvent(LoggableEvent event) {
        if (!settings.isSpywareEnabled()) {
            return;
        }

        synchronized (sendQueue) {
            if (sendQueue.size() >= maxEvents) {
                sendQueue.pop();
                eventsToRemoveAfterSend.i--;
            }
            sendQueue.add(event);

            maybeAutosend();
        }
    }

    private void maybeAutosend() {
        if (sendQueue.size() >= autosendThreshold && autosendCooldown.isExpired()) {
            autosendCooldown.start();
            sendNow();
        }
    }

    /**
     * Stops sending any more events.
     * 
     * Buffer manipulation methods may still be called.
     */
    @Override
    public void close() {
        long delayPerWait = 2000;

        try {
            sendingTask.unsetInterval();
            savingTask.unsetInterval();

            savingTask.waitUntilFinished(delayPerWait);
            savingTask.start();
            savingTask.waitUntilFinished(delayPerWait);
            sendingTask.waitUntilFinished(delayPerWait);

        } catch (TimeoutException ex) {
            log.log(Level.WARNING, "Time out when closing EventSendBuffer", ex);
        } catch (InterruptedException ex) {
            log.log(Level.WARNING, "Closing EventSendBuffer interrupted", ex);
        }

    }

}