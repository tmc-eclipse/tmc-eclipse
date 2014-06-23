package fi.helsinki.cs.plugin.tmc.async.tasks;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import fi.helsinki.cs.plugin.tmc.async.BackgroundTask;
import fi.helsinki.cs.plugin.tmc.async.TaskFeedback;
import fi.helsinki.cs.plugin.tmc.domain.Project;
import fi.helsinki.cs.plugin.tmc.services.ProjectDAO;
import fi.helsinki.cs.plugin.tmc.services.ProjectUploader;
import fi.helsinki.cs.plugin.tmc.ui.IdeUIInvoker;

public class CodeReviewRequestTaskTest {
    private ProjectUploader uploader;
    private TaskFeedback progress;

    private CodeReviewRequestTask task;

    @Before
    public void setUp() throws Exception {
        uploader = mock(ProjectUploader.class);
        ProjectDAO dao = mock(ProjectDAO.class);
        IdeUIInvoker invoker = mock(IdeUIInvoker.class);
        task = new CodeReviewRequestTask(uploader, "path", "requestMessage", dao, invoker);

        progress = mock(TaskFeedback.class);

    }

    @Test
    public void hasCorrectDescription() {
        assertEquals("Creating code review request", task.getDescription());
    }

    @Test
    public void runningWithCorrectSettingsReturnsSuccessAndCallsRequiredMethods() throws IOException {
        when(progress.isCancelRequested()).thenReturn(false);

        assertEquals(BackgroundTask.RETURN_SUCCESS, task.start(progress));

        verify(uploader, times(1)).setProject(any(Project.class));
        verify(uploader, times(1)).setAsRequest("requestMessage");
        verify(uploader, times(1)).zipProjects();
    }

    @Test
    public void taskReturnsInterruptedIfCancelIsRequestedAtFirstCheckpoint() throws IOException {
        when(progress.isCancelRequested()).thenReturn(true);

        assertEquals(BackgroundTask.RETURN_INTERRUPTED, task.start(progress));

        verify(progress, times(0)).incrementProgress(1);
    }

    @Test
    public void taskReturnsInterruptedIfStoppedAtFirstCheckpoint() throws IOException {
        task.stop();
        assertEquals(BackgroundTask.RETURN_INTERRUPTED, task.start(progress));
        verify(progress, times(0)).incrementProgress(1);
    }

    @Test
    public void taskReturnsInterruptedIfCancelIsRequestedAtSecondCheckpoint() throws IOException {
        when(progress.isCancelRequested()).thenReturn(false);

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
        when(progress.isCancelRequested()).thenReturn(false);
        doThrow(new IOException()).when(uploader).zipProjects();
        assertEquals(BackgroundTask.RETURN_FAILURE, task.start(progress));
    }

}
