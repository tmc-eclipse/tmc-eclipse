package tmc.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import fi.helsinki.cs.plugin.tmc.async.tasks.TestrunnerTask;

public class TestRunnerHandler extends AbstractHandler {

    public Object execute(ExecutionEvent event) throws ExecutionException {

        try {
            PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                    .showView("fi.helsinki.cs.plugins.eclipse.views.tmcTestUi");

        } catch (PartInitException e) {

        }

        // rootDir, testDir, resultFile, testClasspath, memorylimit
        new TestrunnerTask(null, null, null, null, null).start(null);

        return null;
    }
}
