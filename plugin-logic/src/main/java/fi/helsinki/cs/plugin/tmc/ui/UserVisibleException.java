package fi.helsinki.cs.plugin.tmc.ui;

public class UserVisibleException extends RuntimeException {
    public UserVisibleException(String msg) {
        super(msg);
    }

    public UserVisibleException(String msg, Throwable cause) {
        super(msg, cause);
    }
}