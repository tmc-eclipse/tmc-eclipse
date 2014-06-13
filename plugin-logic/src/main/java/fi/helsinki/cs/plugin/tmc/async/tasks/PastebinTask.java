package fi.helsinki.cs.plugin.tmc.async.tasks;

import fi.helsinki.cs.plugin.tmc.async.BackgroundTask;
import fi.helsinki.cs.plugin.tmc.async.TaskFeedback;
import fi.helsinki.cs.plugin.tmc.services.ProjectDAO;
import fi.helsinki.cs.plugin.tmc.services.ProjectUploader;
import fi.helsinki.cs.plugin.tmc.ui.IdeUIInvoker;

public class PastebinTask implements BackgroundTask {

    private ProjectUploader uploader;
    private String path;
    private String pasteMessage;

    private boolean isRunning;
    private TaskFeedback progress;
    private String description = "Creating a pastebin";

    private ProjectDAO projectDAO;
    private IdeUIInvoker invoker;

    public PastebinTask(ProjectUploader uploader, String path, String pasteMessage, ProjectDAO projectDAO,
            IdeUIInvoker invoker) {
        this.uploader = uploader;
        this.path = path;
        this.pasteMessage = pasteMessage;

        this.projectDAO = projectDAO;
        this.invoker = invoker;
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
            uploader.setProject(projectDAO.getProjectByFile(path));
            uploader.setAsPaste(pasteMessage);
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
            invoker.raiseVisibleException("An error occurred while uploading exercises:\n" + ex.getMessage());
            return BackgroundTask.RETURN_FAILURE;
        }

        return BackgroundTask.RETURN_SUCCESS;

    }

    public String getPasteUrl() {
        return uploader.getResponse().pasteUrl.toString();
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