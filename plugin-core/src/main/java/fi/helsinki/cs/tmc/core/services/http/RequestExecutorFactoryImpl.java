package fi.helsinki.cs.tmc.core.services.http;

import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpPost;

import fi.helsinki.cs.tmc.core.services.Settings;

/**
 * Implementation of the RequestExecutorFactory interface. Produces actual
 * RequestExecutor-objects
 * 
 */
public class RequestExecutorFactoryImpl implements RequestExecutorFactory {

    private Settings settings;

    public RequestExecutorFactoryImpl(Settings settings) {
        this.settings = settings;
    }

    @Override
    public RequestExecutor createExecutor(String url, UsernamePasswordCredentials credentials) {
        return new RequestExecutor(url, new HttpClientFactoryImpl(), settings).setCredentials(credentials);
    }

    @Override
    public RequestExecutor createExecutor(HttpPost request, UsernamePasswordCredentials credentials) {
        return new RequestExecutor(request, new HttpClientFactoryImpl(), settings).setCredentials(credentials);
    }

}
