package tmc.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import tmc.activator.CoreInitializer;
import tmc.ui.EclipseIdeUIInvoker;
import tmc.util.WorkbenchHelper;
import fi.helsinki.cs.plugin.tmc.domain.Exercise;
import fi.helsinki.cs.plugin.tmc.domain.Project;
import fi.helsinki.cs.plugin.tmc.ui.IdeUIInvoker;

/**
 * UI handler for code review request
 * 
 */
public class CodeReviewRequestHandler extends AbstractHandler {
    WorkbenchHelper helper;

    public CodeReviewRequestHandler() {
        this.helper = CoreInitializer.getDefault().getWorkbenchHelper();
        this.helper.initialize();
    }

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        if (helper.saveOpenFiles()) {
            Shell shell = HandlerUtil.getActiveWorkbenchWindowChecked(event).getShell();
            IdeUIInvoker invoker = new EclipseIdeUIInvoker(shell);

            Project p = CoreInitializer.getDefault().getWorkbenchHelper().getActiveProject();
            if (p == null) {
                invoker.raiseVisibleException("Unable to request code review:\n" + "No TMC project selected.");
                return null;
            }

            Exercise e = p.getExercise();
            if (e == null) {
                invoker.raiseVisibleException("Unable to request code review:\n"
                        + "Selected project is not associated with an exercise.\n" + "\n"
                        + "Please tell your instructor about this error message.");
                return null;
            }
            String exerciseName = e.getName();

            invoker.invokeRequestCodeReviewWindow(exerciseName);
        }
        return null;
    }
}
