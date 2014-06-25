package fi.helsinki.cs.tmc.core.services;

import java.util.List;

import fi.helsinki.cs.tmc.core.domain.Exercise;
import fi.helsinki.cs.tmc.core.domain.Project;
import fi.helsinki.cs.tmc.core.domain.ProjectStatus;
import fi.helsinki.cs.tmc.core.storage.DataSource;

/**
 * Class that handles Project object storage, loading and saving.
 */
public class ProjectDAO {

    private DataSource<Project> dataSource;
    private List<Project> projects;

    public ProjectDAO(DataSource<Project> dataSource) {
        this.dataSource = dataSource;
        loadProjects();
    }

    public void loadProjects() {
        this.projects = dataSource.load();
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public void addProject(Project project) {
        if (projects.contains(project)) {
            projects.remove(project);
        }
        projects.add(project);
        save();
    }

    public Project getProjectByFile(String filePath) {
        for (Project project : projects) {
            if (project.containsFile(filePath) && project.getStatus() != ProjectStatus.DELETED) {
                return project;
            }
        }
        return null;
    }

    public Project getProjectByExercise(Exercise exercise) {
        for (Project project : projects) {
            if (project.getExercise().equals(exercise)) {
                return project;
            }
        }
        return null;
    }

    public void save() {
        dataSource.save(projects);
    }

}
