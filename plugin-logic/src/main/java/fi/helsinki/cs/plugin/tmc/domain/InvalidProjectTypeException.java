package fi.helsinki.cs.plugin.tmc.domain;

/**
 * Thrown when project type could not be determined.
 * 
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
