package tmc.eclipse.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import tmc.eclipse.activator.CoreInitializer;
import tmc.eclipse.tasks.TaskStarter;
import tmc.eclipse.ui.EclipseIdeUIInvoker;
import tmc.eclipse.util.WorkbenchHelper;
import fi.helsinki.cs.tmc.core.Core;
import fi.helsinki.cs.tmc.core.domain.Project;

public class TestRunnerHandler extends AbstractHandler {

    private Shell shell;

    private WorkbenchHelper helper;

    public TestRunnerHandler() {
        this.helper = CoreInitializer.getDefault().getWorkbenchHelper();
        helper.initialize();
    }

    public Object execute(ExecutionEvent event) throws ExecutionException {
        helper.updateActiveView();

        shell = HandlerUtil.getActiveWorkbenchWindowChecked(event).getShell();
        try {
            PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                    .showView("fi.helsinki.cs.plugins.eclipse.views.tmcTestUi");

        } catch (PartInitException e) {

        }

        if (helper.getActiveProject() == null) {
            return null;
        }

        String projectRoot = helper.getActiveProject().getRootPath();
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
        if (!helper.saveOpenFiles()) {
            return;
        }

        TaskStarter.startMavenTestRunnerTask(project, new EclipseIdeUIInvoker(shell));
    }

    private void runTestsForAntProject() {
        if (!helper.saveOpenFiles()) {
            return;
        }

        String projectRoot = helper.getActiveProject().getRootPath();
        String javaExecutable = JavaRuntime.getDefaultVMInstall().getInstallLocation() + "/bin/java";
        TaskStarter.startAntTestRunnerTask(projectRoot, javaExecutable, new EclipseIdeUIInvoker(shell));
    }
}
