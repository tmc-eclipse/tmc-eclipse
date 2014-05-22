package fi.helsinki.cs.plugin.tmc.services.http;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpTasks {
    private UsernamePasswordCredentials credentials = null;

    public HttpTasks setCredentials(String username, String password) {
        this.credentials = new UsernamePasswordCredentials(username, password);
        return this;
    }

    public HttpRequestExecutor createExecutor(String url) {
        return new HttpRequestExecutor(url).setCredentials(credentials);
    }

    private HttpRequestExecutor createExecutor(HttpPost request) {
        return new HttpRequestExecutor(request).setCredentials(credentials);
    }

    public CancellableCallable<byte[]> getForBinary(String url) {
        return downloadToBinary(createExecutor(url));
    }

    public CancellableCallable<String> getForText(String url) {
        return downloadToText(createExecutor(url));
    }

    public CancellableCallable<byte[]> postForBinary(String url, Map<String, String> params) throws URISyntaxException {
        return downloadToBinary(createExecutor(makePostRequest(url, params)));
    }

    public CancellableCallable<String> postForText(String url, Map<String, String> params) throws URISyntaxException {
        return downloadToText(createExecutor(makePostRequest(url, params)));
    }

    public CancellableCallable<String> rawPostForText(String url, byte[] data) throws URISyntaxException {
        return downloadToText(createExecutor(makeRawPostRequest(url, data)));
    }

    public CancellableCallable<String> rawPostForText(String url, byte[] data, Map<String, String> extraHeaders)
            throws URISyntaxException {
        return downloadToText(createExecutor(makeRawPostRequest(url, data, extraHeaders)));
    }

    public CancellableCallable<String> uploadFileForTextDownload(String url, Map<String, String> params,
            String fileField, byte[] data) throws URISyntaxException {
        HttpPost request = makeFileUploadRequest(url, params, fileField, data);
        return downloadToText(createExecutor(request));
    }

    private CancellableCallable<byte[]> downloadToBinary(final HttpRequestExecutor download) {
        return new CancellableCallable<byte[]>() {
            @Override
            public byte[] call() throws Exception {
                return EntityUtils.toByteArray(download.call());
            }

            @Override
            public boolean cancel() {
                return download.cancel();
            }
        };
    }

    private CancellableCallable<String> downloadToText(final HttpRequestExecutor download) {
        return new CancellableCallable<String>() {
            @Override
            public String call() throws Exception {
                return EntityUtils.toString(download.call(), "UTF-8");
            }

            @Override
            public boolean cancel() {
                return download.cancel();
            }
        };
    }

    private HttpPost makePostRequest(String url, Map<String, String> params) throws URISyntaxException {
        HttpPost request = new HttpPost(url);

        ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>(params.size());
        for (Map.Entry<String, String> param : params.entrySet()) {
            pairs.add(new BasicNameValuePair(param.getKey(), param.getValue()));
        }

        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(pairs, "UTF-8");
            request.setEntity(entity);
            return request;
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }

    private HttpPost makeRawPostRequest(String url, byte[] data) throws URISyntaxException {
        Map<String, String> empty = Collections.emptyMap();
        return makeRawPostRequest(url, data, empty);
    }

    private HttpPost makeRawPostRequest(String url, byte[] data, Map<String, String> extraHeaders)
            throws URISyntaxException {
        HttpPost request = new HttpPost(url);
        for (Map.Entry<String, String> header : extraHeaders.entrySet()) {
            request.addHeader(header.getKey(), header.getValue());
        }

        ByteArrayEntity entity = new ByteArrayEntity(data);
        request.setEntity(entity);
        return request;
    }

    private HttpPost makeFileUploadRequest(String url, Map<String, String> params, String fileField, byte[] data)
            throws URISyntaxException {
        HttpPost request = new HttpPost(url);

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.setCharset(Charset.forName("UTF-8"));

        for (Map.Entry<String, String> e : params.entrySet()) {
            builder.addTextBody(e.getKey(), e.getValue());
        }

        builder.addBinaryBody(fileField, data);
        request.setEntity(builder.build());
        return request;
    }
}