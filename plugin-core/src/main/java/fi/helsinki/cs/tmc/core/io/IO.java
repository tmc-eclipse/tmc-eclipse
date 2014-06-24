package fi.helsinki.cs.tmc.core.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.List;

public interface IO {

    String getName();

    String getPath();

    boolean fileExists();

    boolean directoryExists();

    OutputStream getOutputStream();

    InputStream getInputStream();

    Writer getWriter();

    Reader getReader();

    void write(byte[] bytes) throws IOException;

    void createFolderTree(boolean onlyParents);

    List<FileIO> getChildren();

}