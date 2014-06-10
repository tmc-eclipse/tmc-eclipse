package fi.helsinki.cs.plugin.tmc.async.tasks.listeners;

import java.util.List;

import fi.helsinki.cs.plugin.tmc.async.BackgroundTaskListener;
import fi.helsinki.cs.plugin.tmc.async.tasks.TestrunnerTask;
import fi.helsinki.cs.plugin.tmc.domain.TestCaseResult;
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
    }

    @Override
    public void onSuccess() {
        TestRunResult result = task.get();

        uiInvoker.invokeTestResultWindow(result.getTestCaseResults());

        if (allPassed(result.getTestCaseResults())) {
            uiInvoker.invokeSubmitToServerWindow();
        }
    }

    private boolean allPassed(List<TestCaseResult> testCaseResults) {
        for (TestCaseResult r : testCaseResults) {
            if (!r.isSuccessful()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onFailure() {
        // TODO: Kill "Running tests"
        // TODO: Popout error

    }

}