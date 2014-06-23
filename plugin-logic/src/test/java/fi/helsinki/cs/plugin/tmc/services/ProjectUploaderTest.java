package fi.helsinki.cs.plugin.tmc.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Before;
import org.junit.Test;

import fi.helsinki.cs.plugin.tmc.async.StopStatus;
import fi.helsinki.cs.plugin.tmc.domain.Exercise;
import fi.helsinki.cs.plugin.tmc.domain.Project;
import fi.helsinki.cs.plugin.tmc.domain.SubmissionResult;
import fi.helsinki.cs.plugin.tmc.services.http.ServerManager;
import fi.helsinki.cs.plugin.tmc.services.http.SubmissionResponse;

public class ProjectUploaderTest {

    private ProjectUploader uploader;
    private ServerManager server;
    private Project project;

    @Before
    public void setUp() {
        server = mock(ServerManager.class);
        project = mock(Project.class);
        uploader = new ProjectUploader(server);
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

        uploader.setProject(mock(Project.class));
        uploader.handleSubmissionResponse();
    }

    @Test
    public void handleSubmissionCallsServerCorrectlyAndSetsResponse() throws IOException, NoSuchFieldException,
            SecurityException, IllegalArgumentException, IllegalAccessException {

        Field f = uploader.getClass().getDeclaredField("data");
        f.setAccessible(true);

        byte[] data = new byte[5];
        f.set(uploader, data);

        Project project = mock(Project.class);
        Exercise exercise = mock(Exercise.class);

        SubmissionResponse response = mock(SubmissionResponse.class);
        when(project.getExercise()).thenReturn(exercise);
        when(server.uploadFile(exercise, data)).thenReturn(response);

        uploader.setProject(project);
        uploader.handleSubmissionResponse();

        verify(project, times(1)).getExercise();
        verify(server, times(1)).uploadFile(exercise, data);

        f = uploader.getClass().getDeclaredField("response");
        f.setAccessible(true);

        assertEquals(response, (SubmissionResponse) f.get(uploader));
    }

    @Test
    public void resultIsNullIfHandleSubmissionResultIsStopped() throws NoSuchFieldException, SecurityException,
            IllegalArgumentException, IllegalAccessException, URISyntaxException {

        URI uri = new URI("http://www.mock_url.com");
        SubmissionResponse response = new SubmissionResponse(uri, uri);

        Field f = uploader.getClass().getDeclaredField("response");
        f.setAccessible(true);
        f.set(uploader, response);

        SubmissionResult result = mock(SubmissionResult.class);
        when(result.getStatus()).thenReturn(SubmissionResult.Status.PROCESSING);
        when(server.getSubmissionResult(uri)).thenReturn(result);

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

        Field f = uploader.getClass().getDeclaredField("response");
        f.setAccessible(true);
        f.set(uploader, response);

        SubmissionResult result = mock(SubmissionResult.class);
        when(result.getStatus()).thenReturn(SubmissionResult.Status.PROCESSING);
        when(server.getSubmissionResult(uri)).thenReturn(result);

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

        Field f = uploader.getClass().getDeclaredField("response");
        f.setAccessible(true);
        f.set(uploader, response);

        SubmissionResult result = mock(SubmissionResult.class);
        when(result.getStatus()).thenReturn(SubmissionResult.Status.OK);
        when(server.getSubmissionResult(uri)).thenReturn(result);

        uploader.handleSubmissionResult(new StopStatus() {
            @Override
            public boolean mustStop() {
                return true;
            }
        });
        assertEquals(result, uploader.getResult());
    }

}
