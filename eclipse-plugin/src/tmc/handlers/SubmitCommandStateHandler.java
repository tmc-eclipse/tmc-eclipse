package tmc.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.services.ISourceProviderService;

import tmc.activator.CoreInitializer;
import tmc.util.WorkbenchHelper;

public class SubmitCommandStateHandler extends AbstractHandler {

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        ISourceProviderService sourceProviderService = (ISourceProviderService) HandlerUtil.getActiveWorkbenchWindow(
                event).getService(ISourceProviderService.class);
        SubmitCommandState commandStateService = (SubmitCommandState) sourceProviderService
                .getSourceProvider(SubmitCommandState.MY_STATE);

        WorkbenchHelper helper = CoreInitializer.getDefault().getWorkbenchHelper();
        helper.initialize();
        if (helper.getActiveProject() != null) {
            commandStateService.setState(!helper.getActiveProject().getExercise().hasDeadlinePassed());
        }
        return null;
    }

}