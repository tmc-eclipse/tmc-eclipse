package fi.helsinki.cs.plugin.tmc;

public interface TMCErrorHandler {

    void raise(String message);

    void handleException(Exception e);
    
    void handleManualException(String errorMessage);

}
