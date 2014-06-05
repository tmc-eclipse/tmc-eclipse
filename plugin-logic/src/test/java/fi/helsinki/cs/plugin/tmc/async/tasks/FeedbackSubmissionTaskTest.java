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

import fi.helsinki.cs.plugin.tmc.Core;
import fi.helsinki.cs.plugin.tmc.TMCErrorHandler;
import fi.helsinki.cs.plugin.tmc.async.BackgroundTask;
import fi.helsinki.cs.plugin.tmc.domain.FeedbackAnswer;
import fi.helsinki.cs.plugin.tmc.services.FeedbackAnswerSubmitter;

public class FeedbackSubmissionTaskTest {
    private FeedbackAnswerSubmitter submitter;
    private FeedbackSubmissionTask task;
    private List<FeedbackAnswer> answers;
    private String url;

    @Before
    public void setup() {
        submitter = mock(FeedbackAnswerSubmitter.class);
        url = "mockUrl";
        answers = new ArrayList<FeedbackAnswer>();
        task = new FeedbackSubmissionTask(submitter, answers, url);
    }

    @Test
    public void FeedbackAnswerSubmitterIsCalledWhenTaskIsRun() {
        task.start(null);
        verify(submitter, times(1)).submitFeedback(answers, url);
    }

    @Test
    public void FeedbackAnswerSubmitterReturnsSuccess() {
        assertEquals(BackgroundTask.RETURN_SUCCESS, task.start(null));

    }

    @Test
    public void feedbackAnswerSubmitterCallsErrorHandlerAndReturnsFalseOnException() {
        Mockito.doThrow(new RuntimeException("Error message here")).when(submitter).submitFeedback(answers, url);

        TMCErrorHandler handler = mock(TMCErrorHandler.class);
        Core.setErrorHandler(handler);

        assertEquals(BackgroundTask.RETURN_FAILURE, task.start(null));
        verify(handler, times(1)).raise("An error occured while submitting feedback:\nError message here");
    }

}
