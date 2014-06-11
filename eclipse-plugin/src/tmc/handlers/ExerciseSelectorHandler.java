package tmc.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import tmc.ui.ExerciseSelectorDialog;
import fi.helsinki.cs.plugin.tmc.Core;
import fi.helsinki.cs.plugin.tmc.domain.Course;
import fi.helsinki.cs.plugin.tmc.ui.UserVisibleException;

public class ExerciseSelectorHandler extends AbstractHandler {

    public Object execute(ExecutionEvent event) throws ExecutionException {
        try {
            updateExercises();
        } catch (UserVisibleException uve) {
            Core.getErrorHandler().handleException(uve);
            return null;
        }

        IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);

        ExerciseSelectorDialog dialog = new ExerciseSelectorDialog(window.getShell(), SWT.SHEET);
        dialog.open();

        return null;
    }

    private void updateExercises() {
        Course currentCourse = Core.getCourseDAO().getCurrentCourse();
        Core.getUpdater().updateExercises(currentCourse);
    }

}
