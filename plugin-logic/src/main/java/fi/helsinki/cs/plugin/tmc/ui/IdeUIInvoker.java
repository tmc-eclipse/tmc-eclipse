package fi.helsinki.cs.plugin.tmc.ui;

import fi.helsinki.cs.plugin.tmc.domain.SubmissionResult;

public interface IdeUIInvoker {
    public void invokeTestResultWindow(SubmissionResult result);

    public void invokeAllTestsPassedWindow(SubmissionResult result);

    public void invokeSomeTestsFailedWindow(SubmissionResult result);

    public void invokeAllTestsFailedWindow(SubmissionResult result);

}
