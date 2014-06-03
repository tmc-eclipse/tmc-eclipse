package fi.helsinki.cs.plugin.tmc.async.tasks.listeners;

import fi.helsinki.cs.plugin.tmc.async.BackgroundTaskListener;
import fi.helsinki.cs.plugin.tmc.async.tasks.UploaderTask;
import fi.helsinki.cs.plugin.tmc.domain.SubmissionResult;
import fi.helsinki.cs.plugin.tmc.ui.IdeUIInvoker;

public class UploadTaskListener implements BackgroundTaskListener {

    private UploaderTask task;
    private IdeUIInvoker uiInvoker;

    public UploadTaskListener(UploaderTask task, IdeUIInvoker uiInvoker) {
        this.task = task;
        this.uiInvoker = uiInvoker;
    }

    @Override
    public void onBegin() {
        System.out.println("OnBegin");

    }

    @Override
    public void onSuccess() {

        final SubmissionResult result = task.getResult();

        if (result == null) {
            return;
        }

        uiInvoker.invokeTestResultWindow(result);

        if (result.allTestCasesSucceeded()) {
            uiInvoker.invokeAllTestsPassedWindow(result);
        }

    }

    @Override
    public void onFailure() {
        System.out.println("OnFailure");

    }

}
