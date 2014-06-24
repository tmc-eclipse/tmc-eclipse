package fi.helsinki.cs.tmc.core.async.listeners;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import fi.helsinki.cs.tmc.core.async.tasks.PastebinTask;
import fi.helsinki.cs.tmc.core.ui.IdeUIInvoker;

public class PastebinTaskListenerTest {
    private PastebinTask task;
    private IdeUIInvoker invoker;
    private PastebinTaskListener listener;

    @Before
    public void setup() {
        task = mock(PastebinTask.class);
        invoker = mock(IdeUIInvoker.class);
        listener = new PastebinTaskListener(task, invoker);
    }

    @Test
    public void InvokesTheUIAfterSuccess() {
        when(task.getPasteUrl()).thenReturn("pasteurl");

        listener.onSuccess();
        verify(task, times(1)).getPasteUrl();
        verify(invoker, times(1)).invokePastebinResultDialog("pasteurl");
    }

    @Test
    public void raiseErrorWithoutUIInvocationIfUrlIsNull() {
        when(task.getPasteUrl()).thenReturn(null);

        listener.onSuccess();
        verify(invoker, times(1)).raiseVisibleException(
                "The server returned no URL for the paste. Please contact TMC support.");

        verify(task, times(1)).getPasteUrl();
        verify(invoker, times(0)).invokePastebinResultDialog(any(String.class));
    }

    @Test
    public void raisesAnErrorOnFailure() {
        listener.onFailure();
        verify(invoker, times(1)).raiseVisibleException("Failed to create the requested pastebin.");
    }

    @Test
    public void doNothingOnBegin() {
        listener.onBegin();
        verifyNoMoreInteractions(task, invoker);
    }

}
