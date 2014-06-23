package fi.helsinki.cs.plugin.tmc.services.http;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.google.gson.Gson;

import fi.helsinki.cs.plugin.tmc.domain.Course;
import fi.helsinki.cs.plugin.tmc.domain.Exercise;
import fi.helsinki.cs.plugin.tmc.domain.FeedbackAnswer;
import fi.helsinki.cs.plugin.tmc.domain.FeedbackQuestion;
import fi.helsinki.cs.plugin.tmc.domain.ZippedProject;
import fi.helsinki.cs.plugin.tmc.services.Settings;
import fi.helsinki.cs.plugin.tmc.services.http.jsonhelpers.CourseList;
import fi.helsinki.cs.plugin.tmc.services.http.jsonhelpers.ExerciseList;
import fi.helsinki.cs.plugin.tmc.spyware.services.LoggableEvent;
import fi.helsinki.cs.plugin.tmc.ui.ObsoleteClientException;

public class ServerManagerTest {
    private ConnectionBuilder connectionBuilder;
    private Gson gson;
    private ServerManager server;
    private Settings settings;

    @Before
    public void setup() {
        connectionBuilder = mock(ConnectionBuilder.class);
        gson = new Gson();
        settings = mock(Settings.class);
        server = new ServerManager(gson, connectionBuilder, settings);
    }

    @Test
    public void getCoursesReturnsValidCoursesOnSuccefullHttpRequest() throws Exception {
        CourseList cl = buildMockCourseList();
        String mockJson = gson.toJson(cl);

        RequestBuilder rb = mock(RequestBuilder.class);
        when(connectionBuilder.createConnection()).thenReturn(rb);
        when(rb.getForText(any(String.class))).thenReturn(mockJson);

        List<Course> returnedCourses = server.getCourses();

        for (int i = 1; i < cl.getCourses().length; i++) {
            Course c = cl.getCourses()[i];
            boolean found = false;
            for (Course returned : returnedCourses) {
                if (returned.getName().equals(c.getName())) {
                    found = true;
                }
            }
            assertTrue("Didn't find all courses that should have been present, based on the JSON", found);
        }

        assertEquals(cl.getCourses().length, returnedCourses.size());
        assertEquals("7", cl.getApiVersion());
    }

    @Test
    public void getcoursesReturnEmptyListOnFailure() throws Exception {
        RequestBuilder rb = mock(RequestBuilder.class);
        when(connectionBuilder.createConnection()).thenReturn(rb);
        when(rb.getForText(any(String.class))).thenReturn("");

        assertEquals(0, server.getCourses().size());
    }

    @Test
    public void getExercisesReturnsValidObjectOnSuccesfullHttpRequest() throws Exception {
        ExerciseList el = buildMockExerciseList();
        String mockJson = gson.toJson(el);

        RequestBuilder rb = mock(RequestBuilder.class);
        when(connectionBuilder.createConnection()).thenReturn(rb);
        when(rb.getForText(any(String.class))).thenReturn(mockJson);

        List<Exercise> returned = server.getExercises("11");

        for (Exercise orig : el.getExercises()) {
            boolean found = false;
            for (Exercise ret : returned) {
                if (ret.getName().equals(orig.getName())) {
                    found = true;
                }
            }
            assertTrue("The returned list of exercises should contain all exercises of the original list", found);
        }
    }

    @Test
    public void getExerciseZipShouldReturnTheByteArrayGottenFromTheHttpRequest() throws Exception {
        byte[] byteArray = {1, 0, 1};
        RequestBuilder rb = mock(RequestBuilder.class);
        when(connectionBuilder.createConnection()).thenReturn(rb);
        when(rb.getForBinary(any(String.class))).thenReturn(byteArray);

        ZippedProject returned = server.getExerciseZip("url");
        assertEquals(byteArray, returned.getBytes());
    }

    @Test
    public void getExerciseZipShouldReturnEmptyByteArrayOnFailedRequest() throws Exception {
        RequestBuilder rb = mock(RequestBuilder.class);
        when(connectionBuilder.createConnection()).thenReturn(rb);
        when(rb.getForBinary(any(String.class))).thenThrow(new IOException());

        assertEquals(0, server.getExerciseZip("url").getBytes().length);
    }

