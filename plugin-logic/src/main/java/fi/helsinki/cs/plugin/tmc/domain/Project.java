package fi.helsinki.cs.plugin.tmc.domain;

import java.util.List;

public class Project {

    private Exercise exercise;
    private List<String> projectFiles;
    private String rootPath;

    public Project(Exercise exercise, List<String> projectFiles) {
        this.exercise = exercise;
        this.projectFiles = projectFiles;

        this.rootPath = buildRootPath();
    }

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    public String getRootPath() {
        return rootPath;
    }

    public ProjectType getProjectType() {
        return ProjectType.findProjectType(projectFiles);
    }

    public boolean containsFile(String file) {
    	if (file == null){
    		return false;
    	}
        return file.contains(rootPath);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Project)) {
            return false;
        }

        Project p = (Project) o;
        if (this.exercise == null || p.exercise == null) {
            return false;
        }

        return this.exercise.equals(p.exercise);
    }

    @Override
    public int hashCode() {
        if (exercise == null) {
            return 0;
        }

        return exercise.hashCode();
    }

    private String buildRootPath() {
        ProjectType type = getProjectType();
        if (type == null){
        	return "";
        }
        
        for (String file : projectFiles) {
            if (file.contains(type.getBuildFile())) {
                return file.replace(type.getBuildFile(), "");
            }
        }
        return "";
    }

}
