package fi.helsinki.cs.plugin.tmc.async.tasks.listeners;

import fi.helsinki.cs.plugin.tmc.async.BackgroundTaskListener;
import fi.helsinki.cs.plugin.tmc.async.tasks.TestrunnerTask;
import fi.helsinki.cs.plugin.tmc.domain.TestRunResult;
import fi.helsinki.cs.plugin.tmc.ui.IdeUIInvoker;

public class TestrunnerListener implements BackgroundTaskListener {
    private TestrunnerTask task;
    private IdeUIInvoker uiInvoker;

    public TestrunnerListener(TestrunnerTask task, IdeUIInvoker uiInvoker) {
        this.task = task;
        this.uiInvoker = uiInvoker;
    }

    @Override
    public void onBegin() {
        // TODO: Popout "Running tests"
        System.out.println("onBegin");
    }

    @Override
    public void onSuccess() {
        TestRunResult result = task.get();

        uiInvoker.invokeTestResultWindow(result.getTestCaseResults());
    }

    @Override
    public void onFailure() {
        System.out.println("onFailure");
        // TODO: Kill "Running tests"
        // TODO: Popout error

    }

}