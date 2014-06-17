package tmc.tasks;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import tmc.ui.EclipseIdeUIInvoker;

public class CheckForCodeReviewsOnBackgroundTask implements Runnable {
        public void run() {
            try {
                Display.getDefault().asyncExec(new Runnable() {
                    public void run() {
                        /*
                         * Default display seems to always have AT LEAST three
                         * shells:
                         * 
                         * Shell #0 = "PartRenderingEngine's limbo". Intended
                         * for elements that should not be part of current view.
                         * 
                         * Shell #1 = "Resource - Eclipse Platform". Changes (or
                         * atleast the name does) based on selected MAIN UI
                         * element. Opening a dialog doesn't change this shell.
                         * 
                         * Shell #2 = "Quick Access". Also always present but
                         * probably not the one we want to semantically use.
                         * 
                         * Out of these three, Shell #1 seems like the best bet,
                         * therefore we should use it.
                         * 
                         * As a fall back, if the shell doesn't for some reason
                         * exist, we'll use Shell #0.
                         * 
                         * If THAT doesn't exist, we try to get the active
                         * shell. This is very unlikely to exist in such a case
                         * (f.ex. it doesn't exist if the focus is on some other
                         * window than the Eclipse IDE).
                         * 
                         * If everything fails, we just silently fail and hope
                         * for better luck on the next run.
                         */
                        Shell shell = null;
                        if (Display.getDefault().getShells().length > 1) {
                            shell = Display.getDefault().getShells()[1];
                            if (shell == null && Display.getDefault().getShells().length > 0) {
                                shell = Display.getDefault().getShells()[0];
                            }
                            if (shell == null) {
                                shell = Display.getDefault().getActiveShell();
                            }
                        }

                        if (shell == null) {
                            // Shell was null, just die quietly on this run
                            // because throwing an exception would prevent
                            // future scheduled executions.
                        } else {
                            TaskStarter.startFetchCodeReviewsTask(new EclipseIdeUIInvoker(shell));
                        }
                    }
                });
            } catch (Exception e) {
                // Ignore ALL errors, no future scheduled tasks will be ran
                // after any exceptions are thrown.

                e.printStackTrace();
                // TODO: Better error handling
            }
        }
    }