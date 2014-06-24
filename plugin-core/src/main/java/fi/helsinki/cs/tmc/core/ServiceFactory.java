package fi.helsinki.cs.tmc.core;

/**
 * Interface to service factory that handles the creation of various objects for the Core. 
 * Makes unit testing easier as it's possible to provide mock implementations of this and 
 * avoid unnecessary dependencies to, for example, file system
 *
 */

import fi.helsinki.cs.tmc.core.services.CourseDAO;
import fi.helsinki.cs.tmc.core.services.ProjectDAO;
import fi.helsinki.cs.tmc.core.services.ProjectEventHandler;
import fi.helsinki.cs.tmc.core.services.ReviewDAO;
import fi.helsinki.cs.tmc.core.services.Settings;
import fi.helsinki.cs.tmc.core.services.Updater;
import fi.helsinki.cs.tmc.core.services.http.ServerManager;
import fi.helsinki.cs.tmc.core.spyware.SpywarePluginLayer;


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
