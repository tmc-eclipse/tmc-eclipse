package fi.helsinki.cs.plugin.tmc.tasks;

import java.io.IOException;
import java.util.List;

import fi.helsinki.cs.plugin.tmc.Core;
import fi.helsinki.cs.plugin.tmc.domain.Exercise;
import fi.helsinki.cs.plugin.tmc.domain.Project;
import fi.helsinki.cs.plugin.tmc.domain.ZippedProject;
import fi.helsinki.cs.plugin.tmc.io.FileIO;
import fi.helsinki.cs.plugin.tmc.io.zipper.Unzipper;
import fi.helsinki.cs.plugin.tmc.services.ProjectDownloader;
import fi.helsinki.cs.plugin.tmc.services.http.ServerManager;
import fi.helsinki.cs.plugin.tmc.ui.UserVisibleException;

public class DownloaderTask implements BackgroundTask {

    private boolean isRunning;
    private List<Exercise> exerciseList;

    public DownloaderTask(List<Exercise> exerciseList) {
        this.isRunning = true;
        this.exerciseList = exerciseList;
    }

    @Override
    public Object start(TaskFeedback feedback) {
        feedback.resetProgress("Downloading exercises...", exerciseList.size() * 2);

        // TODO: Dependency injection?
        ProjectDownloader downloader = new ProjectDownloader(new ServerManager());

        for (Exercise e : exerciseList) {
            if (feedback.isCanceled()) {
                this.stop();
            }
            if (!isRunning) {
                break;
            }

            ZippedProject zip = downloader.downloadExercise(e);
            feedback.updateProgress(1);

            try {
                Unzipper unzipper = new Unzipper(zip);
                FileIO fileIO = new FileIO(Core.getSettings().getExerciseFilePath() + "/"
                        + Core.getSettings().getCurrentCourseName());
                List<String> fileList = unzipper.unzipTo(fileIO);
                Project project = new Project(e, fileList);
                Core.getProjectDAO().addProject(project);

                feedback.updateProgress(1);
            } catch (IOException exception) {
                Core.getErrorHandler().handleException(
                        new UserVisibleException("An error occurred while unzipping the exercises"));
            }
        }

        return null;
    }

    @Override
    public void stop() {
        this.isRunning = false;
    }

    @Override
    public String getName() {
        return "Downloading exercises";
    }

}
