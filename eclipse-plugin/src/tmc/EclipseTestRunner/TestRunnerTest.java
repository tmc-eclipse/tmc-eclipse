package tmc.EclipseTestRunner;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;

import fi.helsinki.cs.plugin.tmc.domain.Project;

public class TestRunnerTest {
    @SuppressWarnings("null")
    public TestRunnerTest(IProgressMonitor pm, IProject p) {
        IProject myProject = p;
        IProgressMonitor myProgressMonitor = pm;
        try {
            myProject.build(IncrementalProjectBuilder.INCREMENTAL_BUILD, myProgressMonitor);
        } catch (CoreException e) {

        }

    }

    public String getProject(Project project) {
        return project.getExercise().getName();
    }

    public static void main(String[] args) {
        IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
        IProject project = workspaceRoot.getProject("eclipse-plugin");
        IProgressMonitor myProgressMonitor = new NullProgressMonitor();
        try {
            project.build(IncrementalProjectBuilder.INCREMENTAL_BUILD, myProgressMonitor);
        } catch (CoreException e) {

        }
    }
}