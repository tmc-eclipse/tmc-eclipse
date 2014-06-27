package tmc.eclipse.tasks;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import tmc.eclipse.activator.CoreInitializer;
import tmc.eclipse.ui.CustomNotification;
import tmc.eclipse.ui.ExerciseSelectorDialog;
import tmc.eclipse.ui.LoginDialog;
import tmc.eclipse.ui.Notifier;
import tmc.eclipse.ui.SettingsDialog;
import fi.helsinki.cs.tmc.core.Core;
import fi.helsinki.cs.tmc.core.domain.Course;
import fi.helsinki.cs.tmc.core.ui.UserVisibleException;

public class CheckForNewOrUpdatedExercisesOnBackgroundTask implements Runnable {
    private ExerciseSelectorDialog dialog;
    private CustomNotification notification;

    public void run() {
        if ((notification != null && notification.isAlive())) {
            return; 
        }
        
        try {
            Display.getDefault().asyncExec(new Runnable() {
                public void run() { 
                    final Shell shell = CoreInitializer.getDefault().getWorkbenchHelper().getUsableShell();

                    updateCoursesForUser(shell);
                    updateExercisesForCurrentCourse();

                    if (Core.getSettings().isLoggedIn()
                            && !Core.getCourseDAO().getCurrentCourse(Core.getSettings()).getDownloadableExercises()
                                    .isEmpty()) {
                        if (dialog != null) {
                            try {
                                dialog.close();
                                dialog = null;
                            } catch (SWTException e) {
                                // Closed by user, just continue as usual.
                            }
                        }

                        notification = Notifier.getInstance().createNotification("New exercises are available. ",
                                "Click here to download. ", new Listener() {
                                    @Override
                                    public void handleEvent(Event arg0) {
                                        Display.getDefault().asyncExec(new Runnable() {
                                            @Override
                                            public void run() {
                                                dialog = new ExerciseSelectorDialog(shell, SWT.SHEET);
                                                dialog.open();
                                            }
                                        });
                                    }
                                });
                    }
                }
            });
        } catch (Exception e) {
            // Ignore ALL errors, no future scheduled tasks will be ran
            // after any exceptions are thrown.
        }
    }

    private void updateCoursesForUser(Shell shell) {
        try {
            Core.getUpdater().updateCourses();
        } catch (UserVisibleException uve) {
            if (!Core.getSettings().getServerBaseUrl().isEmpty()) {
                LoginDialog ld = new LoginDialog(shell, SWT.SHEET);
                ld.open();
            } else {
                SettingsDialog sd = new SettingsDialog(shell, SWT.SHEET);
                sd.open();
            }
        }
    }

    private void updateExercisesForCurrentCourse() {
        try {
            Course currentCourse = Core.getCourseDAO().getCurrentCourse(Core.getSettings());
            Core.getUpdater().updateExercises(currentCourse);
        } catch (UserVisibleException uve) {

        }
    }
}
