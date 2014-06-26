package fi.helsinki.cs.tmc.core.services.http;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
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
    private MockHttpClientFactory factory;
    private CloseableHttpResponse response;
    private CloseableHttpClient client;
    private StatusLine line;
    private BufferedHttpEntity entity;

    @Before
    public void setUp() throws ClientProtocolException, IOException {
        client = mock(CloseableHttpClient.class);

        response = mock(CloseableHttpResponse.class);
        line = mock(StatusLine.class);
        when(line.getStatusCode()).thenReturn(200);
        when(response.getStatusLine()).thenReturn(line);

        entity = mock(BufferedHttpEntity.class);
        InputStream stream = mock(InputStream.class);
        when(stream.read()).thenReturn(-1);
        when(stream.read(any(byte[].class))).thenReturn(-1);
        when(stream.read(any(byte[].class), anyInt(), anyInt())).thenReturn(-1);

        when(entity.getContent()).thenReturn(stream);

        when(response.getEntity()).thenReturn(entity);
        when(client.execute(any(HttpUriRequest.class))).thenReturn(response);
        factory = new MockHttpClientFactory(client);
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
        HttpUriRequest request = mockRequest();

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

    @Test
    public void executorDoesNotSetCredentialsToRequestIfExecutorHasNoCredentials() throws FailedHttpResponseException,
            IOException, InterruptedException {
        HttpUriRequest request = mockRequest();

        executor = new RequestExecutor(request, factory, mock(Settings.class));

        executor.execute();

        verify(request, times(0)).addHeader(any(Header.class));
    }

    @Test(expected = IOException.class)
    public void executeRequestThrowsIoExceptionIfRequestNotAbortedAndHttpClientThrowsIOException()
            throws ClientProtocolException, IOException, FailedHttpResponseException, InterruptedException {
        HttpUriRequest request = mockRequest();
        when(request.isAborted()).thenReturn(false);
        when(client.execute(any(HttpUriRequest.class))).thenThrow(new IOException("Foo"));
        executor = new RequestExecutor(request, factory, mock(Settings.class));
        executor.execute();
    }

    @Test(expected = InterruptedException.class)
    public void executeThrowsInterruptedExceptionsIfRequestIsAbortedAndHttpClientThrowsIOException()
            throws ClientProtocolException, IOException, FailedHttpResponseException, InterruptedException {
        HttpUriRequest request = mockRequest();
        when(request.isAborted()).thenReturn(true);
        when(client.execute(any(HttpUriRequest.class))).thenThrow(new IOException("Foo"));
        executor = new RequestExecutor(request, factory, mock(Settings.class));
        executor.execute();
    }

    @Test(expected = IOException.class)
    public void executeThrowsIOExceptionIfResponseEntityIsNull() throws ClientProtocolException, IOException,
            FailedHttpResponseException, InterruptedException {
        HttpUriRequest request = mockRequest();

        when(response.getEntity()).thenReturn(null);
        executor = new RequestExecutor(request, factory, mock(Settings.class));
        executor.execute();
    }

    @Test
    public void userIsLoggedInIfStatusIsOK() throws ClientProtocolException, IOException, FailedHttpResponseException,
            InterruptedException {
        HttpUriRequest request = mockRequest();

        Settings settings = mock(Settings.class);
        executor = new RequestExecutor(request, factory, settings);
        executor.execute();
        verify(settings, times(1)).setLoggedIn(true);
    }

    @Test
    public void entityIsReturnedIfStatusIsOK() throws ClientProtocolException, IOException,
            FailedHttpResponseException, InterruptedException {
        HttpUriRequest request = mockRequest();

        Settings settings = mock(Settings.class);
        executor = new RequestExecutor(request, factory, settings);
        assertNotNull(executor.execute());
    }

    @Test
    public void userIsNotLoggedInIfStatusIs403() throws ClientProtocolException, IOException,
            FailedHttpResponseException, InterruptedException {
        HttpUriRequest request = mockRequest();
        when(line.getStatusCode()).thenReturn(403);
        Settings settings = mock(Settings.class);
        executor = new RequestExecutor(request, factory, settings);
        try {
            executor.execute();
        } catch (FailedHttpResponseException ex) {

        }
        verify(settings, times(1)).setLoggedIn(false);
    }

    @Test(expected = FailedHttpResponseException.class)
    public void executeThrowsFailedHttpResponseExceptionOn() throws ClientProtocolException, IOException,
            FailedHttpResponseException, InterruptedException {
        HttpUriRequest request = mockRequest();
        when(line.getStatusCode()).thenReturn(403);
        Settings settings = mock(Settings.class);
        executor = new RequestExecutor(request, factory, settings);
        executor.execute();
    }

    private HttpUriRequest mockRequest() {
        HttpUriRequest request = mock(HttpUriRequest.class);
        URI uri = null;
        try {
            uri = new URI("http://foo.com");
        } catch (URISyntaxException e) {
            fail("Uri syntax broken");
        }
        when(request.getURI()).thenReturn(uri);

        HttpParams param = mock(HttpParams.class);
        // encoding name, for example "UTF-8"; null seems to work as it picks
        // some default encoding in this case

        when(param.getParameter(anyString())).thenReturn(null);
        when(request.getParams()).thenReturn(param);
        return request;
    }

}