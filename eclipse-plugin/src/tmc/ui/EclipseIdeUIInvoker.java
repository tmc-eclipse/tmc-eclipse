package tmc.ui;

import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import tmc.tasks.TaskStarter;
import fi.helsinki.cs.plugin.tmc.Core;
import fi.helsinki.cs.plugin.tmc.domain.SubmissionResult;
import fi.helsinki.cs.plugin.tmc.domain.TestCaseResult;
import fi.helsinki.cs.plugin.tmc.ui.IdeUIInvoker;

public class EclipseIdeUIInvoker implements IdeUIInvoker {

    private Shell shell;

    public EclipseIdeUIInvoker(Shell shell) {
        this.shell = shell;
    }

    @Override
    public void invokeTestResultWindow(final List<TestCaseResult> results) {
        Display.getDefault().asyncExec(new Runnable() {
            public void run() {
                try {
                    TestRunnerView trv = (TestRunnerView) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
                            .getActivePage().showView("fi.helsinki.cs.plugins.eclipse.views.tmcTestUi");
                    trv.addSubmissionResult(results);

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

                dialog.setPointsAwarded(result.getPoints());
                dialog.setModelSolutionUrl(result.getSolutionUrl());
                dialog.setFeedbackQuestions(result.getFeedbackQuestions());
                dialog.SetFeedbackUrl(result.getFeedbackAnswerUrl());

                dialog.open();

            }
        });
    }

    @Override
    public void invokeSomeTestsFailedWindow(SubmissionResult result, String exerciseName) {
        String messageStr = "Exercise " + exerciseName + " failed.\n" + "Some tests failed on the server.\nSee Below";
        String title = "Some tests failed on server";
        invokeErrorMessageBox(messageStr, title);

    }

    @Override
    public void invokeAllTestsFailedWindow(SubmissionResult result, String exerciseName) {
        String messageStr = "Exercise " + exerciseName + " failed.\n" + "All tests failed on the server.\nSee Below";
        String title = "All tests failed on server";
        invokeErrorMessageBox(messageStr, title);
    }

    private void invokeErrorMessageBox(final String messageStr, final String title) {
        invokeMessageBox(messageStr, title, MessageDialog.ERROR);
    }

    private void invokeSuccessMessageBox(final String messageStr, final String title) {
        invokeMessageBox(messageStr, title, MessageDialog.OK);
    }

    private void invokeMessageBox(final String messageStr, final String title, final int type) {
        Display.getDefault().asyncExec(new Runnable() {
            public void run() {
                MessageDialog dialog = new MessageDialog(shell, title, null, messageStr, type, new String[] {"OK"}, 0);
                dialog.open();

            }
        });
    }

    @Override
    public void invokeSubmitToServerWindow() {
        final EclipseIdeUIInvoker invoker = this;
        Display.getDefault().asyncExec(new Runnable() {
            public void run() {
                ServerSubmitMessagebox dialog = new ServerSubmitMessagebox(shell);
                if (dialog.submitExercises()) {
                    TaskStarter.startExerciseUploadTask(invoker);
                }
            }
        });
    }

    @Override
    public void invokeSendToPastebinWindow(final String exerciseName) {
        Display.getDefault().asyncExec(new Runnable() {
            public void run() {
                SendToPastebinDialog dialog = new SendToPastebinDialog(shell, exerciseName);
                dialog.open();
            }
        });
    }

    @Override
    public void invokePastebinResultDialog(final String pasteUrl) {
        Display.getDefault().asyncExec(new Runnable() {
            public void run() {
                PastebinResultDialog dialog = new PastebinResultDialog(shell, pasteUrl);
                dialog.open();
            }
        });
    }

    @Override
    public void invokeRequestCodeReviewWindow(final String exerciseName) {
        Display.getDefault().asyncExec(new Runnable() {
            public void run() {
                CodeReviewRequestDialog dialog = new CodeReviewRequestDialog(shell, exerciseName);
                dialog.open();
            }
        });
    }

    @Override
    public void invokeCodeReviewRequestSuccefullySentWindow() {
        Display.getDefault().asyncExec(new Runnable() {
            public void run() {
                String msg = "Code submitted for review.\nYou will be notified when an instructor has reviewed your code.";
                String title = "Code review request succesfully sent.";
                invokeSuccessMessageBox(msg, title);
            }
        });
    }

    @Override
    public void raiseVisibleException(String message) {
        Core.getErrorHandler().raise(message);
    }
}
