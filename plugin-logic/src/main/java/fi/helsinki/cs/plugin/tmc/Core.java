package fi.helsinki.cs.plugin.tmc;

import fi.helsinki.cs.plugin.tmc.services.CourseFetcher;
import fi.helsinki.cs.plugin.tmc.services.Settings;
import fi.helsinki.cs.plugin.tmc.services.web.UserVisibleException;

public class Core {

	private ProductionFactory factory;
	private ErrorHandler errorHandler;

	public Core(ErrorHandler errorHandler) {
		this.errorHandler = errorHandler;
		try {
			this.factory = ProductionFactory.getInstance();
		} catch (UserVisibleException e) {
			errorHandler.handleException(e);
		}
	}

	public ErrorHandler getErrorHandler() {
		return errorHandler;

	}

	public Settings getSettings() {
		return factory.getSettings();
	}

	public CourseFetcher getCourseFetcher() {
		return factory.getCourseFetcher();
	}

}
