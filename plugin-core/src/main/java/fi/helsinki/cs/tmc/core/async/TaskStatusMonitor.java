package fi.helsinki.cs.tmc.core.async;

/**
 * An interface that all the IDE-specific task status monitors must implement.
 * Background tasks should update the given task status monitor whenever their
 * status changes and the monitor in turn is responsible for notifying the user
 * in a IDE-specific way.
 */
public interface TaskStatusMonitor {

    void startProgress(String message, int amountOfWork);

    void incrementProgress(int progress);

    boolean isCancelRequested();

}