    @Test
    public void submitFeedbackCreatesConnectionAndCallsPostForText() throws Exception {
        RequestBuilder rb = mock(RequestBuilder.class);
        when(connectionBuilder.createConnection()).thenReturn(rb);
        String url = "mock_url";
        String apiUrl = "wee";
        when(connectionBuilder.addApiCallQueryParameters(Mockito.anyString())).thenReturn(apiUrl);

        server.submitFeedback(url, new ArrayList<FeedbackAnswer>());
        verify(connectionBuilder, times(1)).createConnection();
        verify(connectionBuilder, times(1)).addApiCallQueryParameters(url);
        verify(rb, times(1)).postForText(eq(apiUrl), Mockito.anyMap());
    }

    @Test
    public void submitFeedbackPostForTextMapHasCorrectValues() throws Exception {
        List<FeedbackAnswer> answers = new ArrayList<FeedbackAnswer>();
        answers.add(new FeedbackAnswer(new FeedbackQuestion(0, "Question", "text"), "Answer"));

        RequestBuilder rb = mock(RequestBuilder.class);
        when(connectionBuilder.createConnection()).thenReturn(rb);

        String url = "mock_url";
        String apiUrl = "wee";
        when(connectionBuilder.addApiCallQueryParameters(Mockito.anyString())).thenReturn(apiUrl);

        when(rb.postForText(Mockito.anyString(), Mockito.anyMap())).thenAnswer(new Answer() {
            @Override
            public String answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();

                Map<String, String> paramMap = (HashMap<String, String>) args[1];

                assertEquals(2, paramMap.keySet().size());
                assertEquals(2, paramMap.values().size());

                assertTrue(paramMap.keySet().contains("answers[0][question_id]"));
                assertTrue(paramMap.keySet().contains("answers[0][answer]"));

                assertEquals("0", paramMap.get("answers[0][question_id]"));
                assertEquals("Answer", paramMap.get("answers[0][answer]"));

                return "";
            }
        });

