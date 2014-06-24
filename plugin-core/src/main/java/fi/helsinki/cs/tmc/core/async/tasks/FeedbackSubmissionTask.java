package fi.helsinki.cs.tmc.core.async.tasks;

import java.util.List;

import fi.helsinki.cs.tmc.core.async.BackgroundTask;
import fi.helsinki.cs.tmc.core.async.TaskFeedback;
import fi.helsinki.cs.tmc.core.domain.FeedbackAnswer;
import fi.helsinki.cs.tmc.core.services.FeedbackAnswerSubmitter;
import fi.helsinki.cs.tmc.core.ui.IdeUIInvoker;

/**
 * 
 * This is the background task for feedback submission.
 * 
 */
public class FeedbackSubmissionTask extends BackgroundTask {
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
        super("Submitting feedback");

        this.submitter = submitter;
        this.answers = answers;
        this.feedbackUrl = feedbackUrl;
        this.invoker = invoker;
    }

    @Override
    public int start(TaskFeedback progress) {
        progress.startProgress(this.getDescription(), 1);
        try {
            submitter.submitFeedback(answers, feedbackUrl);
            progress.incrementProgress(1);
        } catch (Exception ex) {
            invoker.raiseVisibleException("An error occured while submitting feedback:\n" + ex.getMessage());
            return BackgroundTask.RETURN_FAILURE;
        }
        return BackgroundTask.RETURN_SUCCESS;
    }

    @Override
    public void stop() {
        // we can't stop here, it's bat country
    }
}
