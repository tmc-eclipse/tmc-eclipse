package fi.helsinki.cs.tmc.core.services;

import java.util.List;

import fi.helsinki.cs.tmc.core.domain.FeedbackAnswer;
import fi.helsinki.cs.tmc.core.services.http.ServerManager;

/**
 * Class used by the feedback submission background task.
 * 
 */
public class FeedbackAnswerSubmitter {
    private ServerManager server;

    public FeedbackAnswerSubmitter(ServerManager server) {
        this.server = server;

    }

    public void submitFeedback(List<FeedbackAnswer> answers, String answerUrl) {
        if (answers == null || answers.isEmpty() || answerUrl == null || answerUrl.trim().length() == 0) {
            return;
        }

        if (answersAreEmpty(answers)) {
            return;
        }

        server.submitFeedback(answerUrl, answers);
    }

    private boolean answersAreEmpty(List<FeedbackAnswer> answers) {
        for (FeedbackAnswer a : answers) {
            if (a.getAnswer().trim().length() != 0) {
                return false;
            }
        }
        return true;
    }
}