        server.submitFeedback(url, answers);
    }

    @Test(expected = RuntimeException.class)
    public void submitFeedbackThrowsRuntimeExceptionOnNonHttpException() throws Exception {
        RequestBuilder rb = mock(RequestBuilder.class);
        when(connectionBuilder.createConnection()).thenReturn(rb);

        when(rb.postForText(Mockito.anyString(), Mockito.anyMap())).thenThrow(new Exception("Derp"));
        server.submitFeedback("url", new ArrayList<FeedbackAnswer>());
    }

    @Test(expected = ObsoleteClientException.class)
    public void submitFeedbackThrowsObsoleteClientExceptionWhenStatusCodeIs404AndIsObsolete() throws Exception {
        RequestBuilder rb = mock(RequestBuilder.class);
        when(connectionBuilder.createConnection()).thenReturn(rb);

        FailedHttpResponseException exception = mock(FailedHttpResponseException.class);
        when(exception.getStatusCode()).thenReturn(404);
        when(exception.getEntityAsString()).thenReturn("{obsolete_client:true}");

        when(rb.postForText(Mockito.anyString(), Mockito.anyMap())).thenThrow(exception);
        server.submitFeedback("url", new ArrayList<FeedbackAnswer>());
    }

    @Test(expected = FailedHttpResponseException.class)
    public void submitFeedbackThrowsHttpExceptionWhenStatusCodeIsNot404() throws Exception {
        RequestBuilder rb = mock(RequestBuilder.class);
        when(connectionBuilder.createConnection()).thenReturn(rb);

        FailedHttpResponseException exception = mock(FailedHttpResponseException.class);
        when(exception.getStatusCode()).thenReturn(403);

        when(rb.postForText(Mockito.anyString(), Mockito.anyMap())).thenThrow(exception);
        server.submitFeedback("url", new ArrayList<FeedbackAnswer>());
    }

    @Test(expected = FailedHttpResponseException.class)
    public void submitFeedbackThrowsHttpExceptionWhenStatusCodeIs404ButIsNotObsolete() throws Exception {
        RequestBuilder rb = mock(RequestBuilder.class);
        when(connectionBuilder.createConnection()).thenReturn(rb);

        FailedHttpResponseException exception = mock(FailedHttpResponseException.class);
        when(exception.getStatusCode()).thenReturn(404);
        when(exception.getEntityAsString()).thenReturn("{obsolete_client:false}");

        when(rb.postForText(Mockito.anyString(), Mockito.anyMap())).thenThrow(exception);
        server.submitFeedback("url", new ArrayList<FeedbackAnswer>());
    }

    @Test
    public void uploadFilesCreatesConnectionAndCallsUploadFileForTextDownload() throws Exception {
        RequestBuilder rb = mock(RequestBuilder.class);
        when(connectionBuilder.createConnection()).thenReturn(rb);
        String url = "mock_url";
        String apiUrl = "wee";
        Exercise exercise = mock(Exercise.class);
        when(exercise.getReturnUrl()).thenReturn(url);

        when(connectionBuilder.addApiCallQueryParameters(url)).thenReturn(apiUrl);
        when(settings.getErrorMsgLocale()).thenReturn(Locale.ENGLISH);
        byte[] data = new byte[5];

        
        
        try {
            server.uploadFile(exercise, data);
        } catch (Exception e) {
            // method throws due to malformed json; it's ok as it happens after
            // the method calls this test is interested in
        }

        verify(connectionBuilder, times(1)).createConnection();
        verify(connectionBuilder, times(1)).addApiCallQueryParameters(url);
        verify(rb, times(1)).uploadFileForTextDownload(eq(apiUrl), Mockito.anyMap(), eq("submission[file]"), eq(data));

    }

    @Test(expected = RuntimeException.class)
    public void uploadFilesThrowsIfJsonContainsErrorField() throws Exception {
        RequestBuilder rb = mock(RequestBuilder.class);
        when(connectionBuilder.createConnection()).thenReturn(rb);
        String url = "mock_url";
        String apiUrl = "wee";
        byte[] data = new byte[5];

        Exercise exercise = mock(Exercise.class);
        when(exercise.getReturnUrl()).thenReturn(url);

        when(connectionBuilder.addApiCallQueryParameters(url)).thenReturn(apiUrl);
        when(rb.uploadFileForTextDownload(eq(apiUrl), Mockito.anyMap(), eq("submission[file]"), eq(data))).thenReturn(
                "{error:error_message}");

        server.uploadFile(exercise, data);
    }

    @Test(expected = RuntimeException.class)
    public void uploadFilesThrowsIfJDoesNotContainErrorOrSubmissionUrlFields() throws Exception {
        RequestBuilder rb = mock(RequestBuilder.class);
        when(connectionBuilder.createConnection()).thenReturn(rb);
        String url = "mock_url";
        String apiUrl = "wee";
        byte[] data = new byte[5];

        Exercise exercise = mock(Exercise.class);
        when(exercise.getReturnUrl()).thenReturn(url);

        when(connectionBuilder.addApiCallQueryParameters(url)).thenReturn(apiUrl);
        when(rb.uploadFileForTextDownload(eq(apiUrl), Mockito.anyMap(), eq("submission[file]"), eq(data))).thenReturn(
                "{foo:bar}");

        server.uploadFile(exercise, data);
    }

    @Test(expected = RuntimeException.class)
    public void uploadFilesThrowsIfUrlIsMalformed() throws Exception {
        RequestBuilder rb = mock(RequestBuilder.class);
        when(connectionBuilder.createConnection()).thenReturn(rb);
        String url = "mock_url";
        String apiUrl = "wee";
        byte[] data = new byte[5];

        Exercise exercise = mock(Exercise.class);
        when(exercise.getReturnUrl()).thenReturn(url);

        when(connectionBuilder.addApiCallQueryParameters(url)).thenReturn(apiUrl);
        when(rb.uploadFileForTextDownload(eq(apiUrl), Mockito.anyMap(), eq("submission[file]"), eq(data))).thenReturn(
                "{submission_url:\"http:/www.asdsads%ad.com.\", paste_url:\"htp:///ww.ab%c\\//.co./\"}");

        server.uploadFile(exercise, data);
    }

    @Test
    public void uploadFilesReturnsCorrectResponse() throws Exception {
        RequestBuilder rb = mock(RequestBuilder.class);
        when(connectionBuilder.createConnection()).thenReturn(rb);
        String url = "mock_url";
        String apiUrl = "wee";
        byte[] data = new byte[5];

        Exercise exercise = mock(Exercise.class);
        when(exercise.getReturnUrl()).thenReturn(url);

        when(connectionBuilder.addApiCallQueryParameters(url)).thenReturn(apiUrl);
        when(rb.uploadFileForTextDownload(eq(apiUrl), Mockito.anyMap(), eq("submission[file]"), eq(data))).thenReturn(
                "{submission_url:\"http://www.submission_url.com\", paste_url:\"http://www.paste_url.com\"}");
        
        when(settings.getErrorMsgLocale()).thenReturn(Locale.ENGLISH);
        SubmissionResponse r = server.uploadFile(exercise, data);
        assertEquals(r.submissionUrl.toString(), "http://www.submission_url.com");
        assertEquals(r.pasteUrl.toString(), "http://www.paste_url.com");
    }

    private CourseList buildMockCourseList() {
        Course c1 = new Course("c1");
        Course c2 = new Course("c2");
        Course c3 = new Course("c3");
        Course[] cl = {c1, c2, c3};
        CourseList courseList = new CourseList();
        courseList.setCourses(cl);
        courseList.setApiVersion("7");
        return courseList;
    }

    private ExerciseList buildMockExerciseList() {
        Exercise e1 = new Exercise("e1");
        Exercise e2 = new Exercise("e2");
        Exercise e3 = new Exercise("e3");
        List<Exercise> exercises = new ArrayList<Exercise>();
        exercises.add(e1);
        exercises.add(e2);
        exercises.add(e3);

        Course course = new Course("c1");
        course.setExercises(exercises);

        ExerciseList eList = new ExerciseList();
        eList.setCourse(course);
        return eList;
    }

    @Test
    public void sendEventLogCallsAddApiCallQueryParameters() {
        RequestBuilder rb = mock(RequestBuilder.class);
        when(connectionBuilder.createConnection()).thenReturn(rb);

        String myUrl = "myUrl";
        server.sendEventLogs(myUrl, new ArrayList<LoggableEvent>());
        verify(connectionBuilder, times(1)).addApiCallQueryParameters(myUrl);
    }

    public void sendEventLogCallsSettingsCorrectly() {
        RequestBuilder rb = mock(RequestBuilder.class);
        when(connectionBuilder.createConnection()).thenReturn(rb);

        server.sendEventLogs("url", new ArrayList<LoggableEvent>());
        verify(settings, times(1)).getUsername();
        verify(settings, times(1)).getPassword();

    }

    @Test
    public void sendEventLogSetsExtraHeadersCorrectly() throws Exception {
        RequestBuilder rb = mock(RequestBuilder.class);
        when(connectionBuilder.createConnection()).thenReturn(rb);

        when(rb.rawPostForText(Mockito.anyString(), Mockito.any(byte[].class), Mockito.anyMap())).thenAnswer(
                new Answer() {
                    @Override
                    public String answer(InvocationOnMock invocation) {
                        Object[] args = invocation.getArguments();

                        Map<String, String> paramMap = (HashMap<String, String>) args[2];

                        assertEquals(3, paramMap.keySet().size());
                        assertEquals(3, paramMap.values().size());

                        assertTrue(paramMap.keySet().contains("X-Tmc-Version"));
                        assertTrue(paramMap.keySet().contains("X-Tmc-Username"));
                        assertTrue(paramMap.keySet().contains("X-Tmc-Password"));

                        assertEquals("1", paramMap.get("X-Tmc-Version"));
                        assertEquals("username", paramMap.get("X-Tmc-Username"));
                        assertEquals("password", paramMap.get("X-Tmc-Password"));

                        return "";
                    }
                });

    }

    @Test(expected = RuntimeException.class)
    public void sendEventLogsThrowsRuntimeExceptionWhenPostForTextThrows() throws Exception {
        RequestBuilder rb = mock(RequestBuilder.class);
        when(connectionBuilder.createConnection()).thenReturn(rb);
        when(rb.rawPostForText(Mockito.anyString(), Mockito.any(byte[].class), Mockito.anyMap())).thenThrow(
                new Exception("Error"));

        server.sendEventLogs("url", new ArrayList<LoggableEvent>());
    }
}
