package fi.helsinki.cs.plugin.tmc.async;

public interface BackgroundTask {

    public static final int RETURN_SUCCESS = 0;
    public static final int RETURN_FAILURE = 1;

    public abstract int start(TaskFeedback progress);

    public abstract void stop();

    public abstract String getDescription();

}