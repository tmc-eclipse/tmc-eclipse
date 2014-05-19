package fi.helsinki.cs.plugin.tmc.services.http;

public class ServerAccess {
	public static final int API_VERSION = 7;
	
	private static String getClientVersion() {
		return "0.0.1";
	}

	public String getUrl(String extension) {
		return addApiCallQueryParameters("http://tmc.mooc.fi/test/" + extension);
	}

	private String addApiCallQueryParameters(String url) {
		url = UriUtils.withQueryParam(url, "api_version", "" + API_VERSION);
		url = UriUtils.withQueryParam(url, "client", "eclipse_plugin");
		url = UriUtils.withQueryParam(url, "client_version", getClientVersion());
		return url;
	}

	public HttpTasks createHttpTasks() {
		return new HttpTasks().setCredentials("tmc-eclipse", "testUser1"); // kovakoodattu!
	}
	

	public boolean hasEnoughSettings() { //kovakoodattu!
		return true; 
	}
	
	public boolean needsOnlyPassword() { //kovakoodattu!
        return false;
    }
	
	



}
