package tmc.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import tmc.activator.CoreInitializer;
import tmc.handlers.listeners.SelectionListener;
import tmc.tasks.TaskStarter;
import tmc.ui.EclipseIdeUIInvoker;
import tmc.util.WorkbenchHelper;
import fi.helsinki.cs.plugin.tmc.Core;

public class UploadHandler extends AbstractHandler {
    WorkbenchHelper helper;
    SelectionListener listener;

    public UploadHandler() {
        this.helper = CoreInitializer.getDefault().getWorkbenchHelper();
        this.helper.initialize();

    }

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        if (helper.saveOpenFiles()) {
            Shell shell = HandlerUtil.getActiveWorkbenchWindowChecked(event).getShell();

            if (helper.getActiveProject() == null) {
                Core.getErrorHandler().handleManualException("Unable to submit exercise:\nNo valid exercise selected.");
            } else {
                TaskStarter.startExerciseUploadTask(new EclipseIdeUIInvoker(shell));
            }
        }
        return null;
    }
}
