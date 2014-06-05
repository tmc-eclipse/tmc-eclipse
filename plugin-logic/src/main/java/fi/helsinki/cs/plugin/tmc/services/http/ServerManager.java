package fi.helsinki.cs.plugin.tmc.services.http;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import fi.helsinki.cs.plugin.tmc.domain.Course;
import fi.helsinki.cs.plugin.tmc.domain.Exercise;
import fi.helsinki.cs.plugin.tmc.domain.FeedbackAnswer;
import fi.helsinki.cs.plugin.tmc.domain.SubmissionResult;
import fi.helsinki.cs.plugin.tmc.domain.ZippedProject;
import fi.helsinki.cs.plugin.tmc.services.Settings;
import fi.helsinki.cs.plugin.tmc.services.http.jsonhelpers.CourseList;
import fi.helsinki.cs.plugin.tmc.services.http.jsonhelpers.ExerciseList;
import fi.helsinki.cs.plugin.tmc.services.http.jsonhelpers.SubmissionResultParser;
import fi.helsinki.cs.plugin.tmc.ui.ObsoleteClientException;

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
        String submitUrl = connectionBuilder.addApiCallQueryParameters(exercise.getReturnUrl());

        Map<String, String> params = new LinkedHashMap<String, String>();
        params.put("client_time", "" + (System.currentTimeMillis() / 1000L));
        params.put("client_nanotime", "" + System.nanoTime());
        params.put("error_msg_locale", Settings.getDefaultSettings().getErrorMsgLocale().toString());

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
        } catch (Exception e) {
            // TODO: Better errorhandling?
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
