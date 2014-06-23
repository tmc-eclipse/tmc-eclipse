package fi.helsinki.cs.plugin.tmc.async.tasks;

import fi.helsinki.cs.plugin.tmc.async.BackgroundTask;
import fi.helsinki.cs.plugin.tmc.domain.TestRunResult;

public abstract class TestrunnerTask extends BackgroundTask {

    public TestrunnerTask(String description) {
        super(description);
    }

    public abstract TestRunResult get();
}