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
import fi.helsinki.cs.plugin.tmc.io.FileUtil;

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
		try {
			switch (project.getProjectType()) {
			case JAVA_ANT:
				openAntProject(project, projectType);
				break;
			case JAVA_MAVEN:
				openMavenProject(project, projectType);
				break;
			case MAKEFILE:
				openCProject(project, projectType);
				break;

			}
		} catch (Exception exception) {
			errorHandler.handleException(exception);
		}

	}

	private void openCProject(Project project, ProjectType projectType)
			throws OperationCanceledException, URISyntaxException,
			CoreException {

		new CProjectOpener(project.getRootPath(), project.getExercise()
				.getName()).importAndOpen();

	}

	private void openMavenProject(Project project, ProjectType projectType)
			throws CoreException, IOException {

		new MavenProjectOpener(FileUtil.append(project.getRootPath(),
				projectType.getBuildFile())).importAndOpen();

	}

	private void openAntProject(Project project, ProjectType projectType)
			throws CoreException {
		new AntProjectOpener(FileUtil.append(project.getRootPath(), ".project"))
				.importAndOpen();

	}

}
