package fi.helsinki.cs.tmc.core.async.tasks;

import fi.helsinki.cs.tmc.core.async.BackgroundTask;
import fi.helsinki.cs.tmc.core.async.StopStatus;
import fi.helsinki.cs.tmc.core.async.TaskFeedback;
import fi.helsinki.cs.tmc.core.domain.Project;
import fi.helsinki.cs.tmc.core.domain.SubmissionResult;
import fi.helsinki.cs.tmc.core.services.ProjectDAO;
import fi.helsinki.cs.tmc.core.services.ProjectUploader;
import fi.helsinki.cs.tmc.core.ui.IdeUIInvoker;

/**
 * Background task that handles file upload to server. Used when submitting
 * files to server for grading
 * 
 * 
 */
public class UploaderTask extends BackgroundTask {

    private ProjectUploader uploader;
    private String path;

    private ProjectDAO projectDAO;
    private IdeUIInvoker invoker;

    public UploaderTask(ProjectUploader uploader, String path, ProjectDAO projectDAO, IdeUIInvoker invoker) {
        super("Uploading exercises");
        this.uploader = uploader;
        this.path = path;
        this.projectDAO = projectDAO;
        this.invoker = invoker;
    }

    @Override
    public int start(TaskFeedback progress) {
        progress.startProgress(this.getDescription(), 3);

        return run(progress);

    }

    private int run(final TaskFeedback progress) {

        try {
            uploader.setProject(projectDAO.getProjectByFile(path));
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

            uploader.handleSubmissionResult(new StopStatus() {

                @Override
                public boolean mustStop() {
                    return shouldStop(progress);
                }
            });

            if (getResult() == null) {
                return BackgroundTask.RETURN_FAILURE;
            }

            progress.incrementProgress(1);

        } catch (Exception ex) {
            invoker.raiseVisibleException("An error occurred while uploading exercises:\n" + ex.getMessage());
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
}