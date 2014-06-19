package fi.helsinki.cs.plugin.tmc.spyware.async;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import fi.helsinki.cs.plugin.tmc.async.tasks.SingletonTask;
import fi.helsinki.cs.plugin.tmc.domain.Course;
import fi.helsinki.cs.plugin.tmc.services.CourseDAO;
import fi.helsinki.cs.plugin.tmc.services.Settings;
import fi.helsinki.cs.plugin.tmc.services.http.ServerManager;
import fi.helsinki.cs.plugin.tmc.spyware.services.LoggableEvent;
import fi.helsinki.cs.plugin.tmc.spyware.services.SharedInteger;

public class SendingTask implements Runnable {
    private static final Logger log = Logger.getLogger(SendingTask.class.getName());
    private static final int MAX_EVENTS_PER_SEND = 500;

    private ArrayDeque<LoggableEvent> sendQueue;
    private ServerManager serverManager;
    private CourseDAO courseDAO;
    private Settings settings;
    private Random random;
    private SharedInteger eventsToRemoveAfterSend;
    
    private SingletonTask savingTask;

    public SendingTask(ArrayDeque<LoggableEvent> sendQueue, ServerManager serverManager, CourseDAO courseDAO,
            Settings settings, SingletonTask savingTask, SharedInteger eventsToRemoveAfterSend) {
        this.sendQueue = sendQueue;
        this.serverManager = serverManager;
        this.courseDAO = courseDAO;
        this.settings = settings;
        this.savingTask = savingTask;
        this.random = new Random();
        this.eventsToRemoveAfterSend = eventsToRemoveAfterSend;
    }

    @Override
    public void run() {
        if (!settings.isSpywareEnabled()) {
            return;
        }
        
        boolean shouldSendMore;

        do {
            ArrayList<LoggableEvent> eventsToSend = copyEventsToSendFromQueue();
            if (eventsToSend.isEmpty()) {
                return;
            }

            synchronized (sendQueue) {
                shouldSendMore = sendQueue.size() > eventsToSend.size();
            }

            String url = pickDestinationUrl();
            if (url == null) {
                return;
            }

            log.log(Level.INFO, "Sending {0} events to {1}", new Object[] {eventsToSend.size(), url});

            doSend(eventsToSend, url);
        } while (shouldSendMore);
    }

    private ArrayList<LoggableEvent> copyEventsToSendFromQueue() {
        synchronized (sendQueue) {
            ArrayList<LoggableEvent> eventsToSend = new ArrayList<LoggableEvent>(sendQueue.size());

            Iterator<LoggableEvent> i = sendQueue.iterator();
            while (i.hasNext() && eventsToSend.size() < MAX_EVENTS_PER_SEND) {
                eventsToSend.add(i.next());
            }

            eventsToRemoveAfterSend.i = eventsToSend.size();

            return eventsToSend;
        }
    }

    private String pickDestinationUrl() {

        Course course = courseDAO.getCurrentCourse(settings);
        if (course == null) {
            log.log(Level.FINE, "Not sending events because no course selected");
            return null;
        }

        List<String> urls = course.getSpywareUrls();
        if (urls == null || urls.isEmpty()) {
            log.log(Level.INFO, "Not sending events because no URL provided by server");
            return null;
        }

        String url = urls.get(random.nextInt(urls.size()));

        return url;

        // url for localhost debugging, assuming spyware server
        // runs at port 3101
        // return "http://127.0.0.1:3101";
    }

    private void doSend(final ArrayList<LoggableEvent> eventsToSend, final String url) {

        try {
            serverManager.sendEventLogs(url, eventsToSend, settings);
            log.log(Level.INFO, "Sent {0} events successfully to {1}", new Object[] {eventsToSend.size(), url});

        } catch (Exception ex) {
            log.log(Level.INFO, "Failed to send {0} events to {1}: " + ex.getMessage(),
                    new Object[] {eventsToSend.size(), url});
            return;
        }
        removeSentEventsFromQueue();

        // If saving fails now (or is already running and fails
        // later) then we may end up sending duplicate events
        // later. This will hopefully be very rare.
        savingTask.start();
    }

    private void removeSentEventsFromQueue() {
        synchronized (sendQueue) {
            assert (eventsToRemoveAfterSend.i <= sendQueue.size());
            while (eventsToRemoveAfterSend.i > 0) {
                sendQueue.pop();
                eventsToRemoveAfterSend.i--;
            }
        }
    }
}
