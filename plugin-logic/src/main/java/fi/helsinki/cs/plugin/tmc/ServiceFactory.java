package fi.helsinki.cs.plugin.tmc;

import fi.helsinki.cs.plugin.tmc.services.CourseDAO;
import fi.helsinki.cs.plugin.tmc.services.ProjectDAO;
import fi.helsinki.cs.plugin.tmc.services.ProjectEventHandler;
import fi.helsinki.cs.plugin.tmc.services.Settings;
import fi.helsinki.cs.plugin.tmc.services.Updater;
import fi.helsinki.cs.plugin.tmc.services.http.ServerManager;
import fi.helsinki.cs.plugin.tmc.spyware.SpywarePluginLayer;

public interface ServiceFactory {

    public Settings getSettings();

    public CourseDAO getCourseDAO();

    public ProjectDAO getProjectDAO();

    public ServerManager getServerManager();

    public Updater getUpdater();

    public SpywarePluginLayer getSpyware();

    public ProjectEventHandler getProjectEventHandler();

}
