package tmc.eclipse.tasks;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import tmc.eclipse.activator.CoreInitializer;
import tmc.eclipse.ui.EclipseIdeUIInvoker;

public class CheckForCodeReviewsOnBackgroundTask implements Runnable {
    public void run() {
        try {
            Display.getDefault().asyncExec(new Runnable() {
                public void run() {
                    Shell shell = CoreInitializer.getDefault().getWorkbenchHelper().getUsableShell();
                    TaskStarter.startFetchCodeReviewsTask(new EclipseIdeUIInvoker(shell), false);
                }
            });
        } catch (Exception e) {
            // Ignore ALL errors, no future scheduled tasks will be ran
            // after any exceptions are thrown.
        }
    }
}