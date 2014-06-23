package fi.helsinki.cs.plugin.tmc.services;

import java.util.List;

import fi.helsinki.cs.plugin.tmc.domain.Exercise;
import fi.helsinki.cs.plugin.tmc.domain.Project;
import fi.helsinki.cs.plugin.tmc.storage.DataSource;

/**
 * 
 * Class that handles Project object storage, loading and saving
 * 
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
            if (project.containsFile(filePath)) {
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
