package fi.helsinki.cs.tmc.core.services.http;

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
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.SystemDefaultRoutePlanner;
import org.apache.http.util.EntityUtils;

import fi.helsinki.cs.tmc.core.Core;

/**
 * Downloads a single file over HTTP into memory.
 * 
 * If the response was not a successful one (status code 2xx) then a
 * {@link FailedHttpResponseException} with a preloaded buffered entity is
 * thrown.
 */
class RequestExecutor {
    private HttpUriRequest request;
    private static final Logger LOG = Logger.getLogger(RequestExecutor.class.getName());

    private UsernamePasswordCredentials credentials; // May be null

    /* package */RequestExecutor(String url) {
        this(new HttpGet(url));
    }

    /* package */RequestExecutor(HttpUriRequest request) {
        this.request = request;
        if (request.getURI().getUserInfo() != null) {
            credentials = new UsernamePasswordCredentials(request.getURI().getUserInfo());
        }

    }

    public RequestExecutor setCredentials(String username, String password) {
        return setCredentials(new UsernamePasswordCredentials(username, password));
    }

    public RequestExecutor setCredentials(UsernamePasswordCredentials credentials) {
        this.credentials = credentials;
        return this;
    }

    public BufferedHttpEntity execute() throws IOException, InterruptedException, FailedHttpResponseException {
        CloseableHttpClient httpClient = makeHttpClient();

        return executeRequest(httpClient);
    }

    private CloseableHttpClient makeHttpClient() throws IOException {

        HttpClientBuilder httpClientBuilder = HttpClients.custom().useSystemProperties()
                .setConnectionReuseStrategy(new NoConnectionReuseStrategy())
                .setRedirectStrategy(new LaxRedirectStrategy());
        maybeSetProxy(httpClientBuilder);

        return httpClientBuilder.build();
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
        Core.getSettings().setLoggedIn(false);
        if (response.getEntity() == null) {
            throw new IOException("HTTP " + responseCode + " with no response");
        }

        BufferedHttpEntity entity = new BufferedHttpEntity(response.getEntity());
        EntityUtils.consume(entity); // Ensure it's loaded into memory
        if (success(responseCode)) {
            Core.getSettings().setLoggedIn(true);
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

    private void maybeSetProxy(HttpClientBuilder httpClientBuilder) {
        SystemDefaultRoutePlanner routePlanner = new SystemDefaultRoutePlanner(ProxySelector.getDefault());
        if (routePlanner != null) {
            httpClientBuilder.setRoutePlanner(routePlanner);
        }
    }

    /**
     * For testing
     */
    @Override
    public String toString() {
        return LOG.getName() + "\n" + request.getMethod() + "\n" + request.getURI().toString() + "\n"
                + credentials.getUserName() + " " + credentials.getPassword();
    }
}