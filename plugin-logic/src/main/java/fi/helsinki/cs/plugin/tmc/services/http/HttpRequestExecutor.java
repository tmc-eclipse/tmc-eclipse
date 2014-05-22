package fi.helsinki.cs.plugin.tmc.services.http;

import java.io.IOException;
import java.net.ProxySelector;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.NoConnectionReuseStrategy;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.SystemDefaultRoutePlanner;
import org.apache.http.util.EntityUtils;

/**
 * Downloads a single file over HTTP into memory while being cancellable.
 * 
 * If the response was not a successful one (status code 2xx) then a
 * {@link FailedHttpResponseException} with a preloaded buffered entity is
 * thrown.
 */
public class HttpRequestExecutor implements CancellableCallable<BufferedHttpEntity> {
    private final Object shutdownLock = new Object();

    private HttpUriRequest request;
    private static final Logger LOG = Logger.getLogger(HttpRequestExecutor.class.getName());

    private UsernamePasswordCredentials credentials; // May be null

    /* package */HttpRequestExecutor(String url) {
        this(new HttpGet(url));
    }

    /* package */HttpRequestExecutor(HttpUriRequest request) {
        this.request = request;
        if (request.getURI().getUserInfo() != null) {
            credentials = new UsernamePasswordCredentials(request.getURI().getUserInfo());
        }

    }

    public HttpRequestExecutor setCredentials(String username, String password) {
        return setCredentials(new UsernamePasswordCredentials(username, password));
    }

    public HttpRequestExecutor setCredentials(UsernamePasswordCredentials credentials) {
        this.credentials = credentials;
        return this;
    }

    public HttpRequestExecutor setTimeout(int timeoutMs) {
        return this;
    }

    @Override
    public BufferedHttpEntity call() throws IOException, InterruptedException, FailedHttpResponseException {
        CloseableHttpClient httpClient = makeHttpClient();

        try {
            return executeRequest(httpClient);
        } finally {
            synchronized (shutdownLock) {
                request = null;
                disposeOfHttpClient(httpClient);
            }
        }
    }

    private CloseableHttpClient makeHttpClient() throws IOException {

        HttpClientBuilder httpClientBuilder = HttpClients.custom().useSystemProperties()
                .setConnectionReuseStrategy(new NoConnectionReuseStrategy());
        maybeSetProxy(httpClientBuilder);

        return httpClientBuilder.build();
    }

    private SystemDefaultRoutePlanner getProxy() {
        return new SystemDefaultRoutePlanner(ProxySelector.getDefault());
    }

    private void disposeOfHttpClient(CloseableHttpClient httpClient) {
        try {
            httpClient.close();
        } catch (IOException ex) {
            LOG.log(Level.WARNING, "Dispose of httpClient failed {0}", ex);
        }
    }

    private BufferedHttpEntity executeRequest(HttpClient httpClient) throws IOException, InterruptedException,
            FailedHttpResponseException {

        HttpResponse response = null;

        try {
            if (this.credentials != null) {
                request.addHeader(new BasicScheme(Charset.forName("UTF-8")).authenticate(this.credentials, request,
                        null));
            }
            response = httpClient.execute(request);
        } catch (IOException ex) {
            LOG.log(Level.INFO, "Executing http request failed: {0}", ex.toString());
            if (request.isAborted()) {
                throw new InterruptedException();
            } else {
                throw new IOException("Download failed: " + ex.getMessage(), ex);
            }
        } catch (AuthenticationException ex) {
            LOG.log(Level.INFO, "Auth failed {0}", ex);
            throw new InterruptedException();
        }

        return handleResponse(response);
    }

    private BufferedHttpEntity handleResponse(HttpResponse response) throws IOException, InterruptedException,
            FailedHttpResponseException {
        int responseCode = response.getStatusLine().getStatusCode();
        if (response.getEntity() == null) {
            throw new IOException("HTTP " + responseCode + " with no response");
        }

        BufferedHttpEntity entity = new BufferedHttpEntity(response.getEntity());
        EntityUtils.consume(entity); // Ensure it's loaded into memory
        if (success(responseCode)) {
            return entity;
        } else {
            throw new FailedHttpResponseException(responseCode, entity);
        }
    }

    /*
     * Returns true for statuscodes in the 2xx (success) range. SC_OK = 200,
     * SC_MULTIPLE_CHOICES = 300.
     */
    private boolean success(int responseCode) {
        return HttpStatus.SC_OK <= responseCode && responseCode < HttpStatus.SC_MULTIPLE_CHOICES;
    }

    /**
     * May be called from another thread to cancel an ongoing download.
     */
    @Override
    public boolean cancel() {
        synchronized (shutdownLock) {
            if (request != null) {
                request.abort();
            }
        }
        return true;
    }

    private void maybeSetProxy(HttpClientBuilder httpClientBuilder) {
        SystemDefaultRoutePlanner systemDefaultRoutePlanner = getProxy();
        if (systemDefaultRoutePlanner != null) {
            httpClientBuilder.setRoutePlanner(systemDefaultRoutePlanner);
        }
    }
}