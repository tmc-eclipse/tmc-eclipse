package fi.helsinki.cs.plugin.tmc.async;

/**
 * Interface for the BackgroundTaskRunner. IDE should implement this so that it
 * has control over the progress bar.
 * 
 */
public interface BackgroundTaskRunner {

    void runTask(BackgroundTask task);

    void runTask(BackgroundTask task, BackgroundTaskListener listener);

    void cancelTask(BackgroundTask task);

}