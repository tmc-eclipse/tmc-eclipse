package fi.helsinki.cs.tmc.core.async;

/**
 * An abstract class extended by all background tasks.
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

    public abstract int start(TaskStatusMonitor progress);

    public void stop() {
        this.isRunning = false;
    }

    public String getDescription() {
        return this.description;
    }

    /**
     * Returns whether the background task should stop it's execution. This
     * method should be polled periodically from the background task
     * implementation. A task may be stopped either by calling it's
     * {@link #stop()} method (usually invoked through the task runner) or by
     * user interaction.
     * 
     * @param progress
     *            a task status monitor for listening to task cancellation
     *            requests
     * @return true, if the task execution should be stopped. False otherwise.
     */
    protected boolean shouldStop(TaskStatusMonitor progress) {
        if (!isRunning) {
            return true;
        }

        if (progress != null && progress.isCancelRequested()) {
            return true;
        }
        return false;
    }
}