package fi.helsinki.cs.plugin.tmc.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

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
    public Writer getWriter() {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            return new OutputStreamWriter(new BufferedOutputStream(fos), "UTF-8");
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public Reader getReader() {
        try {
            FileInputStream fis = new FileInputStream(file);
            return new InputStreamReader(new BufferedInputStream(fis), "UTF-8");
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
    public void write(byte[] bytes) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        for (int i = 0; i < bytes.length; i++) {
            fos.write(bytes[i]);
        }
        fos.close();
    }
}
