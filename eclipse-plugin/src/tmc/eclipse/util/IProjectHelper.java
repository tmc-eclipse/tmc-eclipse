package tmc.eclipse.util;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;

import fi.helsinki.cs.tmc.core.io.FileUtil;
/**
 * Class that provides methods for checking whether an IProject -object corresponding to the given path exists.
 *
 */
public class IProjectHelper {

    public static IProject getIProjectWithFilePath(String path) {
        for (IProject p : ResourcesPlugin.getWorkspace().getRoot().getProjects()) {
            if (p.getRawLocation() != null && path.equals(FileUtil.getUnixPath(p.getRawLocation().toString()))) {
                return p;
            }
        }
        return null;
    }

    public static boolean projectWithThisFilePathExists(String path) {
        return getIProjectWithFilePath(path) != null;
    }

}
