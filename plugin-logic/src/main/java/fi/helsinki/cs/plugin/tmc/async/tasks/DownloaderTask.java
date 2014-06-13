package fi.helsinki.cs.plugin.tmc.async.tasks;

import java.io.IOException;
import java.util.List;

import fi.helsinki.cs.plugin.tmc.Core;
import fi.helsinki.cs.plugin.tmc.async.SimpleBackgroundTask;
import fi.helsinki.cs.plugin.tmc.domain.Exercise;
import fi.helsinki.cs.plugin.tmc.domain.Project;
import fi.helsinki.cs.plugin.tmc.domain.ProjectStatus;
import fi.helsinki.cs.plugin.tmc.domain.ZippedProject;
import fi.helsinki.cs.plugin.tmc.io.FileIO;
import fi.helsinki.cs.plugin.tmc.io.FileUtil;
import fi.helsinki.cs.plugin.tmc.io.zipper.Unzipper;
import fi.helsinki.cs.plugin.tmc.io.zipper.unzippingdecider.UnzippingDeciderFactory;
import fi.helsinki.cs.plugin.tmc.services.ProjectDAO;
import fi.helsinki.cs.plugin.tmc.services.ProjectDownloader;
import fi.helsinki.cs.plugin.tmc.services.Settings;

public class DownloaderTask extends SimpleBackgroundTask<Exercise> {

    private ProjectDAO projectDao;
    private Settings settings;
    private ProjectDownloader downloader;
    private ProjectOpener opener;

    public DownloaderTask(ProjectDownloader downloader, ProjectOpener opener, List<Exercise> exercises,
            ProjectDAO projectDao, Settings settings) {
        super("Downloading exercises", exercises);

        this.settings = settings;
        this.downloader = downloader;
        this.opener = opener;
        this.projectDao = projectDao;
    }

    @Override
    public void run(Exercise exercise) {
        try {
            Project project = projectDao.getProjectByExercise(exercise);

            ZippedProject zip = downloader.downloadExercise(exercise);
            Unzipper unzipper = new Unzipper(zip, UnzippingDeciderFactory.createUnzippingDecider(project));
            FileIO folder = new FileIO(FileUtil.append(settings.getExerciseFilePath(), settings.getCurrentCourseName()));
            List<String> fileList = unzipper.unzipTo(folder);

            if (project == null) {
                project = new Project(exercise, fileList);
                projectDao.addProject(project);
            } else {
                project.setProjectFiles(fileList);
            }

            project.setStatus(ProjectStatus.DOWNLOADED);

            opener.open(exercise);
        } catch (IOException exception) {
            Core.getErrorHandler().raise("An error occurred while unzipping the exercises");
        }
    }
}