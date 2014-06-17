package fi.helsinki.cs.plugin.tmc.async;

/**
 * Interface for the IDE specific background tasks
 * 
 */
public interface BackgroundTask {

    public static final int RETURN_SUCCESS = 0;
    public static final int RETURN_FAILURE = 1;

    public abstract int start(TaskFeedback progress);

    public abstract void stop();

    public abstract String getDescription();

}