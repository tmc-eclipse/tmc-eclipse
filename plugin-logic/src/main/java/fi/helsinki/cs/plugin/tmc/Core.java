package fi.helsinki.cs.plugin.tmc;

import fi.helsinki.cs.plugin.tmc.services.CourseFetcher;
import fi.helsinki.cs.plugin.tmc.services.Settings;
import fi.helsinki.cs.plugin.tmc.services.web.UserVisibleException;

public class Core {
	
	private ProductionFactory factory;

	public Core() throws UserVisibleException {
		this.factory = new ProductionFactory();
	}
	
	public Settings getSettings(){
		return factory.getSettings();
	}
	
	public CourseFetcher getCourseFetcher(){
		return factory.getCourseFetcher();
	}
	
}
