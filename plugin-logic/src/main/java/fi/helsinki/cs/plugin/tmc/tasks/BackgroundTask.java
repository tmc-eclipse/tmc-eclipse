package fi.helsinki.cs.plugin.tmc.tasks;

public interface BackgroundTask {

    Object start(TaskFeedback feedback);

    void stop();

    String getName();

}