package fi.helsinki.cs.plugin.tmc.async.tasks;

import fi.helsinki.cs.plugin.tmc.Core;
import fi.helsinki.cs.plugin.tmc.async.BackgroundTask;
import fi.helsinki.cs.plugin.tmc.async.TaskFeedback;
import fi.helsinki.cs.plugin.tmc.services.ProjectUploader;

public class CodeReviewRequestTask implements BackgroundTask {

    private ProjectUploader uploader;
    private String path;
    private String requestMessage;

    private boolean isRunning;
    private TaskFeedback progress;
    private String description = "Creating code review request";

    public CodeReviewRequestTask(ProjectUploader uploader, String path, String requestMessage) {
        this.uploader = uploader;
        this.path = path;
        this.requestMessage = requestMessage;

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
        progress.startProgress(description, 2);

        return run();

    }

    private int run() {

        try {
            uploader.setProject(Core.getProjectDAO().getProjectByFile(path));
            uploader.setAsRequest(requestMessage);
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

        } catch (Exception ex) {
            Core.getErrorHandler().raise("An error occurred while requesting code review:\n" + ex.getMessage());
            return BackgroundTask.RETURN_FAILURE;
        }

        return BackgroundTask.RETURN_SUCCESS;

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