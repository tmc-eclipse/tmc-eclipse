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
        IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);

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
