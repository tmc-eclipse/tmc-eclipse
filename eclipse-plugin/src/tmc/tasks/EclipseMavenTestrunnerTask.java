package tmc.tasks;

import java.io.File;
import java.util.List;

import org.apache.maven.execution.MavenExecutionRequest;
import org.apache.maven.execution.MavenExecutionResult;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.embedder.IMaven;

import fi.helsinki.cs.plugin.tmc.async.tasks.MavenTestrunnerTask;
import fi.helsinki.cs.plugin.tmc.domain.Project;
import fi.helsinki.cs.plugin.tmc.ui.IdeUIInvoker;

public class EclipseMavenTestrunnerTask extends MavenTestrunnerTask {

    public EclipseMavenTestrunnerTask(Project project, IdeUIInvoker invoker) {
        super(project, invoker);
    }

    @Override
    public int runMaven(List<String> goals, Project project) {
        try {
            IMaven maven = MavenPlugin.getMaven();
            MavenExecutionRequest executionRequest = maven.createExecutionRequest(new NullProgressMonitor())
                    .setPom(new File(project.getRootPath() + "/pom.xml")).setGoals(goals);
            MavenExecutionResult executionResult = maven.execute(executionRequest, new NullProgressMonitor());

            System.out.println(executionResult.toString());

            if (executionResult.hasExceptions()) {
                return 1;
            }

            return 0;
        } catch (CoreException e) {
            return 1;
        }
    }
}
