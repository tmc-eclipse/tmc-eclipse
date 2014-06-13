package fi.helsinki.cs.plugin.tmc.async.tasks.listeners;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import fi.helsinki.cs.plugin.tmc.async.tasks.UploaderTask;
import fi.helsinki.cs.plugin.tmc.domain.Exercise;
import fi.helsinki.cs.plugin.tmc.domain.Project;
import fi.helsinki.cs.plugin.tmc.domain.SubmissionResult;
import fi.helsinki.cs.plugin.tmc.domain.TestCaseResult;
import fi.helsinki.cs.plugin.tmc.ui.IdeUIInvoker;

public class UploadTaskListenerTest {

    private UploadTaskListener listener;
    private UploaderTask task;
    private IdeUIInvoker invoker;

    @Before
    public void setUp() {
        invoker = mock(IdeUIInvoker.class);
        task = mock(UploaderTask.class);
        listener = new UploadTaskListener(task, invoker);
    }

    @Test
    public void noExtraMethodsCalledIfSubmissionResultIsNull() {
        when(task.getResult()).thenReturn(null);

        listener.onSuccess();

        verify(task, times(1)).getResult();
        verify(task, times(0)).getProject();

        verify(invoker, times(0)).invokeTestResultWindow(Mockito.anyListOf(TestCaseResult.class));
        verify(invoker, times(0)).invokeAllTestsPassedWindow(Mockito.any(SubmissionResult.class), Mockito.anyString());
        verify(invoker, times(0)).invokeAllTestsFailedWindow(Mockito.any(SubmissionResult.class), Mockito.anyString());
        verify(invoker, times(0)).invokeSomeTestsFailedWindow(Mockito.any(SubmissionResult.class), Mockito.anyString());
    }

    @Test
    public void correctMethodsCalledIfAllTestCasesSucceeded() {
        SubmissionResult result = mock(SubmissionResult.class);
        when(result.allTestCasesSucceeded()).thenReturn(true);
        when(result.allTestCasesFailed()).thenReturn(false);

        Exercise exercise = mock(Exercise.class);
        when(exercise.getName()).thenReturn("foo");

        Project project = mock(Project.class);
        when(project.getExercise()).thenReturn(exercise);

        when(task.getProject()).thenReturn(project);

        when(task.getResult()).thenReturn(result);
        listener.onSuccess();

        verify(task, times(1)).getResult();
        verify(task, times(1)).getProject();

        verify(invoker, times(1)).invokeTestResultWindow(Mockito.anyListOf(TestCaseResult.class));
        verify(invoker, times(1)).invokeAllTestsPassedWindow(Mockito.any(SubmissionResult.class), Mockito.anyString());
        verify(invoker, times(0)).invokeAllTestsFailedWindow(Mockito.any(SubmissionResult.class), Mockito.anyString());
        verify(invoker, times(0)).invokeSomeTestsFailedWindow(Mockito.any(SubmissionResult.class), Mockito.anyString());
    }

    @Test
    public void correctMethodsCalledIfAllTestCasesFail() {
        SubmissionResult result = mock(SubmissionResult.class);
        when(result.allTestCasesSucceeded()).thenReturn(false);
        when(result.allTestCasesFailed()).thenReturn(true);

        Exercise exercise = mock(Exercise.class);
        when(exercise.getName()).thenReturn("foo");

        Project project = mock(Project.class);
        when(project.getExercise()).thenReturn(exercise);

        when(task.getProject()).thenReturn(project);

        when(task.getResult()).thenReturn(result);
        listener.onSuccess();

        verify(task, times(1)).getResult();
        verify(task, times(1)).getProject();

        verify(invoker, times(1)).invokeTestResultWindow(Mockito.anyListOf(TestCaseResult.class));
        verify(invoker, times(0)).invokeAllTestsPassedWindow(Mockito.any(SubmissionResult.class), Mockito.anyString());
        verify(invoker, times(1)).invokeAllTestsFailedWindow(Mockito.any(SubmissionResult.class), Mockito.anyString());
        verify(invoker, times(0)).invokeSomeTestsFailedWindow(Mockito.any(SubmissionResult.class), Mockito.anyString());
    }

    @Test
    public void correctMethodsCalledIfSomeTestCasesFail() {
        SubmissionResult result = mock(SubmissionResult.class);
        when(result.allTestCasesSucceeded()).thenReturn(false);
        when(result.allTestCasesFailed()).thenReturn(false);

        Exercise exercise = mock(Exercise.class);
        when(exercise.getName()).thenReturn("foo");

        Project project = mock(Project.class);
        when(project.getExercise()).thenReturn(exercise);

        when(task.getProject()).thenReturn(project);

        when(task.getResult()).thenReturn(result);
        listener.onSuccess();

        verify(task, times(1)).getResult();
        verify(task, times(1)).getProject();

        verify(invoker, times(1)).invokeTestResultWindow(Mockito.anyListOf(TestCaseResult.class));
        verify(invoker, times(0)).invokeAllTestsPassedWindow(Mockito.any(SubmissionResult.class), Mockito.anyString());
        verify(invoker, times(0)).invokeAllTestsFailedWindow(Mockito.any(SubmissionResult.class), Mockito.anyString());
        verify(invoker, times(1)).invokeSomeTestsFailedWindow(Mockito.any(SubmissionResult.class), Mockito.anyString());
    }
}
