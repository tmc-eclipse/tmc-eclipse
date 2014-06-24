package tmc.eclipse.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import tmc.eclipse.ui.ExerciseSelectorDialog;
import fi.helsinki.cs.tmc.core.Core;
import fi.helsinki.cs.tmc.core.domain.Course;
import fi.helsinki.cs.tmc.core.ui.UserVisibleException;

/**
 * UI handler for exercise download selector
 * 
 */

public class ExerciseSelectorHandler extends AbstractHandler {

    public Object execute(ExecutionEvent event) throws ExecutionException {
        IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);

        try {
            Course currentCourse = Core.getCourseDAO().getCurrentCourse(Core.getSettings());
            Core.getUpdater().updateExercises(currentCourse);
        } catch (UserVisibleException uve) {
            Core.getErrorHandler().raise(uve.getLocalizedMessage());
            return null;
        }
        if (!Core.getCourseDAO().getCurrentCourse(Core.getSettings()).getDownloadableExercises().isEmpty()) {
            ExerciseSelectorDialog dialog = new ExerciseSelectorDialog(window.getShell(), SWT.SHEET);
            dialog.setShowCompleted(false);
            dialog.open();
        } else {
            Core.getErrorHandler().raise("No exercises to be downloaded/updated");
        }

        return null;
    }

}
