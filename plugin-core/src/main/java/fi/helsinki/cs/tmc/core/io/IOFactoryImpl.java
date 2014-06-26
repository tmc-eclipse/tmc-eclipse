package fi.helsinki.cs.tmc.core.io;

/**
 * Implementation of the IOFactory interface. Creates the actual FileIO objects.
 */
public class IOFactoryImpl implements IOFactory {

    @Override
    public FileIO newFile(String path) {
        return new FileIO(path);
    }

}