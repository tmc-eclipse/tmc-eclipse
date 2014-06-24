package fi.helsinki.cs.tmc.core.services.http;

import org.apache.http.auth.UsernamePasswordCredentials;

/**
 * 
 * Interface for request execution creation. Makes unit testing possible when we
 * can mock away the actual http connectivity
 * 
 */
public interface RequestExecutorFactory {
    RequestExecutor createExecutor(String url, UsernamePasswordCredentials credentials);
}
