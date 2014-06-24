package fi.helsinki.cs.tmc.core.async.tasks;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import fi.helsinki.cs.tmc.core.async.BackgroundTask;
import fi.helsinki.cs.tmc.core.async.TaskFeedback;
import fi.helsinki.cs.tmc.core.async.tasks.PastebinTask;
import fi.helsinki.cs.tmc.core.domain.Project;
import fi.helsinki.cs.tmc.core.services.ProjectDAO;
import fi.helsinki.cs.tmc.core.services.ProjectUploader;
import fi.helsinki.cs.tmc.core.services.http.SubmissionResponse;
import fi.helsinki.cs.tmc.core.ui.IdeUIInvoker;

public class PastebinTaskTest {
    private ProjectUploader uploader;
    private TaskFeedback progress;
    private PastebinTask task;
    private IdeUIInvoker invoker;
    private ProjectDAO dao;
    private String path;

    @Before
    public void setup() {
        uploader = mock(ProjectUploader.class);
        progress = mock(TaskFeedback.class);
        when(progress.isCancelRequested()).thenReturn(false);

        dao = mock(ProjectDAO.class);
        path = "path";
        invoker = mock(IdeUIInvoker.class);
        task = new PastebinTask(uploader, path, "pastemessage", dao, invoker);
    }

    @Test
    public void hasCorrectDescription() {
        assertEquals("Creating a pastebin", task.getDescription());
    }

    @Test
    public void taskReturnsSuccessAfterProperInitializationOfAllComponents() throws IOException {

        assertEquals(BackgroundTask.RETURN_SUCCESS, task.start(progress));

        verify(progress, times(2)).incrementProgress(1);
        verify(uploader, times(1)).setAsPaste("pastemessage");
        verify(uploader, times(1)).zipProjects();
        verify(uploader, times(1)).handleSubmissionResponse();
    }

    @Test
    public void taskReturnsInterruptedIfCancelIsRequestedAtFirstCheckpoint() throws IOException {
        when(progress.isCancelRequested()).thenReturn(true);

        assertEquals(BackgroundTask.RETURN_INTERRUPTED, task.start(progress));

        verify(progress, times(0)).incrementProgress(1);
    }

    @Test
    public void projectIsSetToUploader() {
        Project project = mock(Project.class);
        when(dao.getProjectByFile(path)).thenReturn(project);
        task.start(progress);
        verify(uploader, times(1)).setProject(project);
    }

    @Test
    public void taskReturnsInterruptedIfCancelIsRequestedAtSecondCheckpoint() throws IOException {

        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) {
                when(progress.isCancelRequested()).thenReturn(true);
                return null;
            }
        }).when(uploader).handleSubmissionResponse();

        assertEquals(BackgroundTask.RETURN_INTERRUPTED, task.start(progress));

        verify(progress, times(1)).incrementProgress(1);
    }

    @Test
    public void taskReturnsFailureIfAnExceptionIsThrown() throws IOException {

        doThrow(new IOException()).when(uploader).zipProjects();
        assertEquals(BackgroundTask.RETURN_FAILURE, task.start(progress));
    }

    @Test
    public void invokerIsCalledIfAnExceptionIsThrown() throws IOException {

        doThrow(new IOException("Foobar")).when(uploader).zipProjects();
        task.start(progress);
        verify(invoker, times(1)).raiseVisibleException(
                "An error occurred while uploading exercise to pastebin:\nFoobar");
    }

    @Test
    public void getPasteUrlReturnsTheUrlFromTheResponse() throws URISyntaxException {
        SubmissionResponse response = new SubmissionResponse(new URI("submission"), new URI("paste"));
        when(uploader.getResponse()).thenReturn(response);

        assertEquals("paste", task.getPasteUrl());
    }

}
