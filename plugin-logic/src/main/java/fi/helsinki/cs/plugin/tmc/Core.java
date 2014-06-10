package fi.helsinki.cs.plugin.tmc;

import fi.helsinki.cs.plugin.tmc.async.BackgroundTaskRunner;
import fi.helsinki.cs.plugin.tmc.services.CourseDAO;
import fi.helsinki.cs.plugin.tmc.services.ProjectDAO;
import fi.helsinki.cs.plugin.tmc.services.Settings;
import fi.helsinki.cs.plugin.tmc.services.Updater;
import fi.helsinki.cs.plugin.tmc.services.http.ServerManager;
import fi.helsinki.cs.plugin.tmc.spyware.SpywarePluginLayer;

public final class Core {

    private static Core core;

    private TMCErrorHandler errorHandler;
    private BackgroundTaskRunner taskRunner;
    private Settings settings;
    private SpywarePluginLayer spyware;

    private CourseDAO courseDAO;
    private ProjectDAO projectDAO;

    private ServerManager server;

    private Updater updater;

    private Core() {
        ServiceFactory factory = new ServiceFactory();
        this.settings = factory.getSettings();
        this.courseDAO = factory.getCourseDAO();
        this.projectDAO = factory.getProjectDAO();
        this.server = factory.getServerManager();
        this.updater = factory.getUpdater();
        this.errorHandler = new DummyErrorHandler();
        this.spyware = factory.getSpyware();

    }

    public static void setErrorHandler(TMCErrorHandler errorHandler) {
        Core.getInstance().errorHandler = errorHandler;
    }

    public static TMCErrorHandler getErrorHandler() {
        return Core.getInstance().errorHandler;
    }

    public static void setTaskRunner(BackgroundTaskRunner taskRunner) {
        Core.getInstance().taskRunner = taskRunner;
    }

    public static BackgroundTaskRunner getTaskRunner() {
        return Core.getInstance().taskRunner;
    }

    public static Settings getSettings() {
        return Core.getInstance().settings;
    }

    public static CourseDAO getCourseDAO() {
        return Core.getInstance().courseDAO;
    }

    public static ProjectDAO getProjectDAO() {
        return Core.getInstance().projectDAO;
    }

    public static ServerManager getServerManager() {
        return Core.getInstance().server;
    }

    public static Updater getUpdater() {
        return Core.getInstance().updater;
    }

    public static SpywarePluginLayer getSpyware() {
        return Core.getInstance().spyware;
    }

    public static Core getInstance() {
        if (core == null) {
            core = new Core();
        }
        return core;
    }
}
