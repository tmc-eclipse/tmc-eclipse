package fi.helsinki.cs.plugin.tmc.async.tasks;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fi.helsinki.cs.plugin.tmc.async.BackgroundTask;
import fi.helsinki.cs.plugin.tmc.async.TaskFeedback;
import fi.helsinki.cs.plugin.tmc.domain.Project;
import fi.helsinki.cs.plugin.tmc.domain.TestRunResult;
import fi.helsinki.cs.plugin.tmc.utils.TestResultParser;

public abstract class MavenTestrunnerTask implements BackgroundTask, TestrunnerTask {

    private Project project;
    private TestRunResult results;

    private boolean isRunning;

    public MavenTestrunnerTask(Project project) {
        this.project = project;
        this.isRunning = true;
    }

    public TestRunResult get() {
        return this.results;
    }

    @Override
    public int start(TaskFeedback progress) {
        List<String> goals = new ArrayList<String>();
        goals.add("test-compile");

        if (runMaven(goals, project) != 0 || !isRunning) {
            return BackgroundTask.RETURN_FAILURE;
        }

        File resultFile = new File(project.getRootPath() + "/target/test_output.txt");

        goals.clear();
        goals.add("fi.helsinki.cs.tmc:tmc-maven-plugin:1.6:test");
        if (runMaven(goals, project) != 0 || !isRunning) {
            return BackgroundTask.RETURN_FAILURE;
        }

        try {
            this.results = new TestResultParser().parseTestResults(resultFile);
            resultFile.delete();
        } catch (IOException e) {
            return BackgroundTask.RETURN_FAILURE;
        }

        return BackgroundTask.RETURN_SUCCESS;
    }

    public abstract int runMaven(List<String> goals, Project project);

    @Override
    public void stop() {
        this.isRunning = false;
    }

    @Override
    public String getDescription() {
        return "Running Maven tests";
    }

}
