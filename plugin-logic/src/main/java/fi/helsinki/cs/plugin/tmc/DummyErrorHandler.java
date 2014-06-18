package fi.helsinki.cs.plugin.tmc;

/**
 * Dummy error handler that does nothing. Default for the Core until
 * ide-specific implementation is provided.
 * 
 */
public class DummyErrorHandler implements TMCErrorHandler {

    @Override
    public void raise(String message) {
        // Do nothing.
    }

    @Override
    public void handleException(Exception e) {
        // Do nothing.
    }

    public void handleManualException(String errorMessage) {
        // Do nothing.

    }

}
