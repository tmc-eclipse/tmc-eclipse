package fi.helsinki.cs.plugin.tmc;

import fi.helsinki.cs.plugin.tmc.services.CourseDAO;
import fi.helsinki.cs.plugin.tmc.services.CourseFetcher;
import fi.helsinki.cs.plugin.tmc.services.DAOManager;
import fi.helsinki.cs.plugin.tmc.services.ExerciseFetcher;
import fi.helsinki.cs.plugin.tmc.services.ProjectDAO;
import fi.helsinki.cs.plugin.tmc.services.Settings;
import fi.helsinki.cs.plugin.tmc.services.http.ServerManager;

public final class ServiceFactory {

    private Settings settings;
    private CourseDAO courseDAO;
    private ProjectDAO projectDAO;
    private CourseFetcher courseFetcher;
    private ExerciseFetcher exerciseFetcher;
    private ServerManager server;

    public ServiceFactory() {
        this.settings = Settings.getDefaultSettings();
        this.server = new ServerManager(settings);

        DAOManager manager = new DAOManager();
        this.courseDAO = manager.getCourseDAO();
        this.projectDAO = manager.getProjectDAO();

        this.courseFetcher = new CourseFetcher(server, courseDAO);
        this.exerciseFetcher = new ExerciseFetcher(server, courseDAO, settings);
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
