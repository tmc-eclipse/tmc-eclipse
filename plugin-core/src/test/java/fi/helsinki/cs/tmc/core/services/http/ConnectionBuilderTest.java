package fi.helsinki.cs.tmc.core.services.http;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import fi.helsinki.cs.tmc.core.services.Settings;

public class ConnectionBuilderTest {
    private ConnectionBuilder cb;

    @Before
    public void setup() {
        cb = new ConnectionBuilder(Settings.getDefaultSettings());
    }

    @Test
    public void appendedUrlContainsApiVersion() {
        String url = cb.getUrl("test");
        assertTrue(url.contains("api_version=" + ConnectionBuilder.API_VERSION));
    }

    @Test
    public void appendedUrlContainsClient() {
        String url = cb.getUrl("test");
        assertTrue(url.contains("client=eclipse_plugin"));
    }

    @Test
    public void appendedUrlContainsClientVersion() {
        String url = cb.getUrl("test");
        assertTrue(url.contains("client_version=0.0.1"));
    }

    @Test
    public void appendedUrlContainsCorrectRoot() {
        String url = cb.getUrl("test");
        assertTrue(url.contains(Settings.getDefaultSettings().getServerBaseUrl() + "/test"));
    }
}
