package tmc.util;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;

public class IProjectHelper {

    public static IProject[] getWorkSpaceProjects() {
        return ResourcesPlugin.getWorkspace().getRoot().getProjects();
    }

    public static IProject getIProjectByPath(String path) {
        for (IProject project : getWorkSpaceProjects()) {
            if (path.contains(project.getProjectRelativePath().toString())) {
                return project;
            }
        }
        return null;
    }
}
