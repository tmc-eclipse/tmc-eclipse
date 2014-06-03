package tmc.handlers;

import org.eclipse.ant.core.AntRunner;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import tmc.listeners.TestrunnerListener;
import fi.helsinki.cs.plugin.tmc.Core;
import fi.helsinki.cs.plugin.tmc.async.tasks.TestrunnerTask;
import fi.helsinki.cs.plugin.tmc.domain.Project;
import fi.helsinki.cs.plugin.tmc.domain.TestRunResult;

public class TestRunnerHandler extends AbstractHandler {

    public Object execute(ExecutionEvent event) throws ExecutionException {

        try {
            PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                    .showView("fi.helsinki.cs.plugins.eclipse.views.tmcTestUi");

        } catch (PartInitException e) {

        }

        String projectRoot = getProjectRootPath() + "/";
        Project project = Core.getProjectDAO().getProjectByFile(projectRoot);

        TestRunResult results = null;
        switch (project.getProjectType()) {
        case JAVA_ANT:
            runTestsForAntProject();
            break;
        case JAVA_MAVEN:
            results = null;
            break;
        case MAKEFILE:
            results = null;
            break;
        default:
            results = null;
            break;
        }
        return null;
    }

    private void runTestsForAntProject() {
        String projectRoot = getProjectRootPath();
        String javaExecutable = System.getProperty("java.home") + "/bin/java";

        try {
            antBuild(projectRoot);
        } catch (Exception e) {
            System.out.println("Failed building");
            e.printStackTrace();
            return;

            // TODO: Handle build failure
        }

        TestrunnerTask testrun = new TestrunnerTask(projectRoot, projectRoot + "/test", javaExecutable, null);
        TestrunnerListener listener = new TestrunnerListener(testrun);
        Core.getTaskRunner().runTask(testrun, listener);
    }

    private String getProjectRootPath() {
        IEditorPart activeEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                .getActiveEditor();
        IFileEditorInput input = (IFileEditorInput) activeEditor.getEditorInput();
        String projectRoot = input.getFile().getProject().getRawLocation().makeAbsolute().toString();
        return projectRoot;
    }

    private void antBuild(String root) throws Exception {
        IProgressMonitor monitor = new NullProgressMonitor();
        AntRunner runner = new AntRunner();
        runner.setBuildFileLocation(root + "/build.xml");
        runner.setArguments(new String[] {"compile-test"});
        runner.run(monitor);
    }
}
