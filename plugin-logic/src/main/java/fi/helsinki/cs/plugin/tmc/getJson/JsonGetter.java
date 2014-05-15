package fi.helsinki.cs.plugin.tmc.getJson;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;

public class JsonGetter {
	private String bodyText;
	private Gson mapper;

	public JsonGetter() throws IOException, InterruptedException, FailedHttpResponseException {
		ServerAccess sa = new ServerAccess();
		HttpTasks tasks = sa.createHttpTasks();
		String url = sa.getCourseListUrl();
		
		HttpRequestExecutor hre =  tasks.createExecutor(url);
		InputStream is = hre.call().getContent();
		bodyText = IOUtils.toString(is);
		mapper = new Gson();
	}

	public CourseList getCourses() {
		if (bodyText == null){
			return null;
		}
		return mapper.fromJson(bodyText, CourseList.class);
	}
	
	

}
