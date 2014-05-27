package fi.helsinki.cs.plugin.tmc.async;

public interface BackgroundTask {

    void start(TaskFeedback progress);

    void stop();

    String getName();

}