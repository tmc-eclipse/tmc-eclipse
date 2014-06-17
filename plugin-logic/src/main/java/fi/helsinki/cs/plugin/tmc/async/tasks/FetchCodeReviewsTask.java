package fi.helsinki.cs.plugin.tmc.async.tasks;

import java.util.List;

import fi.helsinki.cs.plugin.tmc.async.BackgroundTask;
import fi.helsinki.cs.plugin.tmc.async.TaskFeedback;
import fi.helsinki.cs.plugin.tmc.domain.Course;
import fi.helsinki.cs.plugin.tmc.domain.Review;
import fi.helsinki.cs.plugin.tmc.services.ReviewDAO;
import fi.helsinki.cs.plugin.tmc.services.http.ServerManager;

public class FetchCodeReviewsTask implements BackgroundTask {
    private Course course;
    private ServerManager server;
    private ReviewDAO reviewDAO;
    
    private List<Review> reviews;
    private String description = "Checking for new code reviews";

    public FetchCodeReviewsTask(Course course, ServerManager server, ReviewDAO reviewDAO) {
        this.course = course;
        this.server = server;
        this.reviewDAO = reviewDAO;
    }

    @Override
    public int start(TaskFeedback progress) {
        progress.startProgress("Checking for new code reviews", 2);
        this.reviews = server.downloadReviews(course);
        progress.incrementProgress(1);

        if (this.reviews == null) {
            return RETURN_FAILURE;
        }

        this.reviewDAO.addAll(reviews);
        progress.incrementProgress(1);
        return RETURN_SUCCESS;
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
