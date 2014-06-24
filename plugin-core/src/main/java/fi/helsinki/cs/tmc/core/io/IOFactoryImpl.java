package fi.helsinki.cs.tmc.core.io;

/**
 * Implementation of the IOFactory interface. Creates actual FileIO-objects
 * 
 */
public class IOFactoryImpl implements IOFactory {

    @Override
    public IO createIO(String path) {
        return new FileIO(path);
    }

}
