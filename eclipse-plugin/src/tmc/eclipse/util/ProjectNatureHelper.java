package tmc.eclipse.util;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;

import fi.helsinki.cs.tmc.core.Core;
import fi.helsinki.cs.tmc.core.domain.Exercise;
import fi.helsinki.cs.tmc.core.io.FileUtil;
/**
 * Class that provides methods for handling project natures.
 */
public class ProjectNatureHelper {
    
    /**
     * Updates the IProject natures to match the status of the given exercise.
     * @param e
     */
    public static void updateTMCProjectNature(Exercise e) {
        IProject project = IProjectHelper.getIProjectWithFilePath(FileUtil.getUnixPath(e.getProject().getRootPath()));

        if (e.isCompleted()) {
            setProjectNature(project, TMCCompletedProjectNature.NATURE_ID);
        } else if (e.isAttempted()) {
            setProjectNature(project, TMCAttemptedProjectNature.NATURE_ID);
        } else {
            setProjectNature(project, TMCNewProjectNature.NATURE_ID);
        }

    }
    
    /**
     * Checks whether the given IProject has a TMC Project nature.
     */
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
    
    /**
     * Adds the TMC project nature to an IProject that does not have a TMC nature.
     */
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
    
    /**
     * Updates the TMC project nature in an IProject that already has a TMC nature.
     */
    private static void updateNature(IProject project, String NATURE_ID, IProjectDescription description)
            throws CoreException {
        String[] newNatures = description.getNatureIds();
        newNatures[0] = NATURE_ID;
        description.setNatureIds(newNatures);
        project.setDescription(description, new NullProgressMonitor());
    }

}
