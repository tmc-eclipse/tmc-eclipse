package fi.helsinki.cs.tmc.core.async.tasks;

import fi.helsinki.cs.tmc.core.async.BackgroundTask;
import fi.helsinki.cs.tmc.core.async.TaskFeedback;
import fi.helsinki.cs.tmc.core.domain.Review;
import fi.helsinki.cs.tmc.core.services.http.ServerManager;

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
