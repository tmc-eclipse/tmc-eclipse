package tmc.services;

import java.io.IOException;
import java.net.URISyntaxException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.OperationCanceledException;

import fi.helsinki.cs.plugin.tmc.Core;
import fi.helsinki.cs.plugin.tmc.TMCErrorHandler;
import fi.helsinki.cs.plugin.tmc.async.tasks.ProjectOpener;
import fi.helsinki.cs.plugin.tmc.domain.Exercise;
import fi.helsinki.cs.plugin.tmc.domain.Project;
import fi.helsinki.cs.plugin.tmc.domain.ProjectType;
import fi.helsinki.cs.plugin.tmc.services.ProjectDAO;

public class GenericProjectOpener implements ProjectOpener {
    ProjectDAO projectDAO;
    TMCErrorHandler errorHandler;

    public GenericProjectOpener() {
        projectDAO = Core.getProjectDAO();
        errorHandler = Core.getErrorHandler();
    }

    public void open(Exercise e) {

        Project p = projectDAO.getProjectByExercise(e);
        ProjectType pt = p.getProjectType();

        try {
            switch (pt.getBuildFile()) {
            case ("build.xml"):
                openAntProject(e);
                break;
            case ("pom.xml"):
                openMavenProject(e);
                break;
            case ("Makefile"):
                openCProject(e);
                break;

            }
        } catch (Exception exception) {
            errorHandler.handleException(exception);
        }

    }

    private void openCProject(Exercise e) throws OperationCanceledException, URISyntaxException, CoreException {

        new CProjectOpener(Core.getSettings().getExerciseFilePath() + "/" + Core.getSettings().getCurrentCourseName()
                + parsePath(e.getName()), e.getName()).importAndOpen();

    }

    private void openMavenProject(Exercise e) throws CoreException, IOException {

        new MavenProjectOpener(Core.getSettings().getExerciseFilePath() + "/"
                + Core.getSettings().getCurrentCourseName() + parsePath(e.getName()) + "pom.xml").importAndOpen();

    }

    private void openAntProject(Exercise e) throws CoreException {

        new AntProjectOpener(Core.getSettings().getExerciseFilePath() + "/" + Core.getSettings().getCurrentCourseName()
                + parsePath(e.getName()) + ".project").importAndOpen();

    }

    public String parsePath(String path) {
        String[] parts = path.split("-");
        String toReturn = "/";
        for (int i = 0; i < parts.length; i++) {
            toReturn += parts[i] + "/";
        }
        return toReturn;
    }

}
