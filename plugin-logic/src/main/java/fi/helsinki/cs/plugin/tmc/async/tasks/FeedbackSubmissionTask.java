package fi.helsinki.cs.plugin.tmc.async.tasks;

import java.util.List;

import fi.helsinki.cs.plugin.tmc.Core;
import fi.helsinki.cs.plugin.tmc.async.BackgroundTask;
import fi.helsinki.cs.plugin.tmc.async.TaskFeedback;
import fi.helsinki.cs.plugin.tmc.domain.FeedbackAnswer;
import fi.helsinki.cs.plugin.tmc.services.FeedbackAnswerSubmitter;

public class FeedbackSubmissionTask implements BackgroundTask {

    private TaskFeedback progress;
    private FeedbackAnswerSubmitter submitter;
    private String feedbackUrl;
    private List<FeedbackAnswer> answers;

    public FeedbackSubmissionTask(FeedbackAnswerSubmitter submitter, List<FeedbackAnswer> answers, String feedbackUrl) {
        this.submitter = submitter;
        this.answers = answers;
        this.feedbackUrl = feedbackUrl;
    }

    @Override
    public int start(TaskFeedback progress) {
        this.progress = progress;

        return run();
    }

    @Override
    public void stop() {
        // we can't stop here, it's bat country
    }

    @Override
    public String getDescription() {

        return "Feedback Submission";
    }

    private int run() {
        try {
            submitter.submitFeedback(answers, feedbackUrl);
        } catch (Exception ex) {
            Core.getErrorHandler().raise("An error occured while submitting feedback:\n" + ex.getMessage());
            return BackgroundTask.RETURN_FAILURE;
        }
        return BackgroundTask.RETURN_SUCCESS;
    }
}
