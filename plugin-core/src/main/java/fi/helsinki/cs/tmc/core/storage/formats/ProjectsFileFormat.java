package fi.helsinki.cs.tmc.core.storage.formats;

import java.util.List;

import fi.helsinki.cs.tmc.core.domain.Project;

public class ProjectsFileFormat {

    private List<Project> projects;

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

}
