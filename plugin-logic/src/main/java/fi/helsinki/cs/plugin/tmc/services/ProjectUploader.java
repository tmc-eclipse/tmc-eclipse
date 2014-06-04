package fi.helsinki.cs.plugin.tmc.services;

import java.io.IOException;

import fi.helsinki.cs.plugin.tmc.Core;
import fi.helsinki.cs.plugin.tmc.async.tasks.UploaderTask.StopStatus;
import fi.helsinki.cs.plugin.tmc.domain.Project;
import fi.helsinki.cs.plugin.tmc.domain.SubmissionResult;
import fi.helsinki.cs.plugin.tmc.io.FileIO;
import fi.helsinki.cs.plugin.tmc.io.zipper.RecursiveZipper;
import fi.helsinki.cs.plugin.tmc.services.http.ServerManager;
import fi.helsinki.cs.plugin.tmc.services.http.SubmissionResponse;

public class ProjectUploader {

    private ServerManager server;

    private static int SLEEP_DURATION = 40;
    private static int LOOP_COUNT = 2000 / SLEEP_DURATION;

    private Project project;
    private byte[] data;
    private SubmissionResponse response;
    private SubmissionResult result;

    public ProjectUploader(ServerManager server) {
        this.server = server;
        data = null;
        project = null;
    }

    public void zipProjects(String projectPath) throws IOException {
        project = Core.getProjectDAO().getProjectByFile(projectPath);
        if (project == null) {

            throw new RuntimeException("Not a TMC project! ");
        }
        RecursiveZipper zipper = new RecursiveZipper(new FileIO(project.getRootPath()), project.getZippingDecider());

        data = zipper.zipProjectSources();
    }

    public void handleSumissionResponse() throws IOException {

        if (project == null || data == null) {
            throw new RuntimeException("Internal error: Invalid project or zip data");
        }

        response = server.uploadFile(project.getExercise(), data);
    }

    public void handleSubmissionResult(StopStatus stopStatus) {

        result = server.getSubmissionResult(response.submissionUrl);

        // basically we try to stop the thread being completely unresponsive
        // while sleeping
        // (cancellation for example)

        while (result.getStatus() == SubmissionResult.Status.PROCESSING) {
            for (int i = 0; i < LOOP_COUNT; ++i) {

                if (stopStatus.mustStop()) {
                    return;
                }

                try {
                    Thread.sleep(SLEEP_DURATION);
                } catch (InterruptedException e) {
                }
            }

            result = server.getSubmissionResult(response.submissionUrl);
        }

        if (stopStatus.mustStop()) {
            return;
        }
    }

    public SubmissionResult getResult() {
        return result;
    }

    public Project getProject() {
        return project;
    }
}
