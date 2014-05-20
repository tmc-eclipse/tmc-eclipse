package fi.helsinki.cs.plugin.tmc.services.web;

import org.apache.commons.io.IOUtils;

import fi.helsinki.cs.plugin.tmc.Core;
import fi.helsinki.cs.plugin.tmc.services.http.HttpRequestExecutor;
import fi.helsinki.cs.plugin.tmc.services.http.ServerAccess;
import fi.helsinki.cs.plugin.tmc.ui.UserVisibleException;

public class JsonGetter {

	public String getJson(String url) {
		ServerAccess request = new ServerAccess();
		HttpRequestExecutor exec = request.createHttpTasks().createExecutor(request.getUrl(url));
		
		try {
			return IOUtils.toString(exec.call().getContent());
		} catch(Exception e) {
			Core.getErrorHandler().handleException(new UserVisibleException("Error retrieving JSON data from " + url));
			//throw new UserVisibleException("Error retrieving JSON data from " + url);
			return "";
		}
	}
	
}
