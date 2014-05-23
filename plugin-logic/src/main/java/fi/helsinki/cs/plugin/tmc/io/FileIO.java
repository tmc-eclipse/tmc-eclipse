package fi.helsinki.cs.plugin.tmc.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class FileIO implements IO {

    private File file;

    public FileIO(String file) {
        this.file = new File(file);
    }

    @Override
    public String getName() {
        return file.getName();
    }

    @Override
    public String getPath() {
        return file.getAbsolutePath();
    }

    @Override
    public boolean fileExists() {
        return file.exists() && !file.isDirectory();
    }

    @Override
    public boolean directoryExists() {
        return file.exists() && file.isDirectory();
    }

    @Override
    public OutputStream getOutputStream() {
        try {
            return new FileOutputStream(file);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public InputStream getInputStream() {
        try {
            return new FileInputStream(file);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public Writer getWriter() {
        try {
            return new OutputStreamWriter(new BufferedOutputStream(getOutputStream()), "UTF-8");
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public Reader getReader() {
        try {
            return new InputStreamReader(new BufferedInputStream(getInputStream()), "UTF-8");
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public void createFolderTree(boolean onlyParents) {
        if (onlyParents) {
            file.getParentFile().mkdirs();
        } else {
            file.mkdirs();
        }
    }

    @Override
    public List<IO> getChildren() {
        List<IO> children = new ArrayList<IO>();
        if (directoryExists()) {
            for (File f : file.listFiles()) {
                children.add(new FileIO(f.getAbsolutePath()));
            }
        }
        return children;
    }

    @Override
    public void write(byte[] bytes) {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            for (int i = 0; i < bytes.length; i++) {
                fos.write(bytes[i]);
            }
            fos.close();
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }
}
