package fi.helsinki.cs.tmc.core.async;

/**
 * Interface for the IDE specific progress bar
 * 
 */
public interface TaskFeedback {

    void startProgress(String message, int amountOfWork);

    void incrementProgress(int progress);

    boolean isCancelRequested();

}
