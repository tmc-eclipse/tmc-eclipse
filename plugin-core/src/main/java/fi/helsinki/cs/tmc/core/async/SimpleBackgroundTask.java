package fi.helsinki.cs.tmc.core.async;

import java.util.List;

/**
 * This is an abstract base class for any tasks that need to process a list of
 * objects. Simplifies background task creation by taking care of initializing
 * and updating the task status automatically as the list is processed.
 * 
 * @param <T>
 *            The type of the objects to be processed
 */
public abstract class SimpleBackgroundTask<T> extends BackgroundTask {
    private List<T> list;

    /**
     * Creates a new SimpleBackgroundTask with given description for processing
     * a given list of objects.
     * 
     * @param description
     *            Description of the background task. Should not be null, at
     *            least Eclipse will encounter NullPointerExceptions otherwise.
     * @param list
     *            List of objects that will be provided to the {@link #run(T)}
     *            method one by one
     */
    public SimpleBackgroundTask(String description, List<T> list) {
        super(description);
        this.list = list;
    }

    /**
     * This method is called by the IDE specific implementation of the
     * BackgroundTaskRunner. It will provide the TaskFeedback implementation
     * that handles the IDE progress bar.
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