package fi.helsinki.cs.plugin.tmc;

import fi.helsinki.cs.plugin.tmc.domain.Course;
import fi.helsinki.cs.plugin.tmc.domain.Project;
import fi.helsinki.cs.plugin.tmc.io.FileIO;
import fi.helsinki.cs.plugin.tmc.services.CourseDAO;
import fi.helsinki.cs.plugin.tmc.services.CourseFetcher;
import fi.helsinki.cs.plugin.tmc.services.ExerciseFetcher;
import fi.helsinki.cs.plugin.tmc.services.ProjectDAO;
import fi.helsinki.cs.plugin.tmc.services.Settings;
import fi.helsinki.cs.plugin.tmc.services.http.ServerManager;
import fi.helsinki.cs.plugin.tmc.storage.CourseStorage;
import fi.helsinki.cs.plugin.tmc.storage.DataSource;
import fi.helsinki.cs.plugin.tmc.storage.ProjectStorage;

public final class ServiceFactory {

    public static final String LOCAL_COURSES_PATH = "courses.tmp";
    public static final String LOCAL_PROJECTS_PATH = "projects.tmp";

    private Settings settings;
    private CourseDAO courseDAO;
    private ProjectDAO projectDAO;
    private CourseFetcher courseFetcher;
    private ExerciseFetcher exerciseFetcher;
    private ServerManager server;

    public ServiceFactory() {
        this.server = new ServerManager();
        this.settings = Settings.getDefaultSettings();

        FileIO coursesFile = new FileIO(LOCAL_COURSES_PATH);
        DataSource<Course> courseStorage = new CourseStorage(coursesFile);
        this.courseDAO = new CourseDAO(courseStorage);

        FileIO projectsFile = new FileIO(LOCAL_PROJECTS_PATH);
        DataSource<Project> projectStorage = new ProjectStorage(projectsFile);
        this.projectDAO = new ProjectDAO(projectStorage);

        this.courseFetcher = new CourseFetcher(server, courseDAO);
        this.exerciseFetcher = new ExerciseFetcher(server, courseDAO);
    }

    public Settings getSettings() {
        return settings;
    }

    public CourseDAO getCourseDAO() {
        return courseDAO;
    }

    public ProjectDAO getProjectDAO() {
        return projectDAO;
    }

    public CourseFetcher getCourseFetcher() {
        return courseFetcher;
    }

    public ExerciseFetcher getExerciseFetcher() {
        return exerciseFetcher;
    }

    public ServerManager getServerManager() {
        return server;
    }

}
