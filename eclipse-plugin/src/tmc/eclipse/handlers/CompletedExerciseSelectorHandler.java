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

/**
 * 
 * UI handler for completed exercise download selector
 * 
 */
public class CompletedExerciseSelectorHandler extends AbstractHandler {

    public Object execute(ExecutionEvent event) throws ExecutionException {
        IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);

        Course currentCourse = Core.getCourseDAO().getCurrentCourse(Core.getSettings());
        Core.getUpdater().updateExercises(currentCourse);

        if (!Core.getCourseDAO().getCurrentCourse(Core.getSettings()).getCompletedDownloadableExercises().isEmpty()) {
            ExerciseSelectorDialog dialog = new ExerciseSelectorDialog(window.getShell(), SWT.SHEET);
            dialog.setShowCompleted(true);
            dialog.open();
        } else {
            Core.getErrorHandler().raise("No exercises to be downloaded/updated");
        }

        return null;
    }

}
