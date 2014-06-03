package tmc.async.listeners;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import tmc.ui.TestRunnerView;
import fi.helsinki.cs.plugin.tmc.async.BackgroundTaskListener;
import fi.helsinki.cs.plugin.tmc.async.tasks.UploaderTask;
import fi.helsinki.cs.plugin.tmc.domain.SubmissionResult;

public class UploadTaskListener implements BackgroundTaskListener {

    private UploaderTask task;

    public UploadTaskListener(UploaderTask task) {
        this.task = task;
    }

    @Override
    public void onBegin() {
        System.out.println("OnBegin");

    }

    @Override
    public void onSuccess() {

        final SubmissionResult result = task.getResult();

        Display.getDefault().asyncExec(new Runnable() {
            public void run() {
                try {
                    if (!result.allTestCasesSucceeded()) {
                        TestRunnerView trv = (TestRunnerView) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
                                .getActivePage().showView("fi.helsinki.cs.plugins.eclipse.views.tmcTestUi");
                        trv.addSubmissionResult(result);
                    }

                } catch (PartInitException e) {

                }
            }
        });

    }

    @Override
    public void onFailure() {

        System.out.println("OnFailure");

    }

}
