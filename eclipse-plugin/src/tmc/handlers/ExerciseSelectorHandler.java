package tmc.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import tmc.ui.ExerciseSelectorDialog;
import fi.helsinki.cs.plugin.tmc.Core;

public class ExerciseSelectorHandler extends AbstractHandler {

    public Object execute(ExecutionEvent event) throws ExecutionException {
        // try {
        // updateExercises();
        // } catch (UserVisibleException uve) {
        // Core.getErrorHandler().handleException(uve);
        // return null;
        // }

        IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);

        if (!Core.getCourseDAO().getCurrentCourse(Core.getSettings()).getDownloadableExercises().isEmpty()) {
            ExerciseSelectorDialog esd = new ExerciseSelectorDialog(window.getShell(), SWT.SHEET);
            esd.open();
        } else {
            Core.getErrorHandler().raise("No exercises to be downloaded/updated");
        }

        return null;
    }

    // private void updateExercises() {
    // Course currentCourse =
    // Core.getCourseDAO().getCurrentCourse(Core.getSettings());
    // Core.getUpdater().updateExercises(currentCourse);
    // }

}
