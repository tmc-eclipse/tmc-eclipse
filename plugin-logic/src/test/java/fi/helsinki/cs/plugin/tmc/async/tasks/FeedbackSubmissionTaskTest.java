package fi.helsinki.cs.plugin.tmc.async.tasks;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import fi.helsinki.cs.plugin.tmc.async.BackgroundTask;
import fi.helsinki.cs.plugin.tmc.async.TaskFeedback;
import fi.helsinki.cs.plugin.tmc.domain.FeedbackAnswer;
import fi.helsinki.cs.plugin.tmc.services.FeedbackAnswerSubmitter;
import fi.helsinki.cs.plugin.tmc.ui.IdeUIInvoker;

public class FeedbackSubmissionTaskTest {
    private FeedbackAnswerSubmitter submitter;
    private FeedbackSubmissionTask task;
    private List<FeedbackAnswer> answers;
    private String url;
    private IdeUIInvoker invoker;

    @Before
    public void setup() {
        submitter = mock(FeedbackAnswerSubmitter.class);
        url = "mockUrl";
        answers = new ArrayList<FeedbackAnswer>();

        invoker = mock(IdeUIInvoker.class);
        task = new FeedbackSubmissionTask(submitter, answers, url, invoker);
    }

    @Test
    public void FeedbackAnswerSubmitterIsCalledWhenTaskIsRun() {
        task.start(mock(TaskFeedback.class));
        verify(submitter, times(1)).submitFeedback(answers, url);
    }

    @Test
    public void FeedbackAnswerSubmitterReturnsSuccess() {
        assertEquals(BackgroundTask.RETURN_SUCCESS, task.start(mock(TaskFeedback.class)));

    }

    @Test
    public void feedbackAnswerSubmitterCallsErrorHandlerAndReturnsFalseOnException() {
        Mockito.doThrow(new RuntimeException("Error message here")).when(submitter).submitFeedback(answers, url);

        assertEquals(BackgroundTask.RETURN_FAILURE, task.start(mock(TaskFeedback.class)));
        verify(invoker, times(1)).raiseVisibleException(
                "An error occured while submitting feedback:\nError message here");
    }

}
