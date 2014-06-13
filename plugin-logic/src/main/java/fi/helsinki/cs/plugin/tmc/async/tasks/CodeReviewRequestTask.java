package fi.helsinki.cs.plugin.tmc.async.tasks;

import fi.helsinki.cs.plugin.tmc.async.BackgroundTask;
import fi.helsinki.cs.plugin.tmc.async.TaskFeedback;
import fi.helsinki.cs.plugin.tmc.services.ProjectDAO;
import fi.helsinki.cs.plugin.tmc.services.ProjectUploader;
import fi.helsinki.cs.plugin.tmc.ui.IdeUIInvoker;

/**
 * Background task for code review request
 */

public class CodeReviewRequestTask implements BackgroundTask {

    private ProjectUploader uploader;
    private String path;
    private String requestMessage;

    private boolean isRunning;
    private TaskFeedback progress;
    private String description = "Creating code review request";

    private ProjectDAO projectDAO;
    private IdeUIInvoker invoker;

    /**
     * 
     * @param uploader
     *            Object that handles project upload. Since project upload and
     *            code review are so similar (both involve uploading project),
     *            the object is used here as well
     * @param path
     *            Path to project
     * @param requestMessage
     *            User code review message
     * @param projectDAO
     *            Object that contains all the projects
     * @param invoker
     *            Ide specific object that allows us to invoke ide UI (error
     *            messages for example)
     */
    public CodeReviewRequestTask(ProjectUploader uploader, String path, String requestMessage, ProjectDAO projectDAO,
            IdeUIInvoker invoker) {
        this.uploader = uploader;
        this.path = path;
        this.requestMessage = requestMessage;
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
            invoker.raiseVisibleException("An error occurred while requesting code review:\n" + ex.getMessage());
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