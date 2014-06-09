package fi.helsinki.cs.plugin.tmc.async.tasks;

import fi.helsinki.cs.plugin.tmc.async.BackgroundTask;
import fi.helsinki.cs.plugin.tmc.domain.TestRunResult;

public interface TestrunnerTask extends BackgroundTask {
    TestRunResult get();
}
