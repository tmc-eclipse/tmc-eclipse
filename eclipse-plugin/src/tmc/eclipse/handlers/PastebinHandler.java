package tmc.eclipse.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import tmc.eclipse.activator.CoreInitializer;
import tmc.eclipse.ui.EclipseIdeUIInvoker;
import tmc.eclipse.util.WorkbenchHelper;
import fi.helsinki.cs.tmc.core.domain.Exercise;
import fi.helsinki.cs.tmc.core.domain.Project;
import fi.helsinki.cs.tmc.core.ui.IdeUIInvoker;

/**
 * UI handler for paste bin sending window
 * 
 */
public class PastebinHandler extends AbstractHandler {
    WorkbenchHelper helper;

    public PastebinHandler() {
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
                invoker.raiseVisibleException("Unable to create pastebin:\n" + "No TMC project is selected.");
                return null;
            }

            Exercise e = p.getExercise();
            if (e == null) {
                invoker.raiseVisibleException("Unable to create pastebin:\n"
                        + "Selected project is not associated with an exercise.\n" + "\n"
                        + "Please tell your instructor about this error message.");
                return null;
            }

            String exerciseName = e.getName();
            invoker.invokeSendToPastebinWindow(exerciseName);
        }
        return null;
    }
}