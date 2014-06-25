package fi.helsinki.cs.tmc.core.async.listeners;

import java.util.List;

import fi.helsinki.cs.tmc.core.async.BackgroundTaskListener;
import fi.helsinki.cs.tmc.core.async.tasks.TestrunnerTask;
import fi.helsinki.cs.tmc.core.domain.TestCaseResult;
import fi.helsinki.cs.tmc.core.domain.TestRunResult;
import fi.helsinki.cs.tmc.core.ui.IdeUIInvoker;

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

    @Override
    public void onInterruption() {
    }

}