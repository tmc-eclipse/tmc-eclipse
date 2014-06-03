package tmc.listeners;

import fi.helsinki.cs.plugin.tmc.async.BackgroundTaskListener;
import fi.helsinki.cs.plugin.tmc.async.tasks.TestrunnerTask;
import fi.helsinki.cs.plugin.tmc.domain.TestCaseResult;
import fi.helsinki.cs.plugin.tmc.domain.TestRunResult;

public class TestrunnerListener implements BackgroundTaskListener {
    private TestrunnerTask task;

    public TestrunnerListener(TestrunnerTask task) {
        this.task = task;
    }

    @Override
    public void onBegin() {
        // TODO: Popout "Running tests"
        System.out.println("onBegin");
    }

    @Override
    public void onSuccess() {
        System.out.println("onSuccess");
        // TODO: Kill "Running tests"

        TestRunResult result = task.get();

        System.out.println("---");
        for (TestCaseResult tcr : result.getTestCaseResults()) {
            System.out.println(tcr.getName() + " - " + tcr.isSuccessful());
        }
        // TODO: Popout "test run result"
    }

    @Override
    public void onFailure() {
        System.out.println("onFailure");
        // TODO: Kill "Running tests"
        // TODO: Popout error

    }

}