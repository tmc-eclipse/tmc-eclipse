package fi.helsinki.cs.plugin.tmc.async.tasks;

import java.io.IOException;
import java.util.List;

import fi.helsinki.cs.plugin.tmc.Core;
import fi.helsinki.cs.plugin.tmc.async.BackgroundTask;
import fi.helsinki.cs.plugin.tmc.async.TaskFeedback;
import fi.helsinki.cs.plugin.tmc.domain.Exercise;
import fi.helsinki.cs.plugin.tmc.domain.Project;
import fi.helsinki.cs.plugin.tmc.domain.ZippedProject;
import fi.helsinki.cs.plugin.tmc.io.FileIO;
import fi.helsinki.cs.plugin.tmc.io.zipper.Unzipper;
import fi.helsinki.cs.plugin.tmc.services.ProjectDownloader;
import fi.helsinki.cs.plugin.tmc.ui.UserVisibleException;

public class DownloaderTask implements BackgroundTask {

    private boolean isRunning;

    private ProjectDownloader downloader;
    private List<Exercise> exerciseList;

    public DownloaderTask(ProjectDownloader downloader, List<Exercise> exerciseList) {
        this.isRunning = true;
        this.downloader = downloader;
        this.exerciseList = exerciseList;
    }

    @Override
    public void start(TaskFeedback progress) {
        progress.startProgress("Downloading exercises...", exerciseList.size() * 2);

        for (Exercise e : exerciseList) {
            if (progress.isCanceled()) {
                this.stop();
            }
            if (!isRunning) {
                break;
            }

            ZippedProject zip = downloader.downloadExercise(e);
            progress.incrementProgress(1);

            try {
                Unzipper unzipper = new Unzipper(zip);
                FileIO fileIO = new FileIO(Core.getSettings().getExerciseFilePath() + "/"
                        + Core.getSettings().getCurrentCourseName());
                List<String> fileList = unzipper.unzipTo(fileIO);
                Project project = new Project(e, fileList);
                Core.getProjectDAO().addProject(project);

                progress.incrementProgress(1);
            } catch (IOException exception) {
                Core.getErrorHandler().handleException(
                        new UserVisibleException("An error occurred while unzipping the exercises"));
            }
        }

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
