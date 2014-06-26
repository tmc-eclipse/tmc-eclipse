package fi.helsinki.cs.tmc.core.services.http;

import org.apache.http.impl.client.CloseableHttpClient;

public class MockHttpClientFactory implements HttpClientFactory {
    private CloseableHttpClient returnable;

    public MockHttpClientFactory(CloseableHttpClient returnable) {
        this.returnable = returnable;
    }

    @Override
    public CloseableHttpClient makeHttpClient() {
        return returnable;
    }

}
