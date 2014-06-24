package fi.helsinki.cs.tmc.core;

import fi.helsinki.cs.tmc.core.async.BackgroundTaskRunner;
import fi.helsinki.cs.tmc.core.services.CourseDAO;
import fi.helsinki.cs.tmc.core.services.ProjectDAO;
import fi.helsinki.cs.tmc.core.services.ProjectEventHandler;
import fi.helsinki.cs.tmc.core.services.ReviewDAO;
import fi.helsinki.cs.tmc.core.services.Settings;
import fi.helsinki.cs.tmc.core.services.Updater;
import fi.helsinki.cs.tmc.core.services.http.ServerManager;
import fi.helsinki.cs.tmc.core.spyware.SpywarePluginLayer;

/**
 * This class serves as an interface to the IDE plugin. None of these methods
 * should be called from core as this introduces annoying hidden dependencies
 * that make unit testing really really painful (trust me, been there, done
 * that, had to refactor code).
 */
public final class Core {

    private static Core core;

    private TMCErrorHandler errorHandler;
    private BackgroundTaskRunner taskRunner;
    private Settings settings;
    private SpywarePluginLayer spyware;

    private CourseDAO courseDAO;
    private ProjectDAO projectDAO;
    private ReviewDAO reviewDAO;

    private ServerManager server;

    private Updater updater;

    private ProjectEventHandler projectEventHandler;

    private Core(ServiceFactory factory) {
        this.settings = factory.getSettings();
        this.courseDAO = factory.getCourseDAO();
        this.projectDAO = factory.getProjectDAO();
        this.reviewDAO = factory.getReviewDAO();
        this.server = factory.getServerManager();
        this.updater = factory.getUpdater();
        this.errorHandler = new DummyErrorHandler();
        this.spyware = factory.getSpyware();
        this.projectEventHandler = factory.getProjectEventHandler();
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

    public static ReviewDAO getReviewDAO() {
        return Core.getInstance().reviewDAO;
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

    public static ProjectEventHandler getProjectEventHandler() {
        return Core.getInstance().projectEventHandler;
    }

    public static Core getInstance() {
        if (core == null) {
            return getInstance(new ServiceFactoryImpl());
        }
        return core;
    }

    public static Core getInstance(ServiceFactory factory) {
        if (core == null) {
            core = new Core(factory);
        }
        return core;
    }
}
