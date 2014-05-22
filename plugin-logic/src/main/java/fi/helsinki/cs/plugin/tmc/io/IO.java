package fi.helsinki.cs.plugin.tmc.io;

import java.io.Reader;
import java.io.Writer;

public interface IO {

    String getName();

    String getPath();

    boolean fileExists();

    Writer getWriter();

    Reader getReader();

    void write(byte[] bytes);

    void createFolderTree(boolean onlyParents);

}