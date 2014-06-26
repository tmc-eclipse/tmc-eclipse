package fi.helsinki.cs.tmc.core.services.http;

import fi.helsinki.cs.tmc.core.services.Settings;

/**
 * Builder class for http connections
 * 
 * 
 */
class ConnectionBuilder {

    public static final int API_VERSION = 7;

    private Settings settings;

    public ConnectionBuilder(Settings settings) {
        this.settings = settings;
    }

    /**
     * Gets URL to server with necessary APi call parameters
     * 
     * @param extension
     *            Extension to be added to the base URL
     * @return Complete URL
     */
    public String getUrl(String extension) {
        return addApiCallQueryParameters(settings.getServerBaseUrl() + "/" + extension);
    }

    /**
     * Adds necessary URL parameters to request so that server accepts the
     * request
     * 
     * @param url
     *            URL in question
     * @return URL with parameters
     */
    public String addApiCallQueryParameters(String url) {
        url = UriUtils.withQueryParam(url, "api_version", "" + API_VERSION);
        url = UriUtils.withQueryParam(url, "client", "eclipse_plugin");
        url = UriUtils.withQueryParam(url, "client_version", getClientVersion());
        return url;
    }

    private static String getClientVersion() {
        return "0.0.1";
    }

    /**
     * Creates and returns RequestBuilder
     * 
     * @return RequestBuilder that will build any requests
     */
    public RequestBuilder createConnection() {
        return new RequestBuilder(new RequestExecutorFactoryImpl(settings)).setCredentials(settings.getUsername(),
                settings.getPassword());
    }
}
