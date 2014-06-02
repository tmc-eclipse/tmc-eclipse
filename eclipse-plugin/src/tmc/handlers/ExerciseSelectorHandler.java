package tmc.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import tmc.ui.SuccesfulSubmitDialog;

public class ExerciseSelectorHandler extends AbstractHandler {

    public Object execute(ExecutionEvent event) throws ExecutionException {
        IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);

        SuccesfulSubmitDialog dialog = new SuccesfulSubmitDialog(window.getShell(), SWT.SHEET);
        // ExerciseSelectorDialog dialog = new
        // ExerciseSelectorDialog(window.getShell(), SWT.SHEET);
        dialog.open();

        return null;
    }
}
