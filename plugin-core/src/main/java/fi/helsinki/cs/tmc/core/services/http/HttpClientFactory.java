package fi.helsinki.cs.tmc.core.services.http;

import org.apache.http.impl.client.CloseableHttpClient;

/**
 * Factory interface for HTTP client creation. Primarly exists to allow unit
 * testing any class that would otherwise really connect to internet
 * 
 */
public interface HttpClientFactory {
    CloseableHttpClient makeHttpClient();
}
