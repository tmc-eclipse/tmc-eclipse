package fi.helsinki.cs.tmc.core.io;

/**
 * An interface for IO creation; mostly used to enable better unit testing as we
 * can inject mock factory that creates mock objects.
 */
public interface IOFactory {

    public IO newFile(String path);

}
