package fi.helsinki.cs.tmc.core.services.http;

import java.net.ProxySelector;

import org.apache.http.impl.NoConnectionReuseStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.SystemDefaultRoutePlanner;

public class HttpClientFactoryImpl implements HttpClientFactory {

    @Override
    public CloseableHttpClient makeHttpClient() {
        HttpClientBuilder httpClientBuilder = HttpClients.custom().useSystemProperties()
                .setConnectionReuseStrategy(new NoConnectionReuseStrategy())
                .setRedirectStrategy(new LaxRedirectStrategy());
        maybeSetProxy(httpClientBuilder);

        return httpClientBuilder.build();

    }

    private void maybeSetProxy(HttpClientBuilder httpClientBuilder) {
        SystemDefaultRoutePlanner routePlanner = new SystemDefaultRoutePlanner(ProxySelector.getDefault());
        if (routePlanner != null) {
            httpClientBuilder.setRoutePlanner(routePlanner);
        }
    }
}
