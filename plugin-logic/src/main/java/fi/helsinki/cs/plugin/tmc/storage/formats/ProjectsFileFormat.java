package fi.helsinki.cs.plugin.tmc.storage.formats;

import java.util.List;

import fi.helsinki.cs.plugin.tmc.domain.Project;

public class ProjectsFileFormat {

    private List<Project> projects;

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

}
