package tmc.services;

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

import tmc.util.IProjectHelper;
import tmc.util.TMCProjectNature;
import fi.helsinki.cs.plugin.tmc.Core;
import fi.helsinki.cs.plugin.tmc.TMCErrorHandler;
import fi.helsinki.cs.plugin.tmc.async.tasks.ProjectOpener;
import fi.helsinki.cs.plugin.tmc.domain.Exercise;
import fi.helsinki.cs.plugin.tmc.domain.Project;
import fi.helsinki.cs.plugin.tmc.domain.ProjectType;
import fi.helsinki.cs.plugin.tmc.io.FileUtil;
import fi.helsinki.cs.plugin.tmc.services.ProjectDAO;

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

            if (projectObject != null) {
                addTMCProjectNature(projectObject);
            }

        } catch (Exception e1) {
            Core.getErrorHandler().handleException(e1);
            e1.printStackTrace();
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
        if (!description.hasNature(TMCProjectNature.NATURE_ID)) {
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
        newNatures[prevNatures.length] = TMCProjectNature.NATURE_ID;
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
