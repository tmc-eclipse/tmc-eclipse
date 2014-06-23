package tmc.util;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;

import fi.helsinki.cs.plugin.tmc.io.FileUtil;

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
