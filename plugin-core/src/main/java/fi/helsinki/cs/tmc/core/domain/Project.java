package fi.helsinki.cs.tmc.core.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fi.helsinki.cs.tmc.core.io.FileUtil;
import fi.helsinki.cs.tmc.core.io.zipper.zippingdecider.DefaultZippingDecider;
import fi.helsinki.cs.tmc.core.io.zipper.zippingdecider.MavenZippingDecider;
import fi.helsinki.cs.tmc.core.io.zipper.zippingdecider.ZippingDecider;

/**
 * A domain class for IDE independent data storage of project data, such as it's
 * path, status and files. It is similar to the Exercise class, but Exercise
 * class is meant to reflect server side information while Project class is
 * supposed to track local information.
 */
public class Project {

    private Exercise exercise;
    private List<String> projectFiles;
    private List<String> extraStudentFiles;
    private String rootPath;
    private ProjectStatus status;

    public Project(Exercise exercise) {
        this(exercise, new ArrayList<String>());
    }

    public Project(Exercise exercise, List<String> projectFiles) {
        this.exercise = exercise;
        this.projectFiles = projectFiles;
        this.extraStudentFiles = Collections.emptyList();
        this.rootPath = buildRootPath();
        this.status = ProjectStatus.NOT_DOWNLOADED;

        exercise.setProject(this);
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
        ProjectType type;
        synchronized (projectFiles) {
            List<String> files = new ArrayList<String>(projectFiles);
            type = ProjectType.findProjectType(files);
        }
        return type;
    }

    public boolean containsFile(String file) {
        if (file == null || rootPath.isEmpty()) {
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

        return exercise.hashCode();
    }

    private String buildRootPath() {
        ProjectType type = getProjectType();
        if (type == null) {
            return "";
        }
        synchronized (projectFiles) {
            for (String file : projectFiles) {
                if (file.contains(type.getBuildFile())) {
                    return FileUtil.getUnixPath(file.replace(type.getBuildFile(), ""));
                }
            }
        }
        return "";
    }

    public ZippingDecider getZippingDecider() {
        switch (getProjectType()) {
        case JAVA_MAVEN:
            return new MavenZippingDecider(this);
        case JAVA_ANT:
            return new DefaultZippingDecider(this);
        case MAKEFILE:
            return new DefaultZippingDecider(this);
        default:
            throw new InvalidProjectTypeException("Invalid project type");
        }
    }

    public void setProjectFiles(List<String> files) {
        synchronized (projectFiles) {
            projectFiles = files;
            this.rootPath = buildRootPath();
        }
    }

    public List<String> getReadOnlyProjectFiles() {
        return Collections.unmodifiableList(projectFiles);
    }

    public String getProjectFileByName(String fileName) {
        for (String s : projectFiles) {
            if (s.contains(fileName) && !s.contains("/target/")) {
                return s;
            }
        }
        return "";
    }

    public void addProjectFile(String file) {
        synchronized (projectFiles) {
            projectFiles.add(file);
        }
    }

    public void removeProjectFile(String path) {
        synchronized (projectFiles) {
            projectFiles.remove(path);
        }
    }

    public void setExtraStudentFiles(List<String> files) {
        extraStudentFiles = files;
    }

    public List<String> getExtraStudentFiles() {
        return extraStudentFiles;
    }

    public boolean existsOnDisk() {
        synchronized (projectFiles) {

            for (String file : projectFiles) {
                if (file.replace(getRootPath(), "").contains("/src/")) {
                    return true;
                }
            }
        }
        return false;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
    }

}
