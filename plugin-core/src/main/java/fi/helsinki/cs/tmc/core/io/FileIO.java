package fi.helsinki.cs.tmc.core.io;

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

/**
 * Wrapper class for file IO. Allows us to mock file interactions in unit tests.
 */
public class FileIO {

    private File file;

    public FileIO(String path) {
        this.file = new File(path);
    }

    public String getName() {
        return file.getName();
    }

    public String getPath() {
        return FileUtil.getUnixPath(file.getAbsolutePath());
    }

    public boolean fileExists() {
        return file.exists() && !file.isDirectory();
    }

    public boolean directoryExists() {
        return file.exists() && file.isDirectory();
    }

    /**
     * Important: It is the responsibility of the caller to close this stream!
     */
    public OutputStream getOutputStream() {
        try {
            return new FileOutputStream(file);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Important: It is the responsibility of the caller to close this stream!
     */
    public InputStream getInputStream() {
        try {
            return new FileInputStream(file);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Important: It is the responsibility of the caller to close this writer!
     */
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

    /**
     * Important: It is the responsibility of the caller to close this reader!
     */
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

    public void createFolderTree(boolean onlyParents) {
        if (onlyParents) {
            file.getParentFile().mkdirs();
        } else {
            file.mkdirs();
        }
    }

    public List<FileIO> getChildren() {
        List<FileIO> children = new ArrayList<FileIO>();
        if (directoryExists()) {
            for (File f : file.listFiles()) {
                children.add(new FileIO(f.getAbsolutePath()));
            }
        }
        return children;
    }

    public void write(byte[] bytes) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            for (int i = 0; i < bytes.length; i++) {
                fos.write(bytes[i]);
            }
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public byte[] read() {
        // TODO: implement
        return null;
    }

}
