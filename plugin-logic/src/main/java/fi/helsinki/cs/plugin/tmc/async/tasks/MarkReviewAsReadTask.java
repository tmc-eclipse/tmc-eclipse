package fi.helsinki.cs.plugin.tmc.async.tasks;

import fi.helsinki.cs.plugin.tmc.async.BackgroundTask;
import fi.helsinki.cs.plugin.tmc.async.TaskFeedback;
import fi.helsinki.cs.plugin.tmc.domain.Review;
import fi.helsinki.cs.plugin.tmc.services.http.ServerManager;

public class MarkReviewAsReadTask implements BackgroundTask {
    private String description = "Marking review as read";

    private ServerManager server;
    private Review review;

    public MarkReviewAsReadTask(ServerManager server, Review review) {
        this.server = server;
        this.review = review;
    }

    @Override
    public int start(TaskFeedback progress) {
        progress.startProgress("Marking review as read", 1);

        boolean success = server.markReviewAsRead(review);
        progress.incrementProgress(1);

        return success ? RETURN_SUCCESS : RETURN_FAILURE;
    }

    @Override
    public void stop() {
        // TODO Auto-generated method stub

    }

    @Override
    public String getDescription() {
        return this.description;
    }

}
