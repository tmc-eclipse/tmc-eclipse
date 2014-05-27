package fi.helsinki.cs.plugin.tmc.async;

public interface BackgroundTask {

    public abstract void start(TaskFeedback progress);

    public abstract void stop();

    public abstract String getDescription();

}