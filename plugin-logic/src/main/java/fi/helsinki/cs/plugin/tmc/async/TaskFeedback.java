package fi.helsinki.cs.plugin.tmc.async;

public interface TaskFeedback {

    void startProgress(String message, int amountOfWork);

    void incrementProgress(int progress);

    boolean isCancelRequested();

}
