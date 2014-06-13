package fi.helsinki.cs.plugin.tmc.services.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import fi.helsinki.cs.plugin.tmc.Core;
import fi.helsinki.cs.plugin.tmc.domain.Course;
import fi.helsinki.cs.plugin.tmc.domain.Exercise;
import fi.helsinki.cs.plugin.tmc.domain.FeedbackAnswer;
import fi.helsinki.cs.plugin.tmc.domain.SubmissionResult;
import fi.helsinki.cs.plugin.tmc.domain.ZippedProject;
import fi.helsinki.cs.plugin.tmc.services.Settings;
import fi.helsinki.cs.plugin.tmc.services.http.jsonhelpers.CourseList;
import fi.helsinki.cs.plugin.tmc.services.http.jsonhelpers.ExerciseList;
import fi.helsinki.cs.plugin.tmc.services.http.jsonhelpers.SubmissionResultParser;
import fi.helsinki.cs.plugin.tmc.spyware.services.LoggableEvent;
import fi.helsinki.cs.plugin.tmc.spyware.utility.ByteArrayGsonSerializer;
import fi.helsinki.cs.plugin.tmc.spyware.utility.ExceptionUtils;
import fi.helsinki.cs.plugin.tmc.ui.ObsoleteClientException;
import fi.helsinki.cs.plugin.tmc.ui.UserVisibleException;

public class ServerManager {
    private ConnectionBuilder connectionBuilder;
    private Gson mapper;

    public ServerManager(Gson mapper, ConnectionBuilder connectionBuilder) {
        this.connectionBuilder = connectionBuilder;
        this.mapper = mapper;
    }

    public ServerManager(Settings settings) {
        this(new Gson(), new ConnectionBuilder(settings));
    }

    public List<Course> getCourses() {
        String url = connectionBuilder.getUrl(UrlExtension.COURSES.getExtension());
        String bodyText = getString(url);
        CourseList cl = mapper.fromJson(bodyText, CourseList.class);
        if (cl == null) {
            return new ArrayList<Course>();
        }
        return Arrays.asList(cl.getCourses());
    }

    public List<Exercise> getExercises(String courseId) {
        String url = connectionBuilder.getUrl(UrlExtension.EXERCISES.getExtension(courseId));
        String bodyText = getString(url);
        ExerciseList el = mapper.fromJson(bodyText, ExerciseList.class);
        List<Exercise> exercises = el.getExercises();

        // convert date string to Date object. Ugly hack due to some older code
        // getting deprecated
        for (Exercise e : exercises) {
            e.finalizeDeserialization();
        }
        return exercises;
    }

    public ZippedProject getExerciseZip(String zipUrl) {
        ZippedProject zip = new ZippedProject();
        try {
            byte[] bytes = getBytes(zipUrl);
            zip.setBytes(bytes);
        } catch (Exception e) {
            // TODO: Better error handling?
            e.printStackTrace();
        }
        return zip;
    }

    public SubmissionResponse uploadFile(Exercise exercise, byte[] data) {
        return uploadFile(exercise, data, null);
    }

