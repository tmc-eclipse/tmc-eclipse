package fi.helsinki.cs.plugin.tmc.tasks;

public interface BackgroundTaskRunner {

    void runTask(BackgroundTask task);

    void cancelTask(BackgroundTask task);

}