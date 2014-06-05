package fi.helsinki.cs.plugin.tmc;

public class DummyErrorHandler implements TMCErrorHandler {

    @Override
    public void raise(String message) {
        // Do nothing.
    }

    @Override
    public void handleException(Exception e) {
        // Do nothing.
    }

}
