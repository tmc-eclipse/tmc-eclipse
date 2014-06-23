package fi.helsinki.cs.plugin.tmc.async.tasks.listeners;

import fi.helsinki.cs.plugin.tmc.async.BackgroundTaskListener;
import fi.helsinki.cs.plugin.tmc.async.tasks.UploaderTask;
import fi.helsinki.cs.plugin.tmc.domain.Exercise;
import fi.helsinki.cs.plugin.tmc.domain.SubmissionResult;
import fi.helsinki.cs.plugin.tmc.ui.IdeUIInvoker;
import fi.helsinki.cs.plugin.tmc.utils.ProjectIconHandler;

public class UploadTaskListener implements BackgroundTaskListener {

    private UploaderTask task;
    private IdeUIInvoker uiInvoker;
    private ProjectIconHandler projectIconHandler;

    public UploadTaskListener(UploaderTask task, IdeUIInvoker uiInvoker, ProjectIconHandler projectIconHandler) {
        this.task = task;
        this.uiInvoker = uiInvoker;
        this.projectIconHandler = projectIconHandler;
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
        Exercise exercise = task.getProject().getExercise();
        uiInvoker.invokeTestResultWindow(result.getTestCases());
        
        if (result.allTestCasesSucceeded()) {
            uiInvoker.invokeAllTestsPassedWindow(result, exerciseName);
            exercise.setCompleted(true);
        } else if (result.allTestCasesFailed()) {
            uiInvoker.invokeAllTestsFailedWindow(result, exerciseName);
            exercise.setAttempted(true);
        } else {
            uiInvoker.invokeSomeTestsFailedWindow(result, exerciseName);
            exercise.setAttempted(true);
        }
        projectIconHandler.updateIcon(exercise);

    }

    @Override
    public void onFailure() {
        // error messages handled by the task; no need to do anything here
    }

    @Override
    public void onInterruption() {
    }
}
