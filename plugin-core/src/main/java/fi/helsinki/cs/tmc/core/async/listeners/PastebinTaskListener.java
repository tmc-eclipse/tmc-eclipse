package fi.helsinki.cs.tmc.core.async.listeners;

import fi.helsinki.cs.tmc.core.async.BackgroundTaskListener;
import fi.helsinki.cs.tmc.core.async.tasks.PastebinTask;
import fi.helsinki.cs.tmc.core.ui.IdeUIInvoker;

public class PastebinTaskListener implements BackgroundTaskListener {

    private PastebinTask task;
    private IdeUIInvoker uiInvoker;

    public PastebinTaskListener(PastebinTask task, IdeUIInvoker uiInvoker) {
        this.task = task;
        this.uiInvoker = uiInvoker;
    }

    @Override
    public void onBegin() {
    }

    @Override
    public void onSuccess() {

        final String pasteUrl = task.getPasteUrl();

        if (pasteUrl == null) {
            uiInvoker.raiseVisibleException("The server returned no URL for the paste. Please contact TMC support.");
            return;
        }

        uiInvoker.invokePastebinResultDialog(pasteUrl);
    }

    @Override
    public void onFailure() {
        uiInvoker.raiseVisibleException("Failed to create the requested pastebin.");
    }

    @Override
    public void onInterruption() {
    }
}
