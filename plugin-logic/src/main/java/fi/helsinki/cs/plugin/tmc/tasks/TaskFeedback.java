package fi.helsinki.cs.plugin.tmc.tasks;

public interface TaskFeedback {

    void resetProgress(String message, int amountOfWork);

    void updateProgress(int progress);

    boolean isCanceled();

}
