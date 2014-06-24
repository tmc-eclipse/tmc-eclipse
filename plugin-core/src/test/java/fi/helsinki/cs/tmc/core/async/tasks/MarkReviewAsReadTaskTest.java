package fi.helsinki.cs.tmc.core.async.tasks;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import fi.helsinki.cs.tmc.core.async.BackgroundTask;
import fi.helsinki.cs.tmc.core.async.TaskFeedback;
import fi.helsinki.cs.tmc.core.domain.Review;
import fi.helsinki.cs.tmc.core.services.http.ServerManager;

public class MarkReviewAsReadTaskTest {
    private ServerManager server;
    private Review review;
    
    private TaskFeedback progress;
    private MarkReviewAsReadTask task;

    @Before
    public void setUp() throws Exception {
        server = mock(ServerManager.class);
        review = mock(Review.class);
        progress = mock(TaskFeedback.class);
        task = new MarkReviewAsReadTask(server, review);
    }

    @Test
    public void hasCorrectDescription() {
        assertEquals("Marking review as read", task.getDescription());
    }

    @Test
    public void taskInitializesFeedbackCorrectly() {
        task.start(progress);
        verify(progress, times(1)).startProgress("Marking review as read", 1);
    }

    @Test
    public void taskCallsServerAndIncrementsProgress() {
        task.start(progress);
        verify(server, times(1)).markReviewAsRead(review);
        verify(progress, times(1)).incrementProgress(1);
    }

    @Test
    public void taskReturnsSuccessIfServerReturnsTrue() {
        when(server.markReviewAsRead(review)).thenReturn(true);
        assertEquals(BackgroundTask.RETURN_SUCCESS, task.start(progress));
    }

    @Test
    public void taskReturnsFailureIfServerReturnsFalse() {
        when(server.markReviewAsRead(review)).thenReturn(false);
        assertEquals(BackgroundTask.RETURN_FAILURE, task.start(progress));
    }

    @Test
    public void stopDoesNothing() {
        task.stop();
        verifyNoMoreInteractions(server, review);
    }

}
