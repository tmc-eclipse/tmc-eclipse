package fi.helsinki.cs.tmc.core.spyware.async;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.common.collect.Iterables;

import fi.helsinki.cs.tmc.core.spyware.services.EventStore;
import fi.helsinki.cs.tmc.core.spyware.services.LoggableEvent;

public class SavingTask implements Runnable {
    private ArrayDeque<LoggableEvent> sendQueue;
    private EventStore eventStore;
    
    public SavingTask(ArrayDeque<LoggableEvent> sendQueue, EventStore eventStore) {
        this.sendQueue = sendQueue;
        this.eventStore = eventStore; 
    }
    
    @Override
    public void run() {

        try {
            LoggableEvent[] eventsToSave;
            synchronized (sendQueue) {
                eventsToSave = Iterables.toArray(sendQueue, LoggableEvent.class);
            }
            eventStore.save(eventsToSave);
        } catch (IOException ex) {
            Logger.getLogger(SavingTask.class.getName()).log(Level.WARNING, "Failed to save events", ex);
        }
    }
}
