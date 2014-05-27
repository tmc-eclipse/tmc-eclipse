package fi.helsinki.cs.plugin.tmc.services;

import java.io.IOException;

import fi.helsinki.cs.plugin.tmc.Core;
import fi.helsinki.cs.plugin.tmc.domain.Project;
import fi.helsinki.cs.plugin.tmc.io.FileIO;
import fi.helsinki.cs.plugin.tmc.io.zipper.RecursiveZipper;
import fi.helsinki.cs.plugin.tmc.services.http.ServerManager;

public class ProjectUploader {

    private ServerManager server;

    public ProjectUploader(ServerManager server) {
        this.server = server;
    }

    public void uploadProject(String projectPath) throws IOException {

        Project project = Core.getProjectDAO().getProjectByFile(projectPath);
        RecursiveZipper zipper = new RecursiveZipper(new FileIO(project.getRootPath()), project.getZippingDecider());

        byte[] data = zipper.zipProjectSources();

        server.uploadFile(project.getExercise(), data);
    }
}
