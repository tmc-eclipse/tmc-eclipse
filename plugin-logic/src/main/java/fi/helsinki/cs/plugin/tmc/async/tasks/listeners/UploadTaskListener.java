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

    }

    @Override
    public void onSuccess() {

        final SubmissionResult result = task.getResult();

        if (result == null) {
            return;
        }
        String exerciseName = task.getProject().getExercise().getName();
        uiInvoker.invokeTestResultWindow(result.getTestCases());

        if (result.allTestCasesSucceeded()) {
            uiInvoker.invokeAllTestsPassedWindow(result, exerciseName);
        } else if (result.allTestCasesFailed()) {
            uiInvoker.invokeAllTestsFailedWindow(result, exerciseName);
        } else {
            uiInvoker.invokeSomeTestsFailedWindow(result, exerciseName);
        }

    }

    @Override
    public void onFailure() {
        // error messages handled by the task; no need to do anything here
    }
}
