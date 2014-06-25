package fi.helsinki.cs.tmc.core.io;

import java.io.File;

/**
 * Helper class for file operations.
 */
public class FileUtil {

    public static String append(String a, String b) {
        return getUnixPath(a) + "/" + getUnixPath(b);
    }

    /**
     * Replaces the file separators in given path with Unix styled separators.
     * We use these internally.
     */
    public static String getUnixPath(String path) {
        String unixPath = path.replace(File.separator, "/");

        if (!unixPath.isEmpty() && unixPath.charAt(unixPath.length() - 1) == '/') {
            unixPath = unixPath.substring(0, unixPath.length() - 1);
        }

        return unixPath;
    }

    public static String getNativePath(String path) {
        return new File(path).getAbsolutePath();
    }

}
