package fi.helsinki.cs.plugin.tmc.async.tasks;

import java.util.List;

import fi.helsinki.cs.plugin.tmc.async.BackgroundTask;
import fi.helsinki.cs.plugin.tmc.async.TaskFeedback;
import fi.helsinki.cs.plugin.tmc.domain.FeedbackAnswer;
import fi.helsinki.cs.plugin.tmc.services.FeedbackAnswerSubmitter;
import fi.helsinki.cs.plugin.tmc.ui.IdeUIInvoker;

/**
 * 
 * This is the background task for feedback submission.
 * 
 */
public class FeedbackSubmissionTask implements BackgroundTask {

    private TaskFeedback progress;
    private FeedbackAnswerSubmitter submitter;
    private String feedbackUrl;
    private List<FeedbackAnswer> answers;

    private IdeUIInvoker invoker;

    /**
     * 
     * @param submitter
     *            The actual object that handles feedback submission
     * @param answers
     *            List of answers to feedback questions
     * @param feedbackUrl
     *            url where the feedback is posted
     * @param invoker
     *            An ide-specific object that allows us to invoke ide ui from
     *            core (in this case, error messages)
     */
    public FeedbackSubmissionTask(FeedbackAnswerSubmitter submitter, List<FeedbackAnswer> answers, String feedbackUrl,
            IdeUIInvoker invoker) {
        this.submitter = submitter;
        this.answers = answers;
        this.feedbackUrl = feedbackUrl;
        this.invoker = invoker;
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
            invoker.raiseVisibleException("An error occured while submitting feedback:\n" + ex.getMessage());
            return BackgroundTask.RETURN_FAILURE;
        }
        return BackgroundTask.RETURN_SUCCESS;
    }
}
