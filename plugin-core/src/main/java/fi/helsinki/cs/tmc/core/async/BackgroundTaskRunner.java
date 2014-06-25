package fi.helsinki.cs.tmc.core.async;

/**
 * An interface all background task runners implement. Plugins should provide an
 * IDE-specific implementation for running/stopping tasks which is responsible
 * for giving the user adequate feedback about task status (for example, UI
 * notifications or progress bar).
 */
public interface BackgroundTaskRunner {

    void runTask(BackgroundTask task);

    void runTask(BackgroundTask task, BackgroundTaskListener listener);

    void cancelTask(BackgroundTask task);

}