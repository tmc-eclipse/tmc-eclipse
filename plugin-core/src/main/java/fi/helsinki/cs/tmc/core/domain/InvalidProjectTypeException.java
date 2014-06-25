package fi.helsinki.cs.tmc.core.domain;

/**
 * An exception to be thrown when the project type could not be determined.
 */
public class InvalidProjectTypeException extends RuntimeException {

    public InvalidProjectTypeException() {
        super();
    }

    public InvalidProjectTypeException(String message) {
        super(message);
    }

    private static final long serialVersionUID = -6754119701787304349L;
}
