package fi.helsinki.cs.plugin.tmc.getJson;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import fi.helsinki.cs.plugin.tmc.getJson.web.HttpRequestExecutor;
import fi.helsinki.cs.plugin.tmc.getJson.web.HttpTasks;
import fi.helsinki.cs.plugin.tmc.getJson.web.ServerAccess;


public class JsonGetter {

	
	public String getJson(String url){
		ServerAccess sa = new ServerAccess();
		HttpTasks tasks = sa.createHttpTasks();
		url = sa.getUrl(url);
		HttpRequestExecutor hre =  tasks.createExecutor(url);
		try {
			InputStream is = hre.call().getContent();
			return IOUtils.toString(is);
		} catch (Exception e) {
			System.out.println(e.toString());
			return "";
		}
	}

}
