package fi.helsinki.cs.tmc.core;

/**
 * Interface for the error handlers. IDE should implement this so that program
 * may raise errors (the UI elements at least) from core
 * 
 * 
 */
public interface TMCErrorHandler {

    void raise(String message);

    void handleException(Exception e);

    void handleManualException(String errorMessage);

}
