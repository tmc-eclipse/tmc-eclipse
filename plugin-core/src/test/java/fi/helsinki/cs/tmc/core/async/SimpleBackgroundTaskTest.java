package fi.helsinki.cs.tmc.core.async;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class SimpleBackgroundTaskTest {

    private class Foo {
        void bar() {
        }
    }

    private SimpleBackgroundTask<Foo> task;
    private List<Foo> list;
    private TaskFeedback progress;

    @Before
    public void setUp() {
        list = new ArrayList<Foo>();
        list.add(mock(Foo.class));
        progress = mock(TaskFeedback.class);
        when(progress.isCancelRequested()).thenReturn(false);

        task = new SimpleBackgroundTask<Foo>("Description", list) {
            @Override
            public void run(Foo t) {
                t.bar(); // dummy method call for verifying object is used
            }

        };
    }

    @Test
    public void startReturnsSuccessOnSuccess() {
        assertEquals(BackgroundTask.RETURN_SUCCESS, task.start(progress));
    }

    @Test
    public void startProgressIsCalledWhenCallingStart() {
        task.start(progress);
        verify(progress, times(1)).startProgress(anyString(), anyInt());
    }

    @Test
    public void objectsInListAreUsed() {
        task.start(progress);
        for (Foo o : list) {
            verify(o, times(1)).bar();
        }
    }

    @Test
    public void incrementProgressIsCalled() {
        task.start(progress);
        for (Foo o : list) {
            verify(progress, times(list.size())).incrementProgress(1);
        }
    }

    @Test
    public void isCancelRequestedIsCalledOnEveryLoopIteration() {
        task.start(progress);
        verify(progress, times(list.size())).isCancelRequested();
    }

    @Test
    public void startReturnsFailureOnCancellation() {
        when(progress.isCancelRequested()).thenReturn(true);
        assertEquals(BackgroundTask.RETURN_INTERRUPTED, task.start(progress));
    }

    @Test
    public void objectsInListAreNotUsedIfProgressIsHaltedImmediately() {
        when(progress.isCancelRequested()).thenReturn(true);
        task.start(progress);

        for (Foo o : list) {
            verify(o, times(0)).bar();
        }
    }

    @Test
    public void incrementProgressIsNotCalledWhenIsHaltedImmediately() {
        when(progress.isCancelRequested()).thenReturn(true);
        task.start(progress);
        for (Foo o : list) {
            verify(progress, times(0)).incrementProgress(1);
        }
    }

    @Test
    public void objectsInListAreNotUsedIfStopIsCalledImmediately() {
        task.stop();
        task.start(progress);

        for (Foo o : list) {
            verify(o, times(0)).bar();
        }
    }

    @Test
    public void descriptionIsCorrect() {
        assertEquals("Description", task.getDescription());
    }

}
