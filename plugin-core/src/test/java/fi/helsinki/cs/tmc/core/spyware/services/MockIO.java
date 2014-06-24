package fi.helsinki.cs.tmc.core.spyware.services;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

import fi.helsinki.cs.tmc.core.io.IO;

public class MockIO implements IO {

    StringWriter writer = new StringWriter();

    @Override
    public Writer getWriter() {
        try {
            writer.close();
        } catch (IOException e) {
        }
        writer = new StringWriter();
        return writer;
    }

    @Override
    public Reader getReader() {
        return new StringReader(writer.toString());
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getPath() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean fileExists() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean directoryExists() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public OutputStream getOutputStream() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public InputStream getInputStream() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void write(byte[] bytes) throws IOException {
        // TODO Auto-generated method stub

    }

    @Override
    public void createFolderTree(boolean onlyParents) {
        // TODO Auto-generated method stub

    }

    @Override
    public List<IO> getChildren() {
        // TODO Auto-generated method stub
        return null;
    }
}
