package fi.helsinki.cs.tmc.core.services.http;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.Header;
import org.apache.http.StatusLine;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.params.HttpParams;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import fi.helsinki.cs.tmc.core.services.Settings;

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

        BufferedHttpEntity entity = mock(BufferedHttpEntity.class);
        InputStream stream = mock(InputStream.class);
        when(stream.read()).thenReturn(-1);
        when(stream.read(any(byte[].class))).thenReturn(-1);
        when(stream.read(any(byte[].class), anyInt(), anyInt())).thenReturn(-1);

        when(entity.getContent()).thenReturn(stream);

        when(response.getEntity()).thenReturn(entity);
        when(client.execute(any(HttpUriRequest.class))).thenReturn(response);
        factory = new MockHttpFactory(client);
    }

    @Test
    public void constructorTest() throws URISyntaxException {
        HttpUriRequest request = new HttpPost(new URI("http", "userInfo", "kjhgfdskdfghksadlfg.com", 1337,
                "/dfgdfd/dfgdff.html", "xdxd", "dsfsd"));
        executor = new RequestExecutor(request, factory, mock(Settings.class));
        assertTrue(executor.toString().startsWith("fi.helsinki.cs.tmc.core.services.http.RequestExecutor"));
        assertTrue(executor.toString().contains("POST"));
        assertTrue(executor.toString().contains("userInfo"));
    }

    @Test
    public void setCredentialsTest() {
        this.executor = new RequestExecutor(new HttpPost("link.html?asdasd=123"), factory, mock(Settings.class));
        executor.setCredentials(new UsernamePasswordCredentials("trololo", "jeejee"));
        assertTrue(executor.toString().contains("trololo"));
        assertTrue(executor.toString().contains("jeejee"));
        executor.setCredentials("username", "password");
        assertTrue(executor.toString().contains("username password"));
    }

    @Test
    public void executorSetsCredentialsToRequest() throws FailedHttpResponseException, IOException,
            InterruptedException, URISyntaxException {
        HttpUriRequest request = mock(HttpUriRequest.class);
        URI uri = new URI("http://foo.com");
        when(request.getURI()).thenReturn(uri);

        HttpParams param = mock(HttpParams.class);
        // encoding name, for example "UTF-8"; null seems to work as it picks
        // some default encoding in this case

        when(param.getParameter(anyString())).thenReturn(null);
        when(request.getParams()).thenReturn(param);

        executor = new RequestExecutor(request, factory, mock(Settings.class));

        executor.setCredentials("foo", "bar");

        doAnswer(new Answer() {

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Header header = (Header) invocation.getArguments()[0];
                String usernamePassword = new String(Base64.decodeBase64(header.getValue().split(" ")[1]));
                assertEquals("foo:bar", usernamePassword);
                return null;
            }

        }).when(request).addHeader(any(Header.class));

        executor.execute();

        verify(request, times(1)).addHeader(any(Header.class));
    }
}