package fi.helsinki.cs.plugin.tmc.io;

import java.util.ArrayList;
import java.util.List;

import fi.helsinki.cs.plugin.tmc.domain.Project;
import fi.helsinki.cs.plugin.tmc.services.ProjectDAO;

public class ProjectScanner {

    private ProjectDAO projectDAO;

    public ProjectScanner(ProjectDAO projectDAO) {
        this.projectDAO = projectDAO;
    }

    public void updateProjects() {
        for (Project project : projectDAO.getProjects()) {
            List<String> files = new ArrayList<String>();
            traverse(files, new FileIO(project.getRootPath()));

            project.setProjectFiles(files);
            
            project.getExercise().setDownloaded(project.existsOnDisk());
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
