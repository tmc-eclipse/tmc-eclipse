package tmc.eclipse.openers;

import java.io.IOException;
import java.net.URISyntaxException;

import org.eclipse.core.internal.resources.ResourceException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;

import tmc.eclipse.util.IProjectHelper;
import tmc.eclipse.util.ProjectNatureHelper;
import tmc.eclipse.util.TMCNewProjectNature;
import fi.helsinki.cs.tmc.core.Core;
import fi.helsinki.cs.tmc.core.TMCErrorHandler;
import fi.helsinki.cs.tmc.core.domain.Exercise;
import fi.helsinki.cs.tmc.core.domain.Project;
import fi.helsinki.cs.tmc.core.domain.ProjectType;
import fi.helsinki.cs.tmc.core.io.FileUtil;
import fi.helsinki.cs.tmc.core.services.ProjectDAO;
import fi.helsinki.cs.tmc.core.services.ProjectOpener;

@SuppressWarnings("restriction")
public class GenericProjectOpener implements ProjectOpener {
    ProjectDAO projectDAO;
    TMCErrorHandler errorHandler;

    public GenericProjectOpener() {
        projectDAO = Core.getProjectDAO();
        errorHandler = Core.getErrorHandler();
    }

    public void open(Exercise e) {

        Project project = projectDAO.getProjectByExercise(e);
        ProjectType projectType = project.getProjectType();
        IProject projectObject;
        try {
            if (IProjectHelper.projectWithThisFilePathExists(project.getRootPath())) {
                projectObject = IProjectHelper.getIProjectWithFilePath(project.getRootPath());
            } else {
                projectObject = null;
                projectObject = openWithCorrectOpener(project, projectType, projectObject);

            }
            ResourcesPlugin.getWorkspace().getRoot().refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
            ProjectNatureHelper.updateTMCProjectNature(e);

        } catch (Exception e1) {
            Core.getErrorHandler().handleException(e1);
        }

    }

    private IProject openWithCorrectOpener(Project project, ProjectType projectType, IProject projectObject)
            throws CoreException, IOException, URISyntaxException {
        switch (project.getProjectType()) {
        case JAVA_ANT:
            projectObject = openAntProject(project, projectType);
            break;
        case JAVA_MAVEN:
            projectObject = openMavenProject(project, projectType);
            break;
        case MAKEFILE:
            projectObject = openCProject(project, projectType);
            break;
        default:
            break;
        }
        return projectObject;
    }

    private void addTMCProjectNature(IProject projectObject) throws CoreException {
        ResourcesPlugin.getWorkspace().getRoot().refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
        IProjectDescription description = projectObject.getDescription();
        if (!description.hasNature(TMCNewProjectNature.NATURE_ID)) {
            setNatures(description);
            try {
                projectObject.setDescription(description, new NullProgressMonitor());
            } catch (ResourceException re) {
                Core.getErrorHandler().handleException(re);
            }
        }
    }

    private void setNatures(IProjectDescription description) {
        String[] prevNatures = description.getNatureIds();
        String[] newNatures = new String[prevNatures.length + 1];
        System.arraycopy(prevNatures, 0, newNatures, 0, prevNatures.length);
        newNatures[prevNatures.length] = TMCNewProjectNature.NATURE_ID;
        String temp = newNatures[newNatures.length - 1];
        newNatures[newNatures.length - 1] = newNatures[0];
        newNatures[0] = temp;
        description.setNatureIds(newNatures);
    }

    private IProject openCProject(Project project, ProjectType projectType) throws OperationCanceledException,
            URISyntaxException, CoreException {

        return new CProjectOpener(project.getRootPath(), project.getExercise().getName()).importAndOpen();

    }

    private IProject openMavenProject(Project project, ProjectType projectType) throws CoreException, IOException {

        return new MavenProjectOpener(FileUtil.append(project.getRootPath(), projectType.getBuildFile()))
                .importAndOpen();

    }

    private IProject openAntProject(Project project, ProjectType projectType) throws CoreException {
        return new AntProjectOpener(FileUtil.append(project.getRootPath(), ".project")).importAndOpen();

    }

}
