package fi.helsinki.cs.tmc.core.async.listeners;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import fi.helsinki.cs.tmc.core.async.tasks.FetchCodeReviewsTask;
import fi.helsinki.cs.tmc.core.domain.Review;
import fi.helsinki.cs.tmc.core.services.ReviewDAO;
import fi.helsinki.cs.tmc.core.ui.IdeUIInvoker;

public class FetchCodeReviewsTaskListenerTest {
    private FetchCodeReviewsTask task;
    private IdeUIInvoker invoker;
    private ReviewDAO reviewDAO;
    private FetchCodeReviewsTaskListener listener;

    private Object[] mocks;

    @Before
    public void setUp() throws Exception {
        task = mock(FetchCodeReviewsTask.class);
        invoker = mock(IdeUIInvoker.class);
        reviewDAO = mock(ReviewDAO.class);
        listener = new FetchCodeReviewsTaskListener(task, invoker, reviewDAO, true);

        mocks = new Object[] {task, invoker, reviewDAO};
    }

    @Test
    public void onBeginDoesNothing() {
        listener.onBegin();
        verifyZeroInteractions(mocks);
    }

    @Test
    public void onFailureDoesNothing() {
        listener.onFailure();
        verifyZeroInteractions(mocks);
    }

    @Test
    public void onSuccessFetchesAndShowAndMarksAsReadAllUnseenReviews() {
        List<Review> reviews = new ArrayList<Review>();
        Review r1 = new Review();
        r1.setMarkedAsRead(false);
        reviews.add(r1);
        Review r2 = new Review();
        r2.setMarkedAsRead(false);
        reviews.add(r2);
        
        when(reviewDAO.unseen()).thenReturn(reviews);

        listener.onSuccess();

        verify(reviewDAO, times(1)).unseen();
        verify(invoker, times(2)).invokeCodeReviewDialog(any(Review.class));
        verifyNoMoreInteractions(invoker);
        assertTrue(r1.isMarkedAsRead());
        assertTrue(r2.isMarkedAsRead());
    }

    @Test
    public void onSuccessRaisesMessageIfNoUnseenMessagesAndShowMessagesIsTrue() {
        when(reviewDAO.unseen()).thenReturn(new ArrayList<Review>());
        listener.onSuccess();
        verify(invoker, times(1)).invokeMessageBox("No new code reviews.");
        verifyNoMoreInteractions(invoker);
    }

    @Test
    public void onSuccessDoesNotRaiseMessageIfNoUnseenMessagesAndShowMessagesIsFalse() {
        listener = new FetchCodeReviewsTaskListener(task, invoker, reviewDAO, false);
        when(reviewDAO.unseen()).thenReturn(new ArrayList<Review>());
        listener.onSuccess();
        verifyZeroInteractions(invoker);
    }
}
