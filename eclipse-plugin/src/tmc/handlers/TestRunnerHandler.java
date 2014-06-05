package tmc.handlers;

import java.io.File;
import java.util.List;

import org.apache.maven.execution.MavenExecutionRequest;
import org.apache.maven.execution.MavenExecutionResult;
import org.eclipse.ant.core.AntRunner;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.embedder.IMaven;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import tmc.ui.EclipseIdeUIInvoker;
import fi.helsinki.cs.plugin.tmc.Core;
import fi.helsinki.cs.plugin.tmc.async.tasks.AntTestrunnerTask;
import fi.helsinki.cs.plugin.tmc.async.tasks.MavenTestrunnerTask;
import fi.helsinki.cs.plugin.tmc.async.tasks.TestrunnerTask;
import fi.helsinki.cs.plugin.tmc.async.tasks.listeners.TestrunnerListener;
import fi.helsinki.cs.plugin.tmc.domain.Project;

public class TestRunnerHandler extends AbstractHandler {

    private Shell shell;

    public Object execute(ExecutionEvent event) throws ExecutionException {

        shell = HandlerUtil.getActiveWorkbenchWindowChecked(event).getShell();
        try {
            PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                    .showView("fi.helsinki.cs.plugins.eclipse.views.tmcTestUi");

        } catch (PartInitException e) {

        }

        String projectRoot = getProjectRootPath();
        if (projectRoot == null) {
            Core.getErrorHandler().raise("Unable to run tests: No file open in workspace.");
            return null;
        }

        Project project = Core.getProjectDAO().getProjectByFile(projectRoot);

        if (project == null) {
            Core.getErrorHandler().raise("Unable to run tests: Selected project is not a TMC project.");
            return null;
        }

        switch (project.getProjectType()) {
        case JAVA_ANT:
            runTestsForAntProject();
            break;
        case JAVA_MAVEN:
            runTestsforMavenProject(project);
            break;
        case MAKEFILE:
            break;
        default:
            break;
        }
        return null;
    }

    private void runTestsforMavenProject(Project project) {
        if (!saveOpenFiles()) {
            return;
        }

        TestrunnerTask testrun = new MavenTestrunnerTask(project) {

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
        };

        TestrunnerListener listener = new TestrunnerListener(testrun, new EclipseIdeUIInvoker(shell));
        Core.getTaskRunner().runTask(testrun, listener);
    }

    private boolean saveOpenFiles() {
        return PlatformUI.getWorkbench().saveAllEditors(true);
    }

    private void runTestsForAntProject() {
        if (!saveOpenFiles()) {
            return;
        }

        String projectRoot = getProjectRootPath();
        String javaExecutable = System.getProperty("java.home") + "/bin/java";

        try {
            antBuild(projectRoot);
        } catch (Exception e) {
            Core.getErrorHandler().raise("Unable to run tests: Error when building project.");
            e.printStackTrace();
            return;

            // TODO: Handle build failure
        }

        TestrunnerTask testrun = new AntTestrunnerTask(projectRoot, projectRoot + "/test", javaExecutable, null);
        TestrunnerListener listener = new TestrunnerListener(testrun, new EclipseIdeUIInvoker(shell));
        Core.getTaskRunner().runTask(testrun, listener);
    }

    private String getProjectRootPath() {
        try {
            IEditorPart activeEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                    .getActiveEditor();
            IFileEditorInput input = (IFileEditorInput) activeEditor.getEditorInput();
            String projectRoot = input.getFile().getProject().getRawLocation().makeAbsolute().toString();
            return projectRoot;
        } catch (NullPointerException e) {
            return null;
        }

    }

    private void antBuild(String root) throws CoreException {
        IProgressMonitor monitor = new NullProgressMonitor();
        AntRunner runner = new AntRunner();
        runner.setBuildFileLocation(root + "/build.xml");
        runner.setArguments(new String[] {"compile-test"});
        runner.run(monitor);
    }
}
