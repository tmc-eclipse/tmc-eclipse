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

        project.create(description, null);

        project.open(new NullProgressMonitor());

        description = project.getDescription();

        String[] prevNatures = description.getNatureIds();
        String[] newNatures = new String[prevNatures.length + 1];
        System.arraycopy(prevNatures, 0, newNatures, 0, prevNatures.length);
        newNatures[prevNatures.length] = TMCProjectNature.NATURE_ID;
        String temp = newNatures[newNatures.length - 1];
        newNatures[newNatures.length - 1] = newNatures[0];
        newNatures[0] = temp;
        description.setNatureIds(newNatures);
        project.setDescription(description, new NullProgressMonitor());

    }

}
