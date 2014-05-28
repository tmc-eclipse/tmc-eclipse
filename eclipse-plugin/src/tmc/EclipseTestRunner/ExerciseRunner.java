package tmc.EclipseTestRunner;

import java.util.concurrent.Callable;

import fi.helsinki.cs.plugin.tmc.domain.Project;

public interface ExerciseRunner {
    public abstract Callable<Integer> getCompilingTask(Project project);

    public abstract Callable<TestRunResult> getTestRunningTask(Project project);
}