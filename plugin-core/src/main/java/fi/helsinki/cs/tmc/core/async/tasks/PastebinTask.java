package fi.helsinki.cs.tmc.core.async.tasks;

import fi.helsinki.cs.tmc.core.async.BackgroundTask;
import fi.helsinki.cs.tmc.core.async.TaskFeedback;
import fi.helsinki.cs.tmc.core.services.ProjectDAO;
import fi.helsinki.cs.tmc.core.services.ProjectUploader;
import fi.helsinki.cs.tmc.core.ui.IdeUIInvoker;

/**
 * Background task for sending projects to the TestMyCode pastebin. It is
 * closely related to the uploading task as the only real difference is single
 * parameter in the HTTP request (potentially merge to single task? Constructor
 * parameter list would be nasty though).
 */
public class PastebinTask extends BackgroundTask {

    private ProjectUploader uploader;
    private String path;
    private String pasteMessage;

    private ProjectDAO projectDAO;
    private IdeUIInvoker invoker;

    public PastebinTask(ProjectUploader uploader, String path, String pasteMessage, ProjectDAO projectDAO,
            IdeUIInvoker invoker) {
        super("Creating a pastebin");
        this.uploader = uploader;
        this.path = path;
        this.pasteMessage = pasteMessage;

        this.projectDAO = projectDAO;
        this.invoker = invoker;
    }

    @Override
    public int start(TaskFeedback progress) {
        progress.startProgress(this.getDescription(), 2);

        try {
            uploader.setProject(projectDAO.getProjectByFile(path));
            uploader.setAsPaste(pasteMessage);
            uploader.zipProjects();

            if (shouldStop(progress)) {
                return BackgroundTask.RETURN_INTERRUPTED;
            }

            progress.incrementProgress(1);

            uploader.handleSubmissionResponse();
            if (shouldStop(progress)) {
                return BackgroundTask.RETURN_INTERRUPTED;
            }

            progress.incrementProgress(1);

        } catch (Exception ex) {
            invoker.raiseVisibleException("An error occurred while uploading exercise to pastebin:\n" + ex.getMessage());
            return BackgroundTask.RETURN_FAILURE;
        }

        return BackgroundTask.RETURN_SUCCESS;

    }

    public String getPasteUrl() {
        return uploader.getResponse().pasteUrl.toString();
    }
}