    public SubmissionResponse uploadFile(Exercise exercise, byte[] data, Map<String, String> extraParams) {
        String submitUrl = connectionBuilder.addApiCallQueryParameters(exercise.getReturnUrl());

        Map<String, String> params = new LinkedHashMap<String, String>();
        params.put("client_time", "" + (System.currentTimeMillis() / 1000L));
        params.put("client_nanotime", "" + System.nanoTime());
        params.put("error_msg_locale", Core.getSettings().getErrorMsgLocale().toString());

        if (extraParams != null && !extraParams.isEmpty()) {
            params.putAll(extraParams);
        }

        String response = "";
        try {
            response = connectionBuilder.createConnection().uploadFileForTextDownload(submitUrl, params,
                    "submission[file]", data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        JsonObject respJson = new JsonParser().parse(response).getAsJsonObject();

        if (respJson.get("error") != null) {
            throw new RuntimeException("Server responded with error: " + respJson.get("error"));
        } else if (respJson.get("submission_url") != null) {
            try {
                URI submissionUrl = new URI(respJson.get("submission_url").getAsString());
                URI pasteUrl = new URI(respJson.get("paste_url").getAsString());
                return new SubmissionResponse(submissionUrl, pasteUrl);
            } catch (Exception e) {
                throw new RuntimeException("Server responded with malformed submission url");
            }
        } else {
            throw new RuntimeException("Server returned unknown response");
        }
    }

    public SubmissionResult getSubmissionResult(URI resultURI) {
        String json = getString(resultURI.toString());
        return (new SubmissionResultParser()).parseFromJson(json);
    }

    public String submitFeedback(String answerUrl, List<FeedbackAnswer> answers) {

        final String submitUrl = connectionBuilder.addApiCallQueryParameters(answerUrl);

        Map<String, String> params = new HashMap<String, String>();

        for (int i = 0; i < answers.size(); ++i) {
            String keyPrefix = "answers[" + i + "]";
            FeedbackAnswer answer = answers.get(i);
            params.put(keyPrefix + "[question_id]", "" + answer.getQuestion().getId());
            params.put(keyPrefix + "[answer]", answer.getAnswer());
        }

        try {
            return connectionBuilder.createConnection().postForText(submitUrl, params);
        } catch (FailedHttpResponseException e) {
            return checkForObsoleteClient(e);
        } catch (Exception e) {
            throw new RuntimeException("An error occured while submitting feedback: " + e.getMessage());
        }
    }

    public void sendEventLogs(String url, List<LoggableEvent> events) {

        String fullUrl = connectionBuilder.addApiCallQueryParameters(url);

        Map<String, String> extraHeaders = new LinkedHashMap<String, String>();
        extraHeaders.put("X-Tmc-Version", "1");
        extraHeaders.put("X-Tmc-Username", Core.getSettings().getUsername());
        extraHeaders.put("X-Tmc-Password", Core.getSettings().getPassword());

        byte[] data;
        try {
            data = eventListToPostBody(events);
        } catch (IOException e) {
            throw ExceptionUtils.toRuntimeException(e);
        }

        try {
            connectionBuilder.createConnection().rawPostForText(fullUrl, data, extraHeaders);
        } catch (Exception e) {
            throw new RuntimeException("An error occured while submitting snapshot: " + e.getMessage());
        }
    }

    private byte[] eventListToPostBody(List<LoggableEvent> events) throws IOException {
        ByteArrayOutputStream bufferBos = new ByteArrayOutputStream();
        GZIPOutputStream gzos = new GZIPOutputStream(bufferBos);
        OutputStreamWriter bufferWriter = new OutputStreamWriter(gzos, Charset.forName("UTF-8"));

        Gson gson = new GsonBuilder().registerTypeAdapter(byte[].class, new ByteArrayGsonSerializer()).create();

        gson.toJson(events, new TypeToken<List<LoggableEvent>>() {
        }.getType(), bufferWriter);
        bufferWriter.close();
        gzos.close();

        return bufferBos.toByteArray();
    }

    private byte[] getBytes(String url) {
        byte[] bytes;
        try {
            bytes = connectionBuilder.createConnection().getForBinary(url);
        } catch (Exception e) {
            // TODO: Better error handling?
            bytes = new byte[0];
        }
        return bytes;
    }

    private String getString(String url) {
        String json;
        try {
            json = connectionBuilder.createConnection().getForText(url);
        } catch (FailedHttpResponseException fhre) {
            if (fhre.getStatusCode() == 403) {
                throw new UserVisibleException("Authentication failed - check your username and password.");
            }
            if (fhre.getStatusCode() == 404) {
                throw new UserVisibleException("Could not connect to server - check your TMC server address.");
            }
            if (fhre.getStatusCode() == 500) {
                throw new UserVisibleException(
                        "An error occurred while trying to refresh courses. Please try again later.");
            }
            json = "";
        } catch (IllegalStateException ise) {
            throw new UserVisibleException("Could not connect to server - check your TMC server address.");
        } catch (Exception e) {
            e.printStackTrace();
            json = "";
        }

        return json;
    }

    private <T> T checkForObsoleteClient(FailedHttpResponseException ex) throws ObsoleteClientException,
            FailedHttpResponseException {
        if (ex.getStatusCode() == 404) {
            boolean obsolete;
            try {
                obsolete = new JsonParser().parse(ex.getEntityAsString()).getAsJsonObject().get("obsolete_client")
                        .getAsBoolean();
            } catch (Exception ex2) {
                obsolete = false;
            }
            if (obsolete) {
                throw new ObsoleteClientException();
            }
        }
        throw ex;
    }

}
