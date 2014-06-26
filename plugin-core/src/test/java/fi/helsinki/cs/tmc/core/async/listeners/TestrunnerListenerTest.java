package fi.helsinki.cs.tmc.core.async.listeners;

import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import fi.helsinki.cs.tmc.core.async.tasks.TestrunnerTask;
import fi.helsinki.cs.tmc.core.domain.TestCaseResult;
import fi.helsinki.cs.tmc.core.domain.TestRunResult;
import fi.helsinki.cs.tmc.core.ui.IdeUIInvoker;

public class TestrunnerListenerTest {
    private TestrunnerListener listener;
    private TestrunnerTask task;
    private IdeUIInvoker invoker;
    private TestRunResult result;

    @Before
    public void setUp() {
        task = mock(TestrunnerTask.class);
        invoker = mock(IdeUIInvoker.class);
        result = mock(TestRunResult.class);
        when(result.getTestCaseResults()).thenReturn(new ArrayList<TestCaseResult>());
        when(task.get()).thenReturn(result);
        listener = new TestrunnerListener(task, invoker);
    }

    @Test
    public void onBeginDoesNothing() {
        listener.onBegin();
        verifyNoMoreInteractions(invoker);
        verifyNoMoreInteractions(task);
    }

    @Test
    public void onFailureDoesNothing() {
        listener.onFailure();
        verifyNoMoreInteractions(invoker);
        verifyNoMoreInteractions(task);
    }

    @Test
    public void onInterruptionDoesNothing() {
        listener.onInterruption();
        verifyNoMoreInteractions(invoker);
        verifyNoMoreInteractions(task);
    }

    @Test
    public void onSuccessCallsTaskGet() {
        listener.onSuccess();
        verify(task, times(1)).get();
    }

    @Test
    public void onSuccessInvokesTestResultWindow() {
        listener.onSuccess();
        verify(invoker, times(1)).invokeTestResultWindow(anyListOf(TestCaseResult.class));
    }

    @Test
    public void onSuccessInvokesSubmitToServerWindowIfAllTestPasses() {

        List<TestCaseResult> list = new ArrayList<TestCaseResult>();
        list.add(mock(TestCaseResult.class));
        list.add(mock(TestCaseResult.class));
        list.add(mock(TestCaseResult.class));

        when(list.get(0).isSuccessful()).thenReturn(true);
        when(list.get(1).isSuccessful()).thenReturn(true);
        when(list.get(2).isSuccessful()).thenReturn(true);

        when(result.getTestCaseResults()).thenReturn(list);
        listener.onSuccess();
        verify(invoker, times(1)).invokeSubmitToServerWindow();
    }

    @Test
    public void onSuccessDoesNotInvokesSubmitToServerWindowIfAllTestsDoNotPass() {

        List<TestCaseResult> list = new ArrayList<TestCaseResult>();
        list.add(mock(TestCaseResult.class));
        list.add(mock(TestCaseResult.class));
        list.add(mock(TestCaseResult.class));

        when(list.get(0).isSuccessful()).thenReturn(true);
        when(list.get(1).isSuccessful()).thenReturn(false);
        when(list.get(2).isSuccessful()).thenReturn(true);

        when(result.getTestCaseResults()).thenReturn(list);

        verify(invoker, times(0)).invokeSubmitToServerWindow();
        listener.onSuccess();
    }
}
