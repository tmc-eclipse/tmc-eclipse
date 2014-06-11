package fi.helsinki.cs.plugin.tmc.async.tasks;

import fi.helsinki.cs.plugin.tmc.Core;
import fi.helsinki.cs.plugin.tmc.async.BackgroundTask;
import fi.helsinki.cs.plugin.tmc.async.StopStatus;
import fi.helsinki.cs.plugin.tmc.async.TaskFeedback;
import fi.helsinki.cs.plugin.tmc.domain.Project;
import fi.helsinki.cs.plugin.tmc.domain.SubmissionResult;
import fi.helsinki.cs.plugin.tmc.services.ProjectUploader;

public class UploaderTask implements BackgroundTask {

    private ProjectUploader uploader;
    private String path;
    private boolean asPaste;
    private String pasteMessage;

    private boolean isRunning;
    private TaskFeedback progress;
    private String description = "Uploading exercises";

    public UploaderTask(ProjectUploader uploader, String path) {
        this(uploader, path, false, "");
    }

    public UploaderTask(ProjectUploader uploader, String path, boolean asPaste, String pasteMessage) {
        this.uploader = uploader;
        this.path = path;
        this.asPaste = asPaste;
        this.pasteMessage = pasteMessage;

        isRunning = true;
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
            uploader.setProject(Core.getProjectDAO().getProjectByFile(path));

            if (asPaste) {
                uploader.setAsPaste(pasteMessage);
            }

            uploader.zipProjects();

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
            Core.getErrorHandler().raise("An error occurred while uploading exercises:\n" + ex.getMessage());
            return BackgroundTask.RETURN_FAILURE;
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