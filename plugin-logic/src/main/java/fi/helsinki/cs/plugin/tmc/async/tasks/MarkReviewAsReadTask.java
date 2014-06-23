package fi.helsinki.cs.plugin.tmc.async.tasks;

import fi.helsinki.cs.plugin.tmc.async.BackgroundTask;
import fi.helsinki.cs.plugin.tmc.async.TaskFeedback;
import fi.helsinki.cs.plugin.tmc.domain.Review;
import fi.helsinki.cs.plugin.tmc.services.http.ServerManager;

public class MarkReviewAsReadTask extends BackgroundTask {

    private ServerManager server;
    private Review review;

    public MarkReviewAsReadTask(ServerManager server, Review review) {
        super("Marking review as read");
        this.server = server;
        this.review = review;
    }

    @Override
    public int start(TaskFeedback progress) {
        progress.startProgress(this.getDescription(), 1);

        boolean success = server.markReviewAsRead(review);
        progress.incrementProgress(1);

        return success ? RETURN_SUCCESS : RETURN_FAILURE;
    }
}
