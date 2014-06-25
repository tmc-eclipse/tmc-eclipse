package fi.helsinki.cs.tmc.core.io;

import java.util.HashMap;
import java.util.Map;

public class FakeIOFactory implements IOFactory {

    public Map<String, IO> files;

    public FakeIOFactory() {
        this.files = new HashMap<String, IO>();
    }

    @Override
    public IO newFile(String path) {
        if (files.containsKey(path)) {
            return files.get(path);
        }

        IO file = new FakeFileIO(path);
        files.put(path, file);
        return file;
    }

    public FakeFileIO getFake(String path) {
        return (FakeFileIO) newFile(path);
    }

}
