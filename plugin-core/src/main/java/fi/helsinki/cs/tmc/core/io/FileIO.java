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
public class FileIO implements IO {

    private File file;

    public FileIO(String path) {
        this.file = new File(path);
    }

    @Override
    public String getName() {
        return file.getName();
    }

    @Override
    public String getPath() {
        return FileUtil.getUnixPath(file.getAbsolutePath());
    }

    @Override
    public boolean fileExists() {
        return file.exists() && !file.isDirectory();
    }

    @Override
    public boolean directoryExists() {
        return file.exists() && file.isDirectory();
    }

    /**
     * Important: It is the responsibility of the caller to close this stream!
     */
    @Override
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
    @Override
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

    /**
     * Important: It is the responsibility of the caller to close this reader!
     */
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

    @Override
    public byte[] read() throws IOException {
        // TODO: implement
        return null;
    }

}
