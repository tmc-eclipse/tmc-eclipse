package fi.helsinki.cs.plugin.tmc.services.http;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.junit.Before;
import org.junit.Test;

public class RequestExecutorTest {
    private RequestExecutor executor;

    @Before
    public void setUp() {
    }

    @Test
    public void constructorTest() throws URISyntaxException {
        HttpUriRequest request = new HttpPost(new URI("http", "userInfo", "kjhgfdskdfghksadlfg.com", 1337,
                "/dfgdfd/dfgdff.html", "xdxd", "dsfsd"));
        executor = new RequestExecutor(request);
        assertTrue(executor.toString().startsWith("fi.helsinki.cs.plugin.tmc.services.http.RequestExecutor"));
        assertTrue(executor.toString().contains("POST"));
        assertTrue(executor.toString().contains("userInfo"));
    }

    @Test
    public void setCredentialsTest() {
        this.executor = new RequestExecutor(new HttpPost("link.html?asdasd=123"));
        executor.setCredentials(new UsernamePasswordCredentials("trololo", "jeejee"));
        assertTrue(executor.toString().contains("trololo"));
        assertTrue(executor.toString().contains("jeejee"));
        executor.setCredentials("username", "password");
        assertTrue(executor.toString().contains("username password"));
    }

    @Test(expected = IllegalStateException.class)
    public void nonExistentUrlCausesException() throws IOException, InterruptedException, FailedHttpResponseException {
        this.executor = new RequestExecutor("sadasdasd");
        executor.execute();
    }

}