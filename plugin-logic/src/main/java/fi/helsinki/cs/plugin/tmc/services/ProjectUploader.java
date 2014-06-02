package fi.helsinki.cs.plugin.tmc.services;

import java.io.IOException;

import fi.helsinki.cs.plugin.tmc.Core;
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

    public ProjectUploader(ServerManager server) {
        this.server = server;
    }

    public void uploadProject(String projectPath) throws IOException {

        Project project = Core.getProjectDAO().getProjectByFile(projectPath);
        if (project == null) {
            throw new RuntimeException("Not a TMC project!");
        }
        RecursiveZipper zipper = new RecursiveZipper(new FileIO(project.getRootPath()), project.getZippingDecider());

        byte[] data = zipper.zipProjectSources();

        SubmissionResponse response = server.uploadFile(project.getExercise(), data);
        System.out.println("submissionUrl: " + response.submissionUrl);
        System.out.println("pasteUrl: " + response.pasteUrl);

        SubmissionResult result = getSubmissionResult(response);

        System.out.println("All test cases failed: " + result.allTestCasesFailed());
        System.out.println("Feedback answer url: " + result.getFeedbackAnswerUrl());
        System.out.println("Solution url: " + result.getSolutionUrl());
        System.out.println("Feedback questions: " + result.getFeedbackQuestions());
        System.out.println("Missing review points: " + result.getMissingReviewPoints());
        System.out.println("Points: " + result.getPoints());
        System.out.println("Status: " + result.getStatus());
        System.out.println("Test cases: " + result.getTestCases());

    }

    private SubmissionResult getSubmissionResult(SubmissionResponse response) {

        SubmissionResult result = server.getSubmissionResult(response.submissionUrl);

        // basically we try to stop the thread being completely unresponsive
        // while sleeping
        // (cancellation for example)
        while (result.getStatus() == SubmissionResult.Status.PROCESSING) {
            for (int i = 0; i < LOOP_COUNT; ++i) {
                try {
                    Thread.sleep(SLEEP_DURATION);
                } catch (InterruptedException e) {
                }
            }

            result = server.getSubmissionResult(response.submissionUrl);
        }

        return result;
    }
}
