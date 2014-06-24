package fi.helsinki.cs.tmc.core.services.http;

import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpPost;

/**
 * Implementation of the RequestExecutorFactory interface. Produces actual
 * RE-objects
 * 
 */
public class RequestExecutorFactoryImpl implements RequestExecutorFactory {

    @Override
    public RequestExecutor createExecutor(String url, UsernamePasswordCredentials credentials) {
        return new RequestExecutor(url).setCredentials(credentials);
    }

    @Override
    public RequestExecutor createExecutor(HttpPost request, UsernamePasswordCredentials credentials) {
        return new RequestExecutor(request).setCredentials(credentials);
    }

}
