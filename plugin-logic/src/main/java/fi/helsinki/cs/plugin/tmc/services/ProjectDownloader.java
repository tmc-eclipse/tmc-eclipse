package fi.helsinki.cs.plugin.tmc.services;

import java.util.ArrayList;
import java.util.List;

import fi.helsinki.cs.plugin.tmc.domain.Exercise;
import fi.helsinki.cs.plugin.tmc.domain.ZippedProject;
import fi.helsinki.cs.plugin.tmc.services.http.ServerManager;

/**
 * Class that handles project downloading. Used by the DownloaderTask background
 * task
 * 
 */
public class ProjectDownloader {

    private ServerManager server;

    public ProjectDownloader(ServerManager server) {
        this.server = server;
    }

    public List<ZippedProject> downloadExercises(List<Exercise> exercises) {
        List<ZippedProject> projects = new ArrayList<ZippedProject>();
        for (Exercise exercise : exercises) {
            projects.add(downloadExercise(exercise));
        }
        return projects;
    }

    public ZippedProject downloadExercise(Exercise exercise) {
        String zipUrl = exercise.getDownloadUrl();
        return server.getExerciseZip(zipUrl);
    }

}
