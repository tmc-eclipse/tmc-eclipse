package fi.helsinki.cs.plugin.tmc.async.tasks.listeners;

import fi.helsinki.cs.plugin.tmc.async.BackgroundTaskListener;
import fi.helsinki.cs.plugin.tmc.async.tasks.CodeReviewRequestTask;
import fi.helsinki.cs.plugin.tmc.ui.IdeUIInvoker;

public class CodeReviewRequestListener implements BackgroundTaskListener {

    private CodeReviewRequestTask task;
    private IdeUIInvoker uiInvoker;

    public CodeReviewRequestListener(CodeReviewRequestTask task, IdeUIInvoker uiInvoker) {
        this.task = task;
        this.uiInvoker = uiInvoker;
    }

    @Override
    public void onBegin() {
    }

    @Override
    public void onSuccess() {
        uiInvoker.invokeCodeReviewRequestSuccefullySentWindow();
    }

    @Override
    public void onFailure() {
        uiInvoker.raiseVisibleException("Failed to create the code review request.");

    }

    @Override
    public void onInterruption() {
    }
}