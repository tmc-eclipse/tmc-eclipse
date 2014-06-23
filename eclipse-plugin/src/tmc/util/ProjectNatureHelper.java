package tmc.util;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;

import fi.helsinki.cs.plugin.tmc.Core;
import fi.helsinki.cs.plugin.tmc.domain.Exercise;
import fi.helsinki.cs.plugin.tmc.io.FileUtil;

public class ProjectNatureHelper {

    public static void updateTMCProjectNature(Exercise e) {
        IProject project = IProjectHelper.getIProjectWithFilePath(FileUtil.getUnixPath(e.getProject().getRootPath()));

        if (e.isCompleted()) {
            System.out.println("1");
            setProjectNature(project, TMCCompletedProjectNature.NATURE_ID);
        } else if (e.isAttempted()) {
            System.out.println("2");
            setProjectNature(project, TMCAttemptedProjectNature.NATURE_ID);
        } else {
            System.out.println("3");
            setProjectNature(project, TMCNewProjectNature.NATURE_ID);
        }

    }

    public static boolean isTMCProject(IProject project) {
        try {
            IProjectDescription description = project.getDescription();
            return description.hasNature(TMCAttemptedProjectNature.NATURE_ID)
                    || description.hasNature(TMCCompletedProjectNature.NATURE_ID)
                    || description.hasNature(TMCNewProjectNature.NATURE_ID);
        } catch (CoreException e) {
            Core.getErrorHandler().handleException(e);
        }
        return false;
    }

    public static void setProjectNature(IProject project, String NATURE_ID) {
        try {
            IProjectDescription description = project.getDescription();
            if (isTMCProject(project)) {
                updateNature(project, NATURE_ID, description);
            } else {
                addTMCNature(project, NATURE_ID, description);
            }
        } catch (CoreException e) {
            Core.getErrorHandler().handleException(e);
        }
    }

    private static void addTMCNature(IProject project, String NATURE_ID, IProjectDescription description)
            throws CoreException {
        String[] prevNatures = description.getNatureIds();
        String[] newNatures = new String[prevNatures.length + 1];
        System.arraycopy(prevNatures, 0, newNatures, 0, prevNatures.length);
        newNatures[prevNatures.length] = NATURE_ID;
        String temp = newNatures[newNatures.length - 1];
        newNatures[newNatures.length - 1] = newNatures[0];
        newNatures[0] = temp;
        description.setNatureIds(newNatures);
        project.setDescription(description, new NullProgressMonitor());
    }

    private static void updateNature(IProject project, String NATURE_ID, IProjectDescription description)
            throws CoreException {
        String[] newNatures = description.getNatureIds();
        newNatures[0] = NATURE_ID;
        description.setNatureIds(newNatures);
        project.setDescription(description, new NullProgressMonitor());
    }

}
