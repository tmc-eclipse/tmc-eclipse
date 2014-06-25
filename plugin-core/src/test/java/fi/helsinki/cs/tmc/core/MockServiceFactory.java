package fi.helsinki.cs.tmc.core;

import static org.mockito.Mockito.mock;
import fi.helsinki.cs.tmc.core.io.IOFactory;
import fi.helsinki.cs.tmc.core.services.CourseDAO;
import fi.helsinki.cs.tmc.core.services.ProjectDAO;
import fi.helsinki.cs.tmc.core.services.ProjectEventHandler;
import fi.helsinki.cs.tmc.core.services.ReviewDAO;
import fi.helsinki.cs.tmc.core.services.Settings;
import fi.helsinki.cs.tmc.core.services.Updater;
import fi.helsinki.cs.tmc.core.services.http.ServerManager;
import fi.helsinki.cs.tmc.core.spyware.SpywarePluginLayer;

public class MockServiceFactory implements ServiceFactory {

    private Settings settings;
    private CourseDAO courseDAO;
    private ProjectDAO projectDAO;
    private ReviewDAO reviewDAO;
    private ServerManager serverManager;
    private Updater updater;
    private SpywarePluginLayer spywarePluginLayer;
    private ProjectEventHandler eventHandler;
    private IOFactory io;

    public MockServiceFactory() {
        settings = mock(Settings.class);
        courseDAO = mock(CourseDAO.class);
        projectDAO = mock(ProjectDAO.class);
        reviewDAO = mock(ReviewDAO.class);
        serverManager = mock(ServerManager.class);
        updater = mock(Updater.class);
        spywarePluginLayer = mock(SpywarePluginLayer.class);
        eventHandler = mock(ProjectEventHandler.class);
        io = mock(IOFactory.class);
    }

    @Override
    public Settings getSettings() {
        return settings;
    }

    @Override
    public CourseDAO getCourseDAO() {
        return courseDAO;
    }

    @Override
    public ProjectDAO getProjectDAO() {
        return projectDAO;
    }

    @Override
    public ServerManager getServerManager() {
        return serverManager;
    }

    @Override
    public Updater getUpdater() {
        return updater;
    }

    @Override
    public SpywarePluginLayer getSpyware() {
        return spywarePluginLayer;
    }

    @Override
    public ProjectEventHandler getProjectEventHandler() {
        return eventHandler;
    }

    @Override
    public ReviewDAO getReviewDAO() {
        return reviewDAO;
    }

    @Override
    public IOFactory getIOFactory() {
        return io;
    }

}
