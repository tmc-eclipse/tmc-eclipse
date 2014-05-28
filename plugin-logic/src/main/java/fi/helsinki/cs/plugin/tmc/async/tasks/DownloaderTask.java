package fi.helsinki.cs.plugin.tmc.async.tasks;

import java.io.IOException;
import java.util.List;

import fi.helsinki.cs.plugin.tmc.Core;
import fi.helsinki.cs.plugin.tmc.async.SimpleBackgroundTask;
import fi.helsinki.cs.plugin.tmc.domain.Exercise;
import fi.helsinki.cs.plugin.tmc.domain.Project;
import fi.helsinki.cs.plugin.tmc.domain.ZippedProject;
import fi.helsinki.cs.plugin.tmc.io.FileIO;
import fi.helsinki.cs.plugin.tmc.io.FileUtil;
import fi.helsinki.cs.plugin.tmc.io.zipper.Unzipper;
import fi.helsinki.cs.plugin.tmc.io.zipper.unzippingdecider.UnzippingDeciderFactory;
import fi.helsinki.cs.plugin.tmc.services.ProjectDownloader;
import fi.helsinki.cs.plugin.tmc.services.Settings;

public class DownloaderTask extends SimpleBackgroundTask<Exercise> {

    private Settings settings;
    private ProjectDownloader downloader;
    private ProjectOpener opener;

    public DownloaderTask(ProjectDownloader downloader, ProjectOpener opener, List<Exercise> exercises) {
        super("Downloading exercises", exercises);

        this.settings = Core.getSettings();
        this.downloader = downloader;
        this.opener = opener;
    }

    @Override
    public void run(Exercise exercise) {
        try {
            ZippedProject zip = downloader.downloadExercise(exercise);

            Unzipper unzipper = new Unzipper(zip, UnzippingDeciderFactory.noSrcOverwrite());
            FileIO folder = new FileIO(FileUtil.append(settings.getExerciseFilePath(), settings.getCurrentCourseName()));
            List<String> fileList = unzipper.unzipTo(folder);

            Core.getProjectDAO().addProject(new Project(exercise, fileList));
            opener.open(exercise);
        } catch (IOException exception) {
            Core.getErrorHandler().raise("An error occurred while unzipping the exercises");
        }
    }

}