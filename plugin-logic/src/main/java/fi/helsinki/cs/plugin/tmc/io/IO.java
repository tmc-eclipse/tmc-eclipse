package fi.helsinki.cs.plugin.tmc.io;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public interface IO {

    String getName();

    String getPath();

    boolean fileExists();

    Writer getWriter();

    Reader getReader();

    void write(byte[] bytes) throws IOException;

    void createFolderTree(boolean onlyParents);

}