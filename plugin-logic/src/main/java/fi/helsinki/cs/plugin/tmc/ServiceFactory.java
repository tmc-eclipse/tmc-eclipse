package fi.helsinki.cs.plugin.tmc;

import fi.helsinki.cs.plugin.tmc.services.CourseDAO;
import fi.helsinki.cs.plugin.tmc.services.DAOManager;
import fi.helsinki.cs.plugin.tmc.services.ProjectDAO;
import fi.helsinki.cs.plugin.tmc.services.Settings;
import fi.helsinki.cs.plugin.tmc.services.Updater;
import fi.helsinki.cs.plugin.tmc.services.http.ServerManager;

public final class ServiceFactory {

    private Settings settings;
    private CourseDAO courseDAO;
    private ProjectDAO projectDAO;
    private ServerManager server;
    private Updater updater;

    public ServiceFactory() {
        this.settings = Settings.getDefaultSettings();
        this.server = new ServerManager(settings);

        DAOManager manager = new DAOManager();
        this.courseDAO = manager.getCourseDAO();
        this.projectDAO = manager.getProjectDAO();

        this.updater = new Updater(server, courseDAO, projectDAO);
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

    public ServerManager getServerManager() {
        return server;
    }

    public Updater getUpdater() {
        return updater;
    }

}
