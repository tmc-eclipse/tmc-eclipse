package fi.helsinki.cs.plugin.tmc.services;

import java.util.List;

import fi.helsinki.cs.plugin.tmc.domain.FeedbackAnswer;
import fi.helsinki.cs.plugin.tmc.services.http.ServerManager;

public class FeedbackAnswerSubmitter {
    private ServerManager server;

    public FeedbackAnswerSubmitter(ServerManager server) {
        this.server = server;

    }

    public void submitFeedback(List<FeedbackAnswer> answers, String answerUrl) {
        if (answers.isEmpty() || answerUrl == null || answerUrl.trim().length() == 0) {
            return;
        }

        server.submitFeedback(answerUrl, answers);
    }
}
