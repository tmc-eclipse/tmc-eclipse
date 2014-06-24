package fi.helsinki.cs.tmc.core.services;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import fi.helsinki.cs.tmc.core.domain.FeedbackAnswer;
import fi.helsinki.cs.tmc.core.domain.FeedbackQuestion;
import fi.helsinki.cs.tmc.core.services.FeedbackAnswerSubmitter;
import fi.helsinki.cs.tmc.core.services.http.ServerManager;

public class FeedbackAnswerSubmitterTest {

    private FeedbackAnswerSubmitter submitter;
    private ServerManager server;
    private List<FeedbackAnswer> answers;
    private String url;

    @Before
    public void setUp() {
        server = mock(ServerManager.class);
        submitter = new FeedbackAnswerSubmitter(server);
        answers = new ArrayList<FeedbackAnswer>();
        url = "mock_url";
    }

    @Test
    public void serverManagerIsNotCalledWhenListIsEmpty() {
        submitter.submitFeedback(answers, "");
        verify(server, times(0)).submitFeedback(Mockito.anyString(), Mockito.anyListOf(FeedbackAnswer.class));
    }

    @Test
    public void serverManagerIsNotCalledWhenListIsNull() {
        submitter.submitFeedback(null, "");
        verify(server, times(0)).submitFeedback(Mockito.anyString(), Mockito.anyListOf(FeedbackAnswer.class));
    }

    @Test
    public void serverManagerIsNotCalledWhenUrlIsNull() {
        addAnswer(0, "Question text", "text", "answer");
        submitter.submitFeedback(answers, null);
        verify(server, times(0)).submitFeedback(Mockito.anyString(), Mockito.anyListOf(FeedbackAnswer.class));
    }

    @Test
    public void serverManagerIsNotCalledWhenUrlIsEmpty() {
        addAnswer(0, "Question text", "text", "answer");
        submitter.submitFeedback(answers, "   ");
        verify(server, times(0)).submitFeedback(Mockito.anyString(), Mockito.anyListOf(FeedbackAnswer.class));
    }

    @Test
    public void serverManagerIsNotCalledWhenAnswersAreEmpty() {
        addAnswer(0, "Question text", "text", "");
        submitter.submitFeedback(answers, url);
        verify(server, times(0)).submitFeedback(Mockito.anyString(), Mockito.anyListOf(FeedbackAnswer.class));
    }

    @Test
    public void submitFeedbackIsCalledWhenParametersAreCorrect() {
        addAnswer(0, "Question text", "text", "answer");
        submitter.submitFeedback(answers, url);
        verify(server, times(1)).submitFeedback(url, answers);
    }

    private void addAnswer(int id, String question, String kind, String answer) {
        answers.add(new FeedbackAnswer(new FeedbackQuestion(id, question, kind), answer));
    }
}
