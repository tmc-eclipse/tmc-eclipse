package fi.helsinki.cs.tmc.core;

/**
 * An interface that all the error handlers implement. IDE-specific plugins
 * should implement this interface so that exceptions thrown by Core can be
 * handled properly and the user can be shown appropriate error messages.
 */
public interface TMCErrorHandler {

    void raise(String message);

    void handleException(Exception e);

    void handleManualException(String errorMessage);

}
