package tmc.eclipse.openers;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;

public class AntProjectOpener {
    private String pathToProjectFile;

    public AntProjectOpener(String pathToProjectFile) {
        this.pathToProjectFile = pathToProjectFile;
    }

    public IProject importAndOpen() throws CoreException {
        IProjectDescription description = ResourcesPlugin.getWorkspace().loadProjectDescription(
                new Path(pathToProjectFile));
        IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(description.getName());

        if (!project.exists()) {
            project.create(description, null);

            project.open(new NullProgressMonitor());

        }

        return project;

    }

}
