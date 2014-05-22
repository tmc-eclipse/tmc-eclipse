package tmc.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.part.ViewPart;

import tmc.ui.TestRunnerDialog;

public class TestRunnerHandler extends AbstractHandler {

    public Object execute(ExecutionEvent event) throws ExecutionException {
        TestRunnerDialog dialog = new TestRunnerDialog();

        try {
            PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                    .showView("fi.helsinki.cs.plugins.eclipse.views.tmcTestUi");

        } catch (PartInitException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }
}
