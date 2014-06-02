package fi.helsinki.cs.plugin.tmc.async;

public interface BackgroundTaskRunner {

    void runTask(BackgroundTask task);

    void runTask(BackgroundTask task, BackgroundTaskListener listener);

    void cancelTask(BackgroundTask task);

}