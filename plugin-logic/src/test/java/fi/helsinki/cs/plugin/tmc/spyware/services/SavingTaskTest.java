package fi.helsinki.cs.plugin.tmc.spyware.services;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;
import static org.mockito.Matchers.*;
import static org.mockito.Spy.*;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.regex.Matcher;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.exceptions.base.MockitoException;

import com.google.common.collect.Iterables;

import fi.helsinki.cs.plugin.tmc.io.IO;
import fi.helsinki.cs.plugin.tmc.spyware.async.SavingTask;

public class SavingTaskTest {
    private SavingTask task;
    private ArrayDeque<LoggableEvent> sendQueue;
    private EventStore eventStore;

    @Before
    public void setUp() throws IOException {
        this.eventStore = mock(EventStore.class);

        this.sendQueue = new ArrayDeque<LoggableEvent>();
        this.sendQueue.add(new LoggableEvent("courseA", "ExerciseA", "eventTypeA", new byte[10]));
        this.sendQueue.add(new LoggableEvent("courseB", "ExerciseB", "eventTypeB", new byte[11]));
        this.sendQueue.add(new LoggableEvent("courseC", "ExerciseC", "eventTypeC", new byte[12]));

        this.task = new SavingTask(sendQueue, eventStore);

        doThrow(new MockitoException("Success")).when(eventStore).save(
                Iterables.toArray(sendQueue, LoggableEvent.class));

    }

    @Test(expected = MockitoException.class)
    public void runTest() throws IOException {
        task.run();
    }

}
