package fi.helsinki.cs.tmc.core.async;

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
public abstract class SimpleBackgroundTask<T> extends BackgroundTask {
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
        super(description);
        this.list = list;
    }

    /**
     * This method is called by the IDE specific implementation of the
     * BackgroundTaskRunner. It will provide the TaskFeedback-implementation
     * that handles the IDE progress bar
     */
    @Override
    public int start(TaskFeedback progress) {
        progress.startProgress(this.getDescription(), list.size());

        for (T t : list) {
            if (shouldStop(progress)) {
                return BackgroundTask.RETURN_INTERRUPTED;
            }

            run(t);

            progress.incrementProgress(1);
        }

        return BackgroundTask.RETURN_SUCCESS;
    }

    public abstract void run(T t);

}