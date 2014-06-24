package fi.helsinki.cs.tmc.core.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class FeedbackAnswerTest {
    private FeedbackAnswer answer;
    
    @Before
    public void setUp() {
        this.answer = new FeedbackAnswer(new FeedbackQuestion(0, "question1", "kind1"));
    }

    @Test
    public void toJsonTest() {
        answer.setAnswer("answer1");
        answer.setQuestion(new FeedbackQuestion(1, "question2", "kind2"));
        assertEquals(answer.toJson(), "{\"question_id\":1,\"answer\":\"answer1\"}");
    }

}
