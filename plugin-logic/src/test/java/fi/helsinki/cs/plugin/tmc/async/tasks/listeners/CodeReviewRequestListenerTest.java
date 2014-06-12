package fi.helsinki.cs.plugin.tmc.async.tasks.listeners;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.Before;
import org.junit.Test;

import fi.helsinki.cs.plugin.tmc.Core;
import fi.helsinki.cs.plugin.tmc.TMCErrorHandler;
import fi.helsinki.cs.plugin.tmc.async.tasks.CodeReviewRequestTask;
import fi.helsinki.cs.plugin.tmc.ui.IdeUIInvoker;

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
        TMCErrorHandler errorhandler = mock(TMCErrorHandler.class);
        Core.setErrorHandler(errorhandler);

        listener.onFailure();
        verify(errorhandler, times(1)).raise("Failed to create the code review request.");
    }

    @Test
    public void doNothingOnBegin() {
        listener.onBegin();
        verifyNoMoreInteractions(task, invoker);
    }

}