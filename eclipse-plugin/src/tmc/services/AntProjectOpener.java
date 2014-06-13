package tmc.services;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;

import tmc.util.TMCProjectNature;

public class AntProjectOpener {
    private String pathToProjectFile;

    public AntProjectOpener(String pathToProjectFile) {
        this.pathToProjectFile = pathToProjectFile;
    }

    public void importAndOpen() throws CoreException {
        IProjectDescription description = ResourcesPlugin.getWorkspace().loadProjectDescription(
                new Path(pathToProjectFile));
        IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(description.getName());

        String[] prevNatures = description.getNatureIds();
        String[] newNatures = new String[prevNatures.length + 1];
        System.arraycopy(prevNatures, 0, newNatures, 0, prevNatures.length);
        newNatures[prevNatures.length] = TMCProjectNature.NATURE_ID;
        description.setNatureIds(newNatures);

        project.create(description, null);

        project.open(new NullProgressMonitor());

    }

}
