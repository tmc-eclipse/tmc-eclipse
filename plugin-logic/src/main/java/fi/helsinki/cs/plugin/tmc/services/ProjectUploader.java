package fi.helsinki.cs.plugin.tmc.services;

import java.io.IOException;

import fi.helsinki.cs.plugin.tmc.Core;
import fi.helsinki.cs.plugin.tmc.domain.Project;
import fi.helsinki.cs.plugin.tmc.io.FileIO;
import fi.helsinki.cs.plugin.tmc.io.zipper.RecursiveZipper;
import fi.helsinki.cs.plugin.tmc.services.http.ServerManager;
import fi.helsinki.cs.plugin.tmc.services.http.SubmissionResponse;

public class ProjectUploader {

    private ServerManager server;

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
        System.out.println("submissionUrl" + response.submissionUrl);
        System.out.println("pasteUrl" + response.pasteUrl);

    }
}
