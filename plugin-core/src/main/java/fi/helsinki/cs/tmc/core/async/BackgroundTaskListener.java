package fi.helsinki.cs.tmc.core.async;

/**
 * An interface that all background task listeners implement.
 */
public interface BackgroundTaskListener {

    /**
     * Called when background task is being started
     */
    public void onBegin();

    /**
     * Called when background task returns RETURN_SUCCESS
     */
    public void onSuccess();

    /**
     * Called when background task returns RETURN_FAILURE
     */
    public void onFailure();

    /**
     * Called when the background task returns RETURN_INTERRUPTED
     */
    public void onInterruption();

}
