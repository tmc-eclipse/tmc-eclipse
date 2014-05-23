package fi.helsinki.cs.plugin.tmc.services.http;

import fi.helsinki.cs.plugin.tmc.services.Settings;

/*package*/class ConnectionBuilder {
    public static final int API_VERSION = 7;
    private Settings settings = Settings.getDefaultSettings();

    public String getUrl(String extension) {
        return addApiCallQueryParameters(settings.getServerBaseUrl() + "/" + extension);
    }

    private String addApiCallQueryParameters(String url) {
        url = UriUtils.withQueryParam(url, "api_version", "" + API_VERSION);
        url = UriUtils.withQueryParam(url, "client", "eclipse_plugin");
        url = UriUtils.withQueryParam(url, "client_version", getClientVersion());
        return url;
    }

    private static String getClientVersion() {
        return "0.0.1";
    }

    public RequestBuilder createConnection() {
        return new RequestBuilder().setCredentials(settings.getUsername(), settings.getPassword());
    }

    public boolean hasEnoughSettings() { // kovakoodattu!
        return true;
    }

    public boolean needsOnlyPassword() { // kovakoodattu!
        return false;
    }

}
