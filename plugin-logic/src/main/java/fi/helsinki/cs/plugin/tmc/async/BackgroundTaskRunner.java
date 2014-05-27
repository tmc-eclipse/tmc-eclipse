package fi.helsinki.cs.plugin.tmc.async;

public interface BackgroundTaskRunner {

    void runTask(BackgroundTask task);

    void cancelTask(BackgroundTask task);

}