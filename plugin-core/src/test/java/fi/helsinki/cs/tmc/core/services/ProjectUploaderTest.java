package fi.helsinki.cs.tmc.core.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import fi.helsinki.cs.tmc.core.async.StopStatus;
import fi.helsinki.cs.tmc.core.domain.Exercise;
import fi.helsinki.cs.tmc.core.domain.Project;
import fi.helsinki.cs.tmc.core.domain.SubmissionResult;
import fi.helsinki.cs.tmc.core.services.http.ServerManager;
import fi.helsinki.cs.tmc.core.services.http.SubmissionResponse;

public class ProjectUploaderTest {

    private ProjectUploader uploader;
    private ServerManager server;
    private Project project;
    private Exercise exercise;
    private SubmissionResponse response;
    private SubmissionResult result;

    @Before
    public void setUp() {
        server = mock(ServerManager.class);
        project = mock(Project.class);
        uploader = new ProjectUploader(server);
        response = mock(SubmissionResponse.class);
        exercise = mock(Exercise.class);

        result = mock(SubmissionResult.class);
        when(result.getStatus()).thenReturn(SubmissionResult.Status.OK);
        when(server.getSubmissionResult(any(URI.class))).thenReturn(result);
        when(project.getExercise()).thenReturn(exercise);

        when(server.uploadFile(any(Exercise.class), any(byte[].class))).thenReturn(response);
        when(server.uploadFile(any(Exercise.class), any(byte[].class), anyMap())).thenReturn(response);

    }

    @Test(expected = RuntimeException.class)
    public void zipProjectThrowsExceptionIfProjectIsNull() throws IOException {
        uploader.zipProjects();
    }

    @Test(expected = RuntimeException.class)
    public void handleSumissionResponseThrowsIfProjectIsNull() throws IOException {
        uploader.handleSubmissionResponse();
    }

    @Test(expected = RuntimeException.class)
    public void handleSumissionResponseThrowsIfDataIsNull() throws IOException {

        uploader.setProject(project);
        uploader.handleSubmissionResponse();
    }

    @Test
    public void handleSubmissionCallsServerCorrectlyAndSetsResponse() throws IOException, NoSuchFieldException,
            SecurityException, IllegalArgumentException, IllegalAccessException {

        byte[] data = setData();

        uploader.setProject(project);
        uploader.handleSubmissionResponse();

        verify(project, times(1)).getExercise();
        verify(server, times(1)).uploadFile(exercise, data);

        Field f = uploader.getClass().getDeclaredField("response");
        f.setAccessible(true);

        assertEquals(response, (SubmissionResponse) f.get(uploader));
    }

    @Test
    public void resultIsNullIfHandleSubmissionResultIsStopped() throws NoSuchFieldException, SecurityException,
            IllegalArgumentException, IllegalAccessException, URISyntaxException {

        URI uri = new URI("http://www.mock_url.com");
        SubmissionResponse response = new SubmissionResponse(uri, uri);

        setResponse(response);

        when(result.getStatus()).thenReturn(SubmissionResult.Status.PROCESSING);

        uploader.handleSubmissionResult(new StopStatus() {
            @Override
            public boolean mustStop() {
                return true;
            }
        });

        assertNull(uploader.getResult());

    }

    @Test
    public void getSubmissionResultHasCorrectArgumentInHandleSubmissionResult() throws NoSuchFieldException,
            SecurityException, IllegalArgumentException, IllegalAccessException, URISyntaxException {

        URI uri = new URI("http://www.mock_url.com");
        SubmissionResponse response = new SubmissionResponse(uri, uri);

        setResponse(response);
        when(result.getStatus()).thenReturn(SubmissionResult.Status.PROCESSING);

        uploader.handleSubmissionResult(new StopStatus() {
            @Override
            public boolean mustStop() {
                return true;
            }
        });
        verify(server, times(1)).getSubmissionResult(uri);
    }

    @Test
    public void resultIsCorrectAfterHandleSubmissionResult() throws NoSuchFieldException, SecurityException,
            IllegalArgumentException, IllegalAccessException, URISyntaxException {

        URI uri = new URI("http://www.mock_url.com");
        SubmissionResponse response = new SubmissionResponse(uri, uri);

        setResponse(response);

        uploader.handleSubmissionResult(new StopStatus() {
            @Override
            public boolean mustStop() {
                return true;
            }
        });
        assertEquals(result, uploader.getResult());
    }

    @Test
    public void extraParamsAreGivenToServerWhenPasting() throws IOException, NoSuchFieldException,
            IllegalAccessException {
        uploader.setAsPaste("message");
        uploader.setProject(project);
        setData();

        when(server.uploadFile(any(Exercise.class), any(byte[].class), anyMap())).thenAnswer(
                new Answer<SubmissionResponse>() {

                    @Override
                    public SubmissionResponse answer(InvocationOnMock invocation) throws Throwable {
                        Map<String, String> param = (Map<String, String>) invocation.getArguments()[2];

                        assertTrue(param.keySet().contains("paste"));
                        assertTrue(param.get("paste").equals("1"));
                        assertTrue(param.keySet().contains("message_for_paste"));
                        assertTrue(param.get("message_for_paste").equals("message"));

                        return response;
                    }
                });

        uploader.handleSubmissionResponse();
        verify(server, times(1)).uploadFile(any(Exercise.class), any(byte[].class), anyMap());
    }

    @Test
    public void extraParamsAreGivenToServerWhenPastingWithoutMessageFieldWhenNoMessage() throws IOException,
            NoSuchFieldException, IllegalAccessException {
        uploader.setAsPaste("");
        uploader.setProject(project);
        setData();

        when(server.uploadFile(any(Exercise.class), any(byte[].class), anyMap())).thenAnswer(
                new Answer<SubmissionResponse>() {

                    @Override
                    public SubmissionResponse answer(InvocationOnMock invocation) throws Throwable {
                        Map<String, String> param = (Map<String, String>) invocation.getArguments()[2];

                        assertTrue(param.keySet().contains("paste"));
                        assertTrue(param.get("paste").equals("1"));
                        assertFalse(param.keySet().contains("message_for_paste"));

                        return response;
                    }
                });

        uploader.handleSubmissionResponse();
        verify(server, times(1)).uploadFile(any(Exercise.class), any(byte[].class), anyMap());
    }

    @Test
    public void extraParamsAreGivenToServerSetAsRequest() throws IOException, NoSuchFieldException,
            IllegalAccessException {
        uploader.setAsRequest("message");
        uploader.setProject(project);
        setData();

        when(server.uploadFile(any(Exercise.class), any(byte[].class), anyMap())).thenAnswer(
                new Answer<SubmissionResponse>() {

                    @Override
                    public SubmissionResponse answer(InvocationOnMock invocation) throws Throwable {
                        Map<String, String> param = (Map<String, String>) invocation.getArguments()[2];

                        assertTrue(param.keySet().contains("request_review"));
                        assertTrue(param.get("request_review").equals("1"));
                        assertTrue(param.keySet().contains("message_for_reviewer"));
                        assertTrue(param.get("message_for_reviewer").equals("message"));

                        return response;
                    }
                });

        uploader.handleSubmissionResponse();
        verify(server, times(1)).uploadFile(any(Exercise.class), any(byte[].class), anyMap());
    }

    @Test
    public void extraParamsAreGivenToServerSetAsRequestWithoutMessageFieldWhenNoMessage() throws IOException,
            NoSuchFieldException, IllegalAccessException {
        uploader.setAsRequest("");
        uploader.setProject(project);
        setData();

        when(server.uploadFile(any(Exercise.class), any(byte[].class), anyMap())).thenAnswer(
                new Answer<SubmissionResponse>() {

                    @Override
                    public SubmissionResponse answer(InvocationOnMock invocation) throws Throwable {
                        Map<String, String> param = (Map<String, String>) invocation.getArguments()[2];

                        assertTrue(param.keySet().contains("request_review"));
                        assertTrue(param.get("request_review").equals("1"));
                        assertFalse(param.keySet().contains("message_for_reviewer"));

                        return response;
                    }
                });

        uploader.handleSubmissionResponse();
        verify(server, times(1)).uploadFile(any(Exercise.class), any(byte[].class), anyMap());
    }

    @Test
    public void handleSubmissionResponseCallsServerGetSubmissionResult() throws URISyntaxException,
            NoSuchFieldException, IllegalAccessException {
        URI uri = new URI("http://www.mock_url.com");
        SubmissionResponse response = new SubmissionResponse(uri, uri);
        setResponse(response);

        uploader.handleSubmissionResult(new StopStatus() {

            @Override
            public boolean mustStop() {
                return false;
            }

        });

        verify(server, times(1)).getSubmissionResult(response.submissionUrl);
    }

    @Test
    public void setterGetterWorksForProject() {
        uploader.setProject(project);
        assertEquals(project, uploader.getProject());
    }

    @Test
    public void getterWorksForResponse() throws NoSuchFieldException, IllegalAccessException, IOException {
        uploader.setProject(project);
        setData();
        uploader.handleSubmissionResponse();
        assertEquals(response, uploader.getResponse());
    }

    @Test
    public void resultIsNullIfMustStop() throws NoSuchFieldException, IllegalAccessException, IOException,
            URISyntaxException {
        URI uri = new URI("http://www.mock_url.com");
        SubmissionResponse response = new SubmissionResponse(uri, uri);
        setResponse(response);

        when(result.getStatus()).thenReturn(SubmissionResult.Status.PROCESSING);
        uploader.handleSubmissionResult(new StopStatus() {

            @Override
            public boolean mustStop() {
                return true;
            }

        });
        assertEquals(null, uploader.getResult());
    }

    private byte[] setData() throws NoSuchFieldException, IllegalAccessException {
        Field f = uploader.getClass().getDeclaredField("data");
        f.setAccessible(true);

        byte[] data = new byte[5];
        f.set(uploader, data);
        return data;
    }

    private void setResponse(SubmissionResponse response) throws NoSuchFieldException, IllegalAccessException {
        Field f = uploader.getClass().getDeclaredField("response");
        f.setAccessible(true);
        f.set(uploader, response);
    }

}
