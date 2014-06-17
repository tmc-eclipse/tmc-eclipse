package fi.helsinki.cs.plugin.tmc.async.tasks;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import fi.helsinki.cs.plugin.tmc.async.BackgroundTask;
import fi.helsinki.cs.plugin.tmc.async.TaskFeedback;
import fi.helsinki.cs.plugin.tmc.domain.Course;
import fi.helsinki.cs.plugin.tmc.domain.Review;
import fi.helsinki.cs.plugin.tmc.services.ReviewDAO;
import fi.helsinki.cs.plugin.tmc.services.http.ServerManager;

public class FetchCodeReviewsTaskTest {
    private ServerManager server;
    private ReviewDAO reviewDAO;
    private Course course;

    private TaskFeedback progress;
    private FetchCodeReviewsTask task;

    @Before
    public void setUp() throws Exception {
        server = mock(ServerManager.class);
        reviewDAO = mock(ReviewDAO.class);
        course = mock(Course.class);

        progress = mock(TaskFeedback.class);
        task = new FetchCodeReviewsTask(course, server, reviewDAO);
    }

    @Test
    public void feedbackIsInitializedCorrectly() {
        task.start(progress);
        verify(progress, times(1)).startProgress("Checking for new code reviews", 2);
    }

    @Test
    public void fetchGetsReviewsFromServerAndIncrementsProgressByOne() {
        task.start(progress);
        verify(server, times(1)).downloadReviews(course);
        verify(progress, atLeast(1)).incrementProgress(1);
    }

    @Test
    public void taskReportsFailureIfFetchedReviewsListIsNull() {
        when(server.downloadReviews(course)).thenReturn(null);

        assertEquals(BackgroundTask.RETURN_FAILURE, task.start(progress));
    }

    @Test
    public void taskAddReviewsToReviewDAOAfterSuccefullFetchAndReturnSuccess() {
        List<Review> reviews = new ArrayList<Review>();
        when(server.downloadReviews(course)).thenReturn(reviews);

        assertEquals(BackgroundTask.RETURN_SUCCESS, task.start(progress));
        verify(reviewDAO, times(1)).addAll(reviews);
        verify(progress, times(2)).incrementProgress(1);
    }

    @Test
    public void hasCorrectDescription() {
        assertEquals("Checking for new code reviews", task.getDescription());
    }

    @Test
    public void stopDoesNothing() {
        task.stop();
        verifyNoMoreInteractions(server, reviewDAO, course);
    }
}
