package fi.helsinki.cs.tmc.core.services.http;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class HttpClientFactoryImplTest {

    private HttpClientFactory factory;

    @Before
    public void setUp() {
        factory = new HttpClientFactoryImpl();
    }

    @Test
    public void factoryReturnsObject() {
        assertNotNull(factory.makeHttpClient());
    }
}
