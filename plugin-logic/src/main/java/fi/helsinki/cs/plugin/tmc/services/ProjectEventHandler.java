package fi.helsinki.cs.plugin.tmc.services;

import java.util.ArrayList;
import java.util.List;

import fi.helsinki.cs.plugin.tmc.domain.Project;
import fi.helsinki.cs.plugin.tmc.spyware.ChangeType;
import fi.helsinki.cs.plugin.tmc.spyware.SnapshotInfo;

public class ProjectEventHandler {

    private ProjectDAO projectDAO;

    public ProjectEventHandler(ProjectDAO projectDAO) {
        this.projectDAO = projectDAO;
    }

    public void handle(SnapshotInfo snapshot) {
        Project project = findProject(snapshot);

        if (project == null) {
            return;
        }

        switch (snapshot.getChangeType()) {

        case FILE_CREATE:
        case FOLDER_CREATE:
            handleCreate(project, snapshot);
            break;

        case FILE_DELETE:
            handleFileDelete(project, snapshot);
            break;

        case FOLDER_DELETE:
            handleFolderDelete(project, snapshot);
            break;

        case FILE_RENAME:
            handleFileRename(project, snapshot);
            break;

        case FOLDER_RENAME:
            handleFolderRename(project, snapshot);
            break;

        default:
            break;

        }
    }

    private Project findProject(SnapshotInfo snapshot) {
        Project project;

        if (snapshot.getChangeType() == ChangeType.FILE_RENAME || snapshot.getChangeType() == ChangeType.FOLDER_RENAME) {
            project = projectDAO.getProjectByFile(snapshot.getOldFullFilePath());
        } else {
            project = projectDAO.getProjectByFile(snapshot.getCurrentFullFilePath());
        }

        if (project != null) {
            return project;
        }

        for (Project p : projectDAO.getProjects()) {
            if (snapshot.getCurrentFullFilePath().startsWith(p.getRootPath())) {
                return p;
            }
        }

        return null;
    }

    private void handleFolderRename(Project project, SnapshotInfo snapshot) {
        for (String file : getChildren(project, snapshot.getOldFullFilePath())) {
            remove(project, file);
            add(project, file.replace(snapshot.getOldFullFilePath(), snapshot.getCurrentFullFilePath()));
        }
        remove(project, snapshot.getOldFullFilePath());
        add(project, snapshot.getCurrentFullFilePath());
    }

    private void handleFileRename(Project project, SnapshotInfo snapshot) {
        remove(project, snapshot.getOldFullFilePath());
        add(project, snapshot.getCurrentFullFilePath());
    }

    private void handleFolderDelete(Project project, SnapshotInfo snapshot) {
        for (String file : getChildren(project, snapshot.getCurrentFullFilePath())) {
            remove(project, file);
        }
        remove(project, snapshot.getCurrentFullFilePath());
    }

    private void handleFileDelete(Project project, SnapshotInfo snapshot) {
        remove(project, snapshot.getCurrentFullFilePath());
    }

    private void handleCreate(Project project, SnapshotInfo snapshot) {
        add(project, snapshot.getCurrentFullFilePath());
    }

    private void add(Project project, String path) {
        List<String> files = project.getProjectFiles();
        files.add(path);
        project.setProjectFiles(files);
    }

    private void remove(Project project, String path) {
        List<String> files = project.getProjectFiles();
        for (int i = 0; i < files.size(); i++) {
            if (files.get(i).equals(path)) {
                files.remove(i);
                return;
            }
        }
        project.setProjectFiles(files);
    }

    private List<String> getChildren(Project project, String path) {
        List<String> files = new ArrayList<String>();
        for (String file : project.getProjectFiles()) {
            if (file.startsWith(path + "/")) {
                files.add(file);
            }
        }
        return files;
    }

}
