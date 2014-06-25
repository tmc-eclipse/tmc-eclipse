package fi.helsinki.cs.tmc.core.services.http;

import org.apache.http.impl.client.CloseableHttpClient;

public interface HttpClientFactory {
    CloseableHttpClient makeHttpClient();
}
