package fi.helsinki.cs.tmc.core.services.http;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.StatusLine;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.Before;
import org.junit.Test;

public class RequestExecutorTest {
    private RequestExecutor executor;
    private MockHttpFactory factory;
    private CloseableHttpResponse response;

    @Before
    public void setUp() throws ClientProtocolException, IOException {
        CloseableHttpClient client = mock(CloseableHttpClient.class);

        response = mock(CloseableHttpResponse.class);
        StatusLine line = mock(StatusLine.class);
        when(line.getStatusCode()).thenReturn(200);
        when(response.getStatusLine()).thenReturn(line);
        when(client.execute(any(HttpUriRequest.class))).thenReturn(response);
        factory = new MockHttpFactory(client);
    }

    @Test
    public void constructorTest() throws URISyntaxException {
        HttpUriRequest request = new HttpPost(new URI("http", "userInfo", "kjhgfdskdfghksadlfg.com", 1337,
                "/dfgdfd/dfgdff.html", "xdxd", "dsfsd"));
        executor = new RequestExecutor(request, factory);
        assertTrue(executor.toString().startsWith("fi.helsinki.cs.tmc.core.services.http.RequestExecutor"));
        assertTrue(executor.toString().contains("POST"));
        assertTrue(executor.toString().contains("userInfo"));
    }

    @Test
    public void setCredentialsTest() {
        this.executor = new RequestExecutor(new HttpPost("link.html?asdasd=123"), factory);
        executor.setCredentials(new UsernamePasswordCredentials("trololo", "jeejee"));
        assertTrue(executor.toString().contains("trololo"));
        assertTrue(executor.toString().contains("jeejee"));
        executor.setCredentials("username", "password");
        assertTrue(executor.toString().contains("username password"));
    }

    @Test(expected = IllegalStateException.class)
    public void nonExistentUrlCausesException() throws IOException, InterruptedException, FailedHttpResponseException {
        this.executor = new RequestExecutor("sadasdasd", factory);
        executor.execute();
    }

    // @Test
    // public void executorSetsCredentialsToRequest() {
    // HttpUriRequest request = mock(HttpUriRequest.class);
    // executor = new RequestExecutor(request, factory);
    //
    // executor.setCredentials("foo", "bar");
    // executor.execute()
    // }
}