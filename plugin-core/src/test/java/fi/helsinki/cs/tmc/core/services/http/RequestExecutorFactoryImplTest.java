package fi.helsinki.cs.tmc.core.services.http;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpPost;
import org.junit.Before;
import org.junit.Test;

import fi.helsinki.cs.tmc.core.services.Settings;

public class RequestExecutorFactoryImplTest {
    private RequestExecutorFactory factory;

    @Before
    public void setUp() {

        factory = new RequestExecutorFactoryImpl(mock(Settings.class));
    }

    @Test
    public void createExecutorWithUrlReturnsRequestExecutor() {
        assertEquals(RequestExecutor.class,
                factory.createExecutor("http://www.google.com", mock(UsernamePasswordCredentials.class)).getClass());
    }

    @Test
    public void createExecutorWithRequestReturnsRequestExecutor() throws URISyntaxException {
        HttpPost post = mock(HttpPost.class);
        URI uri = new URI("http://www.google.com");
        when(post.getURI()).thenReturn(uri);
        assertEquals(RequestExecutor.class, factory.createExecutor(post, mock(UsernamePasswordCredentials.class))
                .getClass());
    }
}
