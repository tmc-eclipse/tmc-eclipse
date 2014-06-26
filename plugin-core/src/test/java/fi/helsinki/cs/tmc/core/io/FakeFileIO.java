package fi.helsinki.cs.tmc.core.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class FakeFileIO implements IO {

    private String path;
    private boolean fileExists;
    private boolean directoryExists;

    private byte[] byteContents;

    public FakeFileIO(String path) {
        this.path = path;
        this.byteContents = null;
        this.fileExists = true;
        this.directoryExists = true;
    }

    public void setFileExists(boolean fileExists) {
        this.fileExists = fileExists;
    }

    public void setDirectoryExists(boolean directoryExists) {
        this.directoryExists = directoryExists;
    }

    public void setContents(byte[] bytes) {
        this.byteContents = bytes;
    }

    public void setContents(String contents) {
        try {
            this.byteContents = contents.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
        }
    }

    @Override
    public String getName() {
        String[] pathTree = path.split("/");
        return pathTree[pathTree.length - 1];
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public boolean fileExists() {
        return fileExists;
    }

    @Override
    public boolean directoryExists() {
        return directoryExists;
    }

    @Override
    public OutputStream getOutputStream() {
        ByteArrayOutputStream inMemoryOutputStream = new ByteArrayOutputStream() {

            @Override
            public void flush() {
                byteContents = this.toByteArray();
            }

        };

        return inMemoryOutputStream;
    }

    @Override
    public InputStream getInputStream() {
        return new ByteArrayInputStream(byteContents);
    }

    @Override
    public Writer getWriter() {
        try {
            OutputStream os = getOutputStream();
            if (os == null) {
                return null;
            }

            return new OutputStreamWriter(new BufferedOutputStream(os), "UTF-8");
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public Reader getReader() {
        try {
            InputStream is = getInputStream();
            if (is == null) {
                return null;
            }
            return new InputStreamReader(new BufferedInputStream(is), "UTF-8");
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public void write(byte[] bytes) throws IOException {
        this.byteContents = bytes;
    }

    @Override
    public void createFolderTree(boolean onlyParents) {
    }

    @Override
    public List<IO> getChildren() {
        return new ArrayList<IO>();
    }

    @Override
    public byte[] read() throws IOException {
        return byteContents;
    }

}
