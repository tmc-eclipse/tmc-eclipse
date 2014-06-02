package tmc.async.listeners;

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

        SubmissionResult result = task.getResult();

        System.out.println("OnSuccess");
        System.out.println("Status: " + result.getStatus());

    }

    @Override
    public void onFailure() {

        System.out.println("OnFailure");

    }

}
