package fi.helsinki.cs.plugin.tmc.async;

/**
 * Interface for the IDE specific background tasks
 * 
 */
public abstract class BackgroundTask {

    public static final int RETURN_SUCCESS = 0;
    public static final int RETURN_FAILURE = 1;
    public static final int RETURN_INTERRUPTED = 2;

    private boolean isRunning = true;
    private String description;

    public BackgroundTask(String description) {
        this.description = description;
    }

    public abstract int start(TaskFeedback progress);

    public void stop() {
        this.isRunning = false;
    }

    public String getDescription() {
        return this.description;
    }

    protected boolean shouldStop(TaskFeedback progress) {
        if (!isRunning) {
            return true;
        }

        if (progress != null && progress.isCancelRequested()) {
            return true;
        }
        return false;
    }
}