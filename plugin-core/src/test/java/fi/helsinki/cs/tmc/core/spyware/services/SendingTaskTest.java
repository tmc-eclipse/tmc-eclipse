package fi.helsinki.cs.tmc.core.spyware.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import org.junit.Before;
import org.junit.Test;

import fi.helsinki.cs.tmc.core.async.tasks.SingletonTask;
import fi.helsinki.cs.tmc.core.domain.Course;
import fi.helsinki.cs.tmc.core.services.CourseDAO;
import fi.helsinki.cs.tmc.core.services.Settings;
import fi.helsinki.cs.tmc.core.services.http.ServerManager;
import fi.helsinki.cs.tmc.core.spyware.async.SavingTask;
import fi.helsinki.cs.tmc.core.spyware.async.SendingTask;
import fi.helsinki.cs.tmc.core.spyware.services.EventStore;
import fi.helsinki.cs.tmc.core.spyware.services.LoggableEvent;
import fi.helsinki.cs.tmc.core.spyware.services.SharedInteger;

public class SendingTaskTest {
    private SendingTask task;
    private ArrayDeque<LoggableEvent> sendQueue;
    private ServerManager serverManager;
    private CourseDAO courseDAO;
    private Settings settings;
    private SingletonTask savingTask;
    private EventStore eventStore;
    private SharedInteger eventsToRemoveAfterSend;

    @Before
    public void setUp() throws Exception {
        initializeSendQueue();
        initializeServerManager();
        initializeSettings();
        initializeCourseDAO();
        initializeEventStore();

        this.eventsToRemoveAfterSend = new SharedInteger();
        this.savingTask = new SingletonTask(new SavingTask(sendQueue, eventStore), Executors.newScheduledThreadPool(2));
        this.task = new SendingTask(sendQueue, serverManager, courseDAO, settings, savingTask, eventsToRemoveAfterSend);
    }

    @Test
    public void runWhenThereIsNoUrlsTest() throws InterruptedException {
        task.run();
        assertEquals(sendQueue.size(), 5);
    }
    
    @Test
    public void runWhenSpywareIsDisabledTest() throws InterruptedException {
        when(settings.isSpywareEnabled()).thenReturn(false);
        
        for (Course c : courseDAO.getCourses()) {
            List<String> l = new ArrayList<String>();
            l.add("a");
            l.add("b");
            l.add("c");
            c.setSpywareUrls(l);
        }
        
        task.run();

        assertEquals(sendQueue.size(), 5);
    }

    @Test
    public void runTest() { 
        for (Course c : courseDAO.getCourses()) {
            List<String> l = new ArrayList<String>();
            l.add("a");
            l.add("b");
            l.add("c");
            c.setSpywareUrls(l);
        }

        task.run();

        assertEquals(0, eventsToRemoveAfterSend.i);
    }

    private void initializeEventStore() {
        this.eventStore = mock(EventStore.class);
    }

    private void initializeCourseDAO() {
        courseDAO = mock(CourseDAO.class);
        List<Course> l = new ArrayList<Course>();
        l.add(new Course("Course1"));
        l.add(new Course("Course2"));
        l.add(new Course("Course3"));
        l.add(new Course("Course4"));
        l.add(new Course("Course5"));

        when(courseDAO.getCourses()).thenReturn(l);
        when(courseDAO.getCurrentCourse(any(Settings.class))).thenReturn(l.get(1));
    }

    private void initializeServerManager() {
        this.serverManager = mock(ServerManager.class);
    }

    private void initializeSettings() {
        this.settings = mock(Settings.class);
        when(settings.isSpywareEnabled()).thenReturn(true);
    }

    private void initializeSendQueue() {
        this.sendQueue = new ArrayDeque<LoggableEvent>();
        this.sendQueue.add(new LoggableEvent("course1", "exercise1", "file_change", new byte[10], "metadata1"));
        this.sendQueue.add(new LoggableEvent("course2", "exercise2", "file_change", new byte[10], "metadata2"));
        this.sendQueue.add(new LoggableEvent("course3", "exercise3", "file_change", new byte[10], "metadata3"));
        this.sendQueue.add(new LoggableEvent("course4", "exercise4", "file_change", new byte[10], "metadata4"));
        this.sendQueue.add(new LoggableEvent("course5", "exercise5", "file_change", new byte[10], "metadata5"));
    }
}
