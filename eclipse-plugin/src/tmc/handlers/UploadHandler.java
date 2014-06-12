package tmc.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import fi.helsinki.cs.plugin.tmc.Core;
import tmc.activator.CoreInitializer;
import tmc.tasks.TaskStarter;
import tmc.ui.EclipseIdeUIInvoker;
import tmc.util.WorkbenchHelper;

public class UploadHandler extends AbstractHandler {
	WorkbenchHelper helper;

	public UploadHandler() {
		this.helper = CoreInitializer.getDefault().getWorkbenchHelper();
		this.helper.initialize();

	}

	@Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        if (helper.saveOpenFiles()) {
            Shell shell = HandlerUtil.getActiveWorkbenchWindowChecked(event).getShell();
            if(helper.getActiveProject().getExercise().hasDeadlinePassed()){
        		Core.getErrorHandler().
        	}
            TaskStarter.startExerciseUploadTask(new EclipseIdeUIInvoker(shell));
        }
        return null;
    }
}
