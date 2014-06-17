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
import fi.helsinki.cs.plugin.tmc.services.CourseDAO;
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

    private SingletonTask sendingSingletonTask;
    private SingletonTask savingTask;

    private final ScheduledExecutorService scheduler;
    // private ServerAccess serverAccess;
    // private Courses courses;
    private final EventStore eventStore;
    private final Settings settings;
    private final ServerManager serverManager;
    private final CourseDAO courseDAO;

    // The following variables must only be accessed with a lock on sendQueue.
    private final ArrayDeque<LoggableEvent> sendQueue = new ArrayDeque<LoggableEvent>();
    private int maxEvents = DEFAULT_MAX_EVENTS;
    private int autosendThreshold = DEFAULT_AUTOSEND_THREHSOLD;
    private Cooldown autosendCooldown;
    private SendingTask sendingTask;

    public EventSendBuffer(EventStore store, Settings settings, ServerManager serverManager, CourseDAO courseDAO) {
        this.eventStore = store;
        this.settings = settings;
        this.serverManager = serverManager;
        this.courseDAO = courseDAO;

        scheduler = Executors.newScheduledThreadPool(2);
        this.autosendCooldown = new Cooldown(DEFAULT_AUTOSEND_COOLDOWN);
        initializeTasks();

        try {
            List<LoggableEvent> initialEvents = Arrays.asList(eventStore.load());
            initialEvents = initialEvents.subList(0, Math.min(maxEvents, initialEvents.size()));
            this.sendQueue.addAll(initialEvents);
        } catch (IOException ex) {
            log.log(Level.WARNING, "Failed to read events from event store", ex);
        } catch (RuntimeException ex) {
            log.log(Level.WARNING, "Failed to read events from event store", ex);
        }

        this.sendingSingletonTask.setInterval(DEFAULT_SEND_INTERVAL);
        this.savingTask.setInterval(DEFAULT_SAVE_INTERVAL);
    }

    private final void initializeTasks() {
        savingTask = new SingletonTask(new SavingTask(sendQueue, eventStore), scheduler);

        this.sendingTask = new SendingTask(sendQueue, serverManager, courseDAO, settings, savingTask);
        sendingSingletonTask = new SingletonTask(sendingTask, scheduler);

    }

    public void setSendingInterval(long interval) {
        sendingSingletonTask.setInterval(interval);
    }

    public void setSavingInterval(long interval) {
        savingTask.setInterval(interval);
    }

    public void setMaxEvents(int newMaxEvents) {
        if (newMaxEvents <= 0) {
            throw new IllegalArgumentException();
        }

        synchronized (sendQueue) {
            if (newMaxEvents < maxEvents) {
                int diff = newMaxEvents - maxEvents;
                for (int i = 0; i < diff; ++i) {
                    sendQueue.pop();
                }
                sendingTask.setEventsToRemoveAfterSend(sendingTask.getEventsToRemoveAfterSend() -diff);
            }

            maxEvents = newMaxEvents;
        }
    }

    public void setAutosendThreshold(int autosendThreshold) {
        synchronized (sendQueue) {
            if (autosendThreshold <= 0) {
                throw new IllegalArgumentException();
            }
            this.autosendThreshold = autosendThreshold;

            maybeAutosend();
        }
    }

    public void setAutosendCooldown(long durationMillis) {
        this.autosendCooldown.setDurationMillis(durationMillis);
    }

    public void sendNow() {
        sendingSingletonTask.start();
    }

    public void saveNow(long timeout) throws TimeoutException, InterruptedException {
        savingTask.start();
        savingTask.waitUntilFinished(timeout);
    }

    public void waitUntilCurrentSendingFinished(long timeout) throws TimeoutException, InterruptedException {
        sendingSingletonTask.waitUntilFinished(timeout);
    }

    @Override
    public void receiveEvent(LoggableEvent event) {
        if (!settings.isSpywareEnabled()) {
            return;
        }

        synchronized (sendQueue) {
            if (sendQueue.size() >= maxEvents) {
                sendQueue.pop();
                sendingTask.setEventsToRemoveAfterSend(sendingTask.getEventsToRemoveAfterSend() - 1);
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
            sendingSingletonTask.unsetInterval();
            savingTask.unsetInterval();

            savingTask.waitUntilFinished(delayPerWait);
            savingTask.start();
            savingTask.waitUntilFinished(delayPerWait);
            sendingSingletonTask.waitUntilFinished(delayPerWait);

        } catch (TimeoutException ex) {
            log.log(Level.WARNING, "Time out when closing EventSendBuffer", ex);
        } catch (InterruptedException ex) {
            log.log(Level.WARNING, "Closing EventSendBuffer interrupted", ex);
        }

    }

}