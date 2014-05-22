package fi.helsinki.cs.plugin.tmc.services;

import java.util.ArrayList;
import java.util.List;

import fi.helsinki.cs.plugin.tmc.domain.Exercise;
import fi.helsinki.cs.plugin.tmc.domain.ZippedProject;
import fi.helsinki.cs.plugin.tmc.services.http.HttpTasks;
import fi.helsinki.cs.plugin.tmc.services.http.ServerAccess;

public class ExerciseDownloader {

    private HttpTasks http;

    public ExerciseDownloader() {
        this.http = new ServerAccess().createHttpTasks();
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
        ZippedProject zippedProject = new ZippedProject();
        try {
            zippedProject.setBytes(http.getForBinary(zipUrl).call());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return zippedProject;
    }

}
