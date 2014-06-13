package fi.helsinki.cs.plugin.tmc.async.tasks;

import java.io.IOException;
import java.util.List;

import fi.helsinki.cs.plugin.tmc.async.SimpleBackgroundTask;
import fi.helsinki.cs.plugin.tmc.domain.Exercise;
import fi.helsinki.cs.plugin.tmc.domain.Project;
import fi.helsinki.cs.plugin.tmc.domain.ZippedProject;
import fi.helsinki.cs.plugin.tmc.io.FileIO;
import fi.helsinki.cs.plugin.tmc.io.FileUtil;
import fi.helsinki.cs.plugin.tmc.io.zipper.Unzipper;
import fi.helsinki.cs.plugin.tmc.io.zipper.unzippingdecider.UnzippingDeciderFactory;
import fi.helsinki.cs.plugin.tmc.services.ProjectDAO;
import fi.helsinki.cs.plugin.tmc.services.ProjectDownloader;
import fi.helsinki.cs.plugin.tmc.services.Settings;
import fi.helsinki.cs.plugin.tmc.ui.IdeUIInvoker;

/**
 * This class downloads and unzips projects and opens them in ide. It is run as
 * asynchronous task to prevent ide from freezing while this is being done.
 * 
 */
public class DownloaderTask extends SimpleBackgroundTask<Exercise> {

    private final ProjectDAO projectDao;
    private final Settings settings;
    private final ProjectDownloader downloader;
    private final ProjectOpener opener;
    private final IdeUIInvoker invoker;

    /**
     * 
     * @param downloader
     *            Object that handles the actual download
     * @param opener
     *            Object that handles the project opening in the ide. Requires
     *            ide-specific implementation
     * @param exercises
     *            List of exercises to be downloaded. Exercise-objects contain
     *            necessary urls etc for download
     * @param projectDao
     *            dao that handles project storage.
     * @param settings
     *            Settings-object. Required for settings (duh)
     * @param invoker
     *            ide ui invoker. Required for ability to show error messages.
     *            Requires ide-specific implementation
     */
    public DownloaderTask(ProjectDownloader downloader, ProjectOpener opener, List<Exercise> exercises,
            ProjectDAO projectDao, Settings settings, IdeUIInvoker invoker) {
        super("Downloading exercises", exercises);

        this.settings = settings;
        this.downloader = downloader;
        this.opener = opener;
        this.projectDao = projectDao;
        this.invoker = invoker;
    }

    /**
     * Handles single exercise downloading, unzipping and opening. This method
     * is called by the SimpleBackgroundTask super class
     */
    @Override
    public void run(Exercise exercise) {
        try {
            Project project = projectDao.getProjectByExercise(exercise);

            ZippedProject zip = downloader.downloadExercise(exercise);
            Unzipper unzipper = new Unzipper(zip, UnzippingDeciderFactory.createUnzippingDecider(project));
            FileIO folder = new FileIO(FileUtil.append(settings.getExerciseFilePath(), settings.getCurrentCourseName()));
            List<String> fileList = unzipper.unzipTo(folder);

            exercise.setDownloaded(true);

            if (project == null) {
                project = new Project(exercise, fileList);
            }
            projectDao.addProject(project);

            opener.open(exercise);
        } catch (IOException exception) {
            invoker.raiseVisibleException("An error occurred while unzipping the exercises");
        }
    }
}