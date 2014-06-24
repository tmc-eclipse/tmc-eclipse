package fi.helsinki.cs.tmc.core.io;

import java.util.ArrayList;
import java.util.List;

import fi.helsinki.cs.tmc.core.domain.Project;
import fi.helsinki.cs.tmc.core.domain.ProjectStatus;
import fi.helsinki.cs.tmc.core.services.ProjectDAO;

/**
 * Class that is used on startup to ensure that the project database remains in
 * consistent state even if files are deleted or added on disk while IDE is
 * closed
 * 
 */
public class ProjectScanner {

    private ProjectDAO projectDAO;

    public ProjectScanner(ProjectDAO projectDAO) {
        this.projectDAO = projectDAO;
    }

    public void updateProject(Project project) {
        if (project.getStatus() == ProjectStatus.DELETED) {
            return;
        }

        List<String> files = new ArrayList<String>();
        traverse(files, new FileIO(project.getRootPath()));

        project.setProjectFiles(files);

        if (project.existsOnDisk()) {
            project.setStatus(ProjectStatus.DOWNLOADED);
        } else {
            project.setStatus(ProjectStatus.NOT_DOWNLOADED);
        }
    }

    public void updateProjects() {
        for (Project project : projectDAO.getProjects()) {
            updateProject(project);
        }
    }

    private void traverse(List<String> list, FileIO file) {
        if (file != null && (file.fileExists() || file.directoryExists())) {
            list.add(file.getPath());

            if (file.directoryExists()) {
                for (FileIO child : file.getChildren()) {
                    traverse(list, child);
                }
            }
        }
    }

}
