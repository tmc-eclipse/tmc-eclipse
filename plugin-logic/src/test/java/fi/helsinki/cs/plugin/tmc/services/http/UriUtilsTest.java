package fi.helsinki.cs.plugin.tmc.services.http;

import static org.junit.Assert.assertEquals;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;

import fi.helsinki.cs.plugin.tmc.services.http.UriUtils;

public class UriUtilsTest {

    @Test
    public void testWithQueryParamString() {
        String uri = UriUtils.withQueryParam("http://tmc.mooc.fi", "test", "val");

        assertEquals(uri.toString(), "http://tmc.mooc.fi?test=val");
    }

    @Test
    public void testWithQueryParamsURI() throws URISyntaxException {
        URI uri = UriUtils.withQueryParam(new URI("http://tmc.mooc.fi"), "key", "value");

        assertEquals(uri.toString(), "http://tmc.mooc.fi?key=value");
    }
}
