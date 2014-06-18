package fi.helsinki.cs.plugin.tmc;

import fi.helsinki.cs.plugin.tmc.io.FileIO;
import fi.helsinki.cs.plugin.tmc.services.CourseDAO;
import fi.helsinki.cs.plugin.tmc.services.DAOManager;
import fi.helsinki.cs.plugin.tmc.services.ProjectDAO;
import fi.helsinki.cs.plugin.tmc.services.ProjectEventHandler;
import fi.helsinki.cs.plugin.tmc.services.ReviewDAO;
import fi.helsinki.cs.plugin.tmc.services.Settings;
import fi.helsinki.cs.plugin.tmc.services.Updater;
import fi.helsinki.cs.plugin.tmc.services.http.ServerManager;
import fi.helsinki.cs.plugin.tmc.spyware.SpywarePluginLayer;
import fi.helsinki.cs.plugin.tmc.spyware.services.DocumentChangeHandler;
import fi.helsinki.cs.plugin.tmc.spyware.services.EventReceiver;
import fi.helsinki.cs.plugin.tmc.spyware.services.EventSendBuffer;
import fi.helsinki.cs.plugin.tmc.spyware.services.EventStore;
import fi.helsinki.cs.plugin.tmc.spyware.services.SnapshotTaker;
import fi.helsinki.cs.plugin.tmc.spyware.utility.ActiveThreadSet;

/**
 * Default implementation of ServiceFactory interface. Creates the various
 * objects that Core uses.
 * 
 * @author ekaaria
 * 
 */
public final class ServiceFactoryImpl implements ServiceFactory {

    private Settings settings;
    private CourseDAO courseDAO;
    private ProjectDAO projectDAO;
    private ReviewDAO reviewDAO;

    private ServerManager server;
    private Updater updater;
    private SpywarePluginLayer spyware;
    private ProjectEventHandler projectEventHandler;

    public ServiceFactoryImpl() {
        this.settings = Settings.getDefaultSettings();
        this.server = new ServerManager(settings);

        DAOManager manager = new DAOManager();
        this.courseDAO = manager.getCourseDAO();
        this.projectDAO = manager.getProjectDAO();
        this.reviewDAO = manager.getReviewDAO();

        this.updater = new Updater(server, courseDAO, projectDAO);
        this.projectEventHandler = new ProjectEventHandler(projectDAO);

        EventReceiver receiver = new EventSendBuffer(new EventStore(new FileIO("events.tmp")), settings, server,
                courseDAO);
        ActiveThreadSet set = new ActiveThreadSet();
        SnapshotTaker taker = new SnapshotTaker(set, receiver, settings, projectDAO);
        DocumentChangeHandler handler = new DocumentChangeHandler(receiver, set, settings, projectDAO);

        this.spyware = new SpywarePluginLayer(set, receiver, taker, handler);
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

    public ReviewDAO getReviewDAO() {
        return reviewDAO;
    }

    public ServerManager getServerManager() {
        return server;
    }

    public Updater getUpdater() {
        return updater;
    }

    public SpywarePluginLayer getSpyware() {
        return spyware;
    }

    public ProjectEventHandler getProjectEventHandler() {
        return projectEventHandler;
    }

}
