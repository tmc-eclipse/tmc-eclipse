package fi.helsinki.cs.plugin.tmc;

/**
 * Interface to service factory that handles the creation of various objects for the Core. 
 * Makes unit testing easier as it's possible to provide mock implementations of this and 
 * avoid unnecessary dependencies to, for example, file system
 *
 */

import fi.helsinki.cs.plugin.tmc.services.CourseDAO;
import fi.helsinki.cs.plugin.tmc.services.ProjectDAO;
import fi.helsinki.cs.plugin.tmc.services.ProjectEventHandler;
import fi.helsinki.cs.plugin.tmc.services.ReviewDAO;
import fi.helsinki.cs.plugin.tmc.services.Settings;
import fi.helsinki.cs.plugin.tmc.services.Updater;
import fi.helsinki.cs.plugin.tmc.services.http.ServerManager;
import fi.helsinki.cs.plugin.tmc.spyware.SpywarePluginLayer;


public interface ServiceFactory {

    public Settings getSettings();

    public CourseDAO getCourseDAO();

    public ProjectDAO getProjectDAO();

    public ReviewDAO getReviewDAO();

    public ServerManager getServerManager();

    public Updater getUpdater();

    public SpywarePluginLayer getSpyware();

    public ProjectEventHandler getProjectEventHandler();

}
