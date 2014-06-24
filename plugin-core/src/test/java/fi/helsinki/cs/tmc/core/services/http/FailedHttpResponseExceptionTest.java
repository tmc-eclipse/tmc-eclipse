package fi.helsinki.cs.tmc.core.services.http;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.entity.BasicHttpEntity;
import org.junit.Before;
import org.junit.Test;

public class FailedHttpResponseExceptionTest {
    private HttpEntity entity;
    private FailedHttpResponseException exception;

    @Before
    public void setup() {
        entity = mock(BasicHttpEntity.class);
        exception = new FailedHttpResponseException(200, entity);
    }

    @Test
    public void hasCorrectStatusCode() {
        exception = new FailedHttpResponseException(200, entity);
        assertEquals(200, exception.getStatusCode());

        exception = new FailedHttpResponseException(404, entity);
        assertEquals(404, exception.getStatusCode());
    }

    @Test
    public void hasCorrectEntity() {
        assertEquals(entity, exception.getEntity());
    }

    @Test
    public void entityAsStringReturnsCorrectly() throws IllegalStateException, IOException {
        InputStream content = new ByteArrayInputStream("testContent".getBytes("UTF-8"));
        when(entity.getContent()).thenReturn(content);

        assertEquals("testContent", exception.getEntityAsString());
    }

    @Test(expected = RuntimeException.class)
    public void entityAsStringThrowsRuntimeExceptionOnIOException() throws IllegalStateException, IOException {
        when(entity.getContent()).thenThrow(new IOException());
        exception.getEntityAsString();
    }

}
