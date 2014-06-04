package fi.helsinki.cs.plugin.tmc.async.tasks;

import fi.helsinki.cs.plugin.tmc.Core;
import fi.helsinki.cs.plugin.tmc.async.BackgroundTask;
import fi.helsinki.cs.plugin.tmc.async.TaskFeedback;
import fi.helsinki.cs.plugin.tmc.domain.Project;
import fi.helsinki.cs.plugin.tmc.domain.SubmissionResult;
import fi.helsinki.cs.plugin.tmc.services.ProjectUploader;

public class UploaderTask implements BackgroundTask {

    private ProjectUploader uploader;
    private boolean isRunning;
    private TaskFeedback progress;
    private String description = "Uploading exercises";
    private String path;

    public interface StopStatus {
        boolean mustStop();
    }

    public UploaderTask(ProjectUploader uploader, String path) {
        this.uploader = uploader;
        isRunning = true;
        this.path = path;
    }

    private boolean isRunning() {
        if (!isRunning) {
            return false;
        }

        isRunning = !progress.isCancelRequested();
        return isRunning;
    }

    @Override
    public int start(TaskFeedback p) {
        progress = p;
        progress.startProgress(description, 3);

        return run();

    }

    private int run() {

        try {
            uploader.zipProjects(path);
            if (!isRunning()) {
                return BackgroundTask.RETURN_FAILURE;
            }
            progress.incrementProgress(1);

            uploader.handleSumissionResponse();
            if (!isRunning()) {
                return BackgroundTask.RETURN_FAILURE;
            }
            progress.incrementProgress(1);

            uploader.handleSubmissionResult(new StopStatus() {

                @Override
                public boolean mustStop() {
                    return !isRunning();
                }
            });

            if (getResult() == null) {
                return BackgroundTask.RETURN_FAILURE;
            }

            progress.incrementProgress(1);

        } catch (Exception ex) {
            Core.getErrorHandler().raise("An error occurred while uploading exercises: " + ex.getMessage());
            ex.printStackTrace();
        }

        return BackgroundTask.RETURN_SUCCESS;

    }

    public SubmissionResult getResult() {
        return uploader.getResult();
    }

    public Project getProject() {
        return uploader.getProject();
    }

    @Override
    public void stop() {
        isRunning = false;
    }

    @Override
    public String getDescription() {
        return description;
    }

}