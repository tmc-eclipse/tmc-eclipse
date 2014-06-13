package fi.helsinki.cs.plugin.tmc.services;

import java.io.IOException;
import java.util.HashMap;

import fi.helsinki.cs.plugin.tmc.async.StopStatus;
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
    private HashMap<String, String> extraParams;
    
    private SubmissionResponse response;
    private SubmissionResult result;
    private Settings settings;

    public ProjectUploader(ServerManager server, Settings settings) {
        this.settings = settings;
        this.server = server;
        data = null;
        project = null;
        this.extraParams = new HashMap<String, String>();
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public void zipProjects() throws IOException {

        if (project == null) {
            throw new RuntimeException("Not a TMC project!");
        }

        RecursiveZipper zipper = new RecursiveZipper(new FileIO(project.getRootPath()), project.getZippingDecider());

        data = zipper.zipProjectSources();
    }

    public void handleSumissionResponse() throws IOException {

        if (project == null || data == null) {
            throw new RuntimeException("Internal error: Invalid project or zip data");
        }

        if (!extraParams.isEmpty()) {
            response = server.uploadFile(project.getExercise(), data, extraParams, settings);
        } else {
            response = server.uploadFile(project.getExercise(), data, settings);
        }
    }

    public void handleSubmissionResult(StopStatus stopStatus) {

        result = server.getSubmissionResult(response.submissionUrl);

        // basically we try to stop the thread being completely unresponsive
        // while sleeping so that cancellation goes through

        while (result.getStatus() == SubmissionResult.Status.PROCESSING) {
            if (!waitForServer(stopStatus)) {
                result = null;
                return;
            }

            result = server.getSubmissionResult(response.submissionUrl);
        }
    }

    private boolean waitForServer(StopStatus stopStatus) {
        for (int i = 0; i < LOOP_COUNT; ++i) {
            if (stopStatus.mustStop()) {
                return false;
            }

            try {
                Thread.sleep(SLEEP_DURATION);
            } catch (InterruptedException e) {
            }
        }
        return true;
    }

    public SubmissionResponse getResponse() {
        return response;
    }

    public SubmissionResult getResult() {
        return result;
    }

    public Project getProject() {
        return project;
    }

    public void setAsPaste(String pasteMessage) {
        extraParams.put("paste", "1");
        if (!pasteMessage.isEmpty()) {
            extraParams.put("message_for_paste", pasteMessage);
        }
    }
    
    public void setAsRequest(String requestMessage) {
        extraParams.put("request_review", "1");
        if (!requestMessage.isEmpty()) {
            extraParams.put("message_for_reviewer", requestMessage);
        }
    }
}
