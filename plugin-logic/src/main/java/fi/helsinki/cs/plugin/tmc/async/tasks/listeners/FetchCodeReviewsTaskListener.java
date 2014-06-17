package fi.helsinki.cs.plugin.tmc.async.tasks.listeners;

import fi.helsinki.cs.plugin.tmc.async.BackgroundTaskListener;
import fi.helsinki.cs.plugin.tmc.async.tasks.FetchCodeReviewsTask;
import fi.helsinki.cs.plugin.tmc.domain.Review;
import fi.helsinki.cs.plugin.tmc.services.ReviewDAO;
import fi.helsinki.cs.plugin.tmc.ui.IdeUIInvoker;

public class FetchCodeReviewsTaskListener implements BackgroundTaskListener {
    private FetchCodeReviewsTask task;
    private IdeUIInvoker invoker;
    private ReviewDAO reviewDAO;

    public FetchCodeReviewsTaskListener(FetchCodeReviewsTask task, IdeUIInvoker invoker, ReviewDAO reviewDAO) {
        this.task = task;
        this.invoker = invoker;
        this.reviewDAO = reviewDAO;
    }

    @Override
    public void onBegin() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSuccess() {
        for (Review r : reviewDAO.all()) {
            if (!r.isMarkedAsRead()) {
                r.setMarkedAsRead(true);
                invoker.invokeCodeReviewDialog(r);
            }
        }
    }

    @Override
    public void onFailure() {
        // TODO Auto-generated method stub
    }

}
