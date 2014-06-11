package fi.helsinki.cs.plugin.tmc.async.tasks.listeners;

import fi.helsinki.cs.plugin.tmc.Core;
import fi.helsinki.cs.plugin.tmc.async.BackgroundTaskListener;
import fi.helsinki.cs.plugin.tmc.async.tasks.PastebinTask;
import fi.helsinki.cs.plugin.tmc.async.tasks.UploaderTask;
import fi.helsinki.cs.plugin.tmc.domain.SubmissionResult;
import fi.helsinki.cs.plugin.tmc.ui.IdeUIInvoker;

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
            Core.getErrorHandler().raise("The server returned no URL for the paste. Please contact TMC support.");
            return;
        }

        uiInvoker.invokePastebinResultDialog(pasteUrl);
    }

    @Override
    public void onFailure() {
        Core.getErrorHandler().raise("Failed to create the requested pastebin.");
    }
}
