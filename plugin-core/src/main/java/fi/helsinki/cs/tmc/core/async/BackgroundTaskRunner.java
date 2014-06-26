package fi.helsinki.cs.tmc.core.async;

/**
 * An interface all background task runners implement. Plugins should provide an
 * IDE-specific implementation for running/stopping tasks which is responsible
 * for giving the user adequate feedback about task status (for example, UI
 * notifications or progress bar).
 */
public interface BackgroundTaskRunner {

    /**
     * Runs a specified background task
     * 
     * @param task
     *            The background task to run
     */
    void runTask(BackgroundTask task);

    /**
     * Runs a specified background task with a given listener that is notified
     * when the status of the task changes
     * 
     * @param task
     *            The background task to run
     * @param listener
     *            A listener to be notified when the status of the task changes
     */
    void runTask(BackgroundTask task, BackgroundTaskListener listener);

    /**
     * Cancels the execution of a given background task
     * 
     * @param task
     *            The background task to cancel
     */
    void cancelTask(BackgroundTask task);

}