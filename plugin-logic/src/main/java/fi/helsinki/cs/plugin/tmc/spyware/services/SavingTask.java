package fi.helsinki.cs.plugin.tmc.spyware.services;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.common.collect.Iterables;

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
