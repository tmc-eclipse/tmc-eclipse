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
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

class RequestBuilder {
    private UsernamePasswordCredentials credentials = null;

    RequestBuilder setCredentials(String username, String password) {
        this.credentials = new UsernamePasswordCredentials(username, password);
        return this;
    }

    private RequestExecutor createExecutor(String url) {
        return new RequestExecutor(url).setCredentials(credentials);
    }

    private RequestExecutor createExecutor(HttpPost request) {
        return new RequestExecutor(request).setCredentials(credentials);
    }

    public byte[] getForBinary(String url) throws Exception {
        return downloadToBinary(createExecutor(url));
    }

    public String getForText(String url) throws Exception {
        return downloadToText(createExecutor(url));
    }

    public byte[] postForBinary(String url, Map<String, String> params) throws Exception {
        return downloadToBinary(createExecutor(makePostRequest(url, params)));
    }

    public String postForText(String url, Map<String, String> params) throws Exception {
        return downloadToText(createExecutor(makePostRequest(url, params)));
    }

    public String postForText(String url, byte[] data) throws Exception {
        return downloadToText(createExecutor(makeRawPostRequest(url, data)));
    }

    public String postForText(String url, byte[] data, Map<String, String> extraHeaders) throws Exception {
        return downloadToText(createExecutor(makeRawPostRequest(url, data, extraHeaders)));
    }

    public String uploadFileForTextDownload(String url, Map<String, String> params, String fileField, byte[] data)
            throws Exception {
        HttpPost request = makeFileUploadRequest(url, params, fileField, data);
        return downloadToText(createExecutor(request));
    }

    private byte[] downloadToBinary(final RequestExecutor download) throws Exception {
        return EntityUtils.toByteArray(download.execute());
    }

    private String downloadToText(final RequestExecutor download) throws Exception {
        return EntityUtils.toString(download.execute(), "UTF-8");
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

        builder.addBinaryBody(fileField, data, ContentType.APPLICATION_OCTET_STREAM, "file");
        request.setEntity(builder.build());

        return request;
    }
}