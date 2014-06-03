package tmc.ui;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import fi.helsinki.cs.plugin.tmc.domain.SubmissionResult;
import fi.helsinki.cs.plugin.tmc.ui.IdeUIInvoker;

public class EclipseIdeUIInvoker implements IdeUIInvoker {

    private Shell shell;

    public EclipseIdeUIInvoker(Shell shell) {
        this.shell = shell;
    }

    @Override
    public void invokeTestResultWindow(final SubmissionResult result) {
        Display.getDefault().asyncExec(new Runnable() {
            public void run() {
                try {
                    TestRunnerView trv = (TestRunnerView) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
                            .getActivePage().showView("fi.helsinki.cs.plugins.eclipse.views.tmcTestUi");
                    trv.addSubmissionResult(result);

                } catch (PartInitException e) {

                }
            }
        });
    }

    @Override
    public void invokeAllTestsPassedWindow(final SubmissionResult result, final String exerciseName) {

        Display.getDefault().asyncExec(new Runnable() {
            public void run() {
                SuccesfulSubmitDialog dialog = new SuccesfulSubmitDialog(shell, exerciseName);

                int points = 0; // getPoints(result);
                System.out.println(result.getPoints());
                dialog.setPointsAwarded(points);
                dialog.setModelSolutionUrl(result.getSolutionUrl());

                dialog.open();

            }

            private int getPoints(final SubmissionResult result) {
                int points = 0;
                for (String point : result.getPoints()) {
                    points += Integer.parseInt(point);
                }
                return points;
            }
        });
    }

    @Override
    public void invokeSomeTestsFailedWindow(SubmissionResult result, String exerciseName) {
        String messageStr = "Exercise " + exerciseName + " failed.\n" + "Some tests failed on the server.\nSee Below";
        invokeMessageBox(messageStr);

    }

    @Override
    public void invokeAllTestsFailedWindow(SubmissionResult result, String exerciseName) {
        String messageStr = "Exercise " + exerciseName + " failed.\n" + "All tests failed on the server.\nSee Below";
        invokeMessageBox(messageStr);
    }

    private void invokeMessageBox(final String messageStr) {
        Display.getDefault().asyncExec(new Runnable() {
            public void run() {
                MessageDialog dialog = new MessageDialog(shell, "", null, messageStr, MessageDialog.ERROR,
                        new String[] {"OK"}, 0);
                dialog.open();

            }
        });
    }

}
