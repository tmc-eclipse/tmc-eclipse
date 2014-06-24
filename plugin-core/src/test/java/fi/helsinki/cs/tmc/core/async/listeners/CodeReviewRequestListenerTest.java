package fi.helsinki.cs.tmc.core.async.listeners;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.Before;
import org.junit.Test;

import fi.helsinki.cs.tmc.core.async.listeners.CodeReviewRequestListener;
import fi.helsinki.cs.tmc.core.async.tasks.CodeReviewRequestTask;
import fi.helsinki.cs.tmc.core.ui.IdeUIInvoker;

public class CodeReviewRequestListenerTest {
    private CodeReviewRequestTask task;
    private IdeUIInvoker invoker;
    private CodeReviewRequestListener listener;

    @Before
    public void setup() {
        task = mock(CodeReviewRequestTask.class);
        invoker = mock(IdeUIInvoker.class);
        listener = new CodeReviewRequestListener(task, invoker);
    }

    @Test
    public void InvokesTheUIAfterSuccess() {
        listener.onSuccess();
        verify(invoker, times(1)).invokeCodeReviewRequestSuccefullySentWindow();
    }

    @Test
    public void raisesAnErrorOnFailure() {
        listener.onFailure();
        verify(invoker, times(1)).raiseVisibleException("Failed to create the code review request.");
    }

    @Test
    public void doNothingOnBegin() {
        listener.onBegin();
        verifyNoMoreInteractions(task, invoker);
    }

}