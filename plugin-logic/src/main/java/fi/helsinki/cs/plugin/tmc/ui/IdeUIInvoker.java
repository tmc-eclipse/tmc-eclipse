package fi.helsinki.cs.plugin.tmc.ui;

import fi.helsinki.cs.plugin.tmc.domain.SubmissionResult;

public interface IdeUIInvoker {
    public void invokeTestResultWindow(SubmissionResult result);

    public void invokeAllTestsPassedWindow(SubmissionResult result, String exerciseName);

    public void invokeSomeTestsFailedWindow(SubmissionResult result, String exerciseName);

    public void invokeAllTestsFailedWindow(SubmissionResult result, String exerciseName);

}
