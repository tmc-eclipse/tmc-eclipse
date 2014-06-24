package fi.helsinki.cs.tmc.core.async.listeners;

import java.util.List;

import fi.helsinki.cs.tmc.core.async.BackgroundTaskListener;
import fi.helsinki.cs.tmc.core.async.tasks.FetchCodeReviewsTask;
import fi.helsinki.cs.tmc.core.domain.Review;
import fi.helsinki.cs.tmc.core.services.ReviewDAO;
import fi.helsinki.cs.tmc.core.ui.IdeUIInvoker;

public class FetchCodeReviewsTaskListener implements BackgroundTaskListener {
    private FetchCodeReviewsTask task;
    private IdeUIInvoker invoker;
    private ReviewDAO reviewDAO;
    private boolean showMessages;

    public FetchCodeReviewsTaskListener(FetchCodeReviewsTask task, IdeUIInvoker invoker, ReviewDAO reviewDAO,
            boolean showMessages) {
        this.task = task;
        this.invoker = invoker;
        this.reviewDAO = reviewDAO;
        this.showMessages = showMessages;
    }

    @Override
    public void onBegin() {
    }

    @Override
    public void onSuccess() {
        List<Review> unseen = reviewDAO.unseen();
        if (unseen.isEmpty() && showMessages) {
            invoker.invokeMessageBox("No new code reviews.");
            return;
        } else {
            for (Review r : unseen) {
                r.setMarkedAsRead(true);
                invoker.invokeCodeReviewDialog(r);
            }
        }
    }

    @Override
    public void onFailure() {
    }

    @Override
    public void onInterruption() {
    }

}
