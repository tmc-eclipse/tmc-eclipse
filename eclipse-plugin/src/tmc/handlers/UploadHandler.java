package tmc.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import tmc.tasks.TaskStarter;
import tmc.ui.EclipseIdeUIInvoker;

public class UploadHandler extends AbstractHandler {

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        Shell shell = HandlerUtil.getActiveWorkbenchWindowChecked(event).getShell();
        TaskStarter.startExerciseUploadTask(new EclipseIdeUIInvoker(shell));
        return null;
    }
}
