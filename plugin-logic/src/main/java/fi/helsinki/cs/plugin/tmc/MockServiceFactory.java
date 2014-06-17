package fi.helsinki.cs.plugin.tmc;

import static org.mockito.Mockito.mock;
import fi.helsinki.cs.plugin.tmc.services.CourseDAO;
import fi.helsinki.cs.plugin.tmc.services.ProjectDAO;
import fi.helsinki.cs.plugin.tmc.services.ProjectEventHandler;
import fi.helsinki.cs.plugin.tmc.services.Settings;
import fi.helsinki.cs.plugin.tmc.services.Updater;
import fi.helsinki.cs.plugin.tmc.services.http.ServerManager;
import fi.helsinki.cs.plugin.tmc.spyware.SpywarePluginLayer;

public class MockServiceFactory implements ServiceFactory {

    private Settings settings;
    private CourseDAO courseDAO;
    private ProjectDAO projectDAO;
    private ServerManager serverManager;
    private Updater updater;
    private SpywarePluginLayer spywarePluginLayer;
    private ProjectEventHandler eventHandler;

    public MockServiceFactory() {
        settings = mock(Settings.class);
        courseDAO = mock(CourseDAO.class);
        projectDAO = mock(ProjectDAO.class);
        serverManager = mock(ServerManager.class);
        updater = mock(Updater.class);
        spywarePluginLayer = mock(SpywarePluginLayer.class);
        eventHandler = mock(ProjectEventHandler.class);

    }

    @Override
    public Settings getSettings() {
        return settings;
    }

    @Override
    public CourseDAO getCourseDAO() {
        // TODO Auto-generated method stub
        return courseDAO;
    }

    @Override
    public ProjectDAO getProjectDAO() {
        // TODO Auto-generated method stub
        return projectDAO;
    }

    @Override
    public ServerManager getServerManager() {
        // TODO Auto-generated method stub
        return serverManager;
    }

    @Override
    public Updater getUpdater() {
        // TODO Auto-generated method stub
        return updater;
    }

    @Override
    public SpywarePluginLayer getSpyware() {
        // TODO Auto-generated method stub
        return spywarePluginLayer;
    }

    @Override
    public ProjectEventHandler getProjectEventHandler() {
        // TODO Auto-generated method stub
        return eventHandler;
    }

}
