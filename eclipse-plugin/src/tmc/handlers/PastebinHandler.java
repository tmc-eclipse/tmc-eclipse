package tmc.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import fi.helsinki.cs.plugin.tmc.domain.Exercise;
import fi.helsinki.cs.plugin.tmc.domain.Project;
import fi.helsinki.cs.plugin.tmc.ui.IdeUIInvoker;
import tmc.activator.CoreInitializer;
import tmc.tasks.TaskStarter;
import tmc.ui.EclipseIdeUIInvoker;
import tmc.util.WorkbenchHelper;

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
            Project p = CoreInitializer.getDefault().getWorkbenchHelper().getActiveProject();
            Exercise e = p.getExercise();
            String exerciseName = e.getName();
            new EclipseIdeUIInvoker(shell).invokeSendToPastebinWindow(exerciseName);
        }
        return null;
    }
}