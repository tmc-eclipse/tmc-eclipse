package fi.helsinki.cs.plugin.tmc.async;

import java.util.List;

/**
 * 
 * This is an abstract base class for any tasks that has list of objects that
 * must be processed and require the IDE progress bar to be advanced between
 * steps.
 * 
 * 
 * @param <T>
 *            Object type to be used
 * 
 */
public abstract class SimpleBackgroundTask<T> implements BackgroundTask {

    private boolean isRunning;
    private String description;

    private List<T> list;

    /**
     * 
     * @param description
     *            Description of the background task. Should not be null, at
     *            least eclipse will encounter NPEs otherwise.
     * @param list
     *            List of objects that will be provided to the run-method one by
     *            one
     */
    public SimpleBackgroundTask(String description, List<T> list) {
        this.isRunning = true;
        this.description = description;
        this.list = list;
    }

    /**
     * This method is called by the IDE specific implementation of the
     * BackgroundTaskRunner. It will provide the TaskFeedback-implementation
     * that handles the IDE progress bar
     */
    @Override
    public int start(TaskFeedback progress) {
        progress.startProgress(description, list.size());

        for (T t : list) {
            if (progress.isCancelRequested()) {
                this.stop();
            }
            if (!isRunning) {
                return BackgroundTask.RETURN_FAILURE;
            }

            run(t);

            progress.incrementProgress(1);
        }

        return BackgroundTask.RETURN_SUCCESS;
    }

    @Override
    public void stop() {
        this.isRunning = false;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public abstract void run(T t);

}