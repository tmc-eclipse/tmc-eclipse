package fi.helsinki.cs.tmc.core.services;

import java.util.ArrayList;
import java.util.List;

import fi.helsinki.cs.tmc.core.domain.Project;
import fi.helsinki.cs.tmc.core.domain.ProjectStatus;
import fi.helsinki.cs.tmc.core.spyware.ChangeType;
import fi.helsinki.cs.tmc.core.spyware.SnapshotInfo;

/**
 * 
 * Class that responds to events such as renames, deletes and additions. Ensures
 * that project database remains consistent.
 * 
 */
public class ProjectEventHandler {

    private ProjectDAO projectDAO;

    public ProjectEventHandler(ProjectDAO projectDAO) {
        this.projectDAO = projectDAO;
    }

    public void handleDeletion(String projectPath) {
        Project project = projectDAO.getProjectByFile(projectPath);
        if (project == null) {
            return;
        }

        project.setProjectFiles(new ArrayList<String>());
        project.setStatus(ProjectStatus.DELETED);
    }

    public void handleSnapshot(SnapshotInfo snapshot) {
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

        if (project.existsOnDisk()) {
            project.setStatus(ProjectStatus.DOWNLOADED);
        } else {
            project.setStatus(ProjectStatus.NOT_DOWNLOADED);
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
            if (!p.getRootPath().isEmpty() && snapshot.getCurrentFullFilePath().startsWith(p.getRootPath())) {
                return p;
            }
        }

        return null;
    }

    private void handleFolderRename(Project project, SnapshotInfo snapshot) {
        if (isProjectRename(snapshot)) {
            return;
        }

        List<String> children = getChildren(project, snapshot.getOldFullFilePath());
        for (String file : children) {
            remove(project, file);
            add(project, file.replace(snapshot.getOldFullFilePath(), snapshot.getCurrentFullFilePath()));
        }
        remove(project, snapshot.getOldFullFilePath());
        add(project, snapshot.getCurrentFullFilePath());
    }

    private boolean isProjectRename(SnapshotInfo snapshot) {
        return snapshot.getOldFullFilePath().isEmpty();
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
        project.addProjectFile(path);
    }

    private void remove(Project project, String path) {
        project.removeProjectFile(path);
    }

    private List<String> getChildren(Project project, String path) {
        List<String> files = new ArrayList<String>();
        List<String> projectFiles = project.getReadOnlyProjectFiles();
        for (String file : projectFiles) {
            if (file.startsWith(path + "/")) {
                files.add(file);
            }
        }
        return files;
    }

}
