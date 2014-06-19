package tmc.util;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import fi.helsinki.cs.plugin.tmc.io.FileUtil;

public class IProjectHelper {

    public static boolean projectWithThisFilePathExists(String path) {
        return getProjectWithThisFilePath(path) != null;
    }

    public static IProject getProjectWithThisFilePath(String path) {
        for (IProject p : ResourcesPlugin.getWorkspace().getRoot().getProjects()) {
            if (p.getRawLocation() != null && path.contains(FileUtil.getUnixPath(p.getRawLocation().toString()))) {
                return p;
            }
        }
        return null;
    }

}
