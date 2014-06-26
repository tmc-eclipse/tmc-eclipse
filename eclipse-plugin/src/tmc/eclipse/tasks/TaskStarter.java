package tmc.eclipse.tasks;

import java.util.ArrayList;
import java.util.List;

import tmc.eclipse.activator.CoreInitializer;
import tmc.eclipse.openers.GenericProjectOpener;
import tmc.eclipse.ui.EclipseIdeUIInvoker;
import tmc.eclipse.util.EclipseProjectIconHandler;
import tmc.eclipse.util.WorkbenchHelper;
import fi.helsinki.cs.tmc.core.Core;
import fi.helsinki.cs.tmc.core.async.listeners.CodeReviewRequestListener;
import fi.helsinki.cs.tmc.core.async.listeners.FetchCodeReviewsTaskListener;
import fi.helsinki.cs.tmc.core.async.listeners.PastebinTaskListener;
import fi.helsinki.cs.tmc.core.async.listeners.TestrunnerListener;
import fi.helsinki.cs.tmc.core.async.listeners.UploadTaskListener;
import fi.helsinki.cs.tmc.core.async.tasks.CodeReviewRequestTask;
import fi.helsinki.cs.tmc.core.async.tasks.DownloaderTask;
import fi.helsinki.cs.tmc.core.async.tasks.FeedbackSubmissionTask;
import fi.helsinki.cs.tmc.core.async.tasks.FetchCodeReviewsTask;
import fi.helsinki.cs.tmc.core.async.tasks.MarkReviewAsReadTask;
import fi.helsinki.cs.tmc.core.async.tasks.OpenAllDownloadedExercisesTask;
import fi.helsinki.cs.tmc.core.async.tasks.PastebinTask;
import fi.helsinki.cs.tmc.core.async.tasks.TestrunnerTask;
import fi.helsinki.cs.tmc.core.async.tasks.UploaderTask;
import fi.helsinki.cs.tmc.core.domain.Course;
import fi.helsinki.cs.tmc.core.domain.Exercise;
import fi.helsinki.cs.tmc.core.domain.FeedbackAnswer;
import fi.helsinki.cs.tmc.core.domain.Project;
import fi.helsinki.cs.tmc.core.domain.Review;
import fi.helsinki.cs.tmc.core.services.FeedbackAnswerSubmitter;
import fi.helsinki.cs.tmc.core.services.ProjectDownloader;
import fi.helsinki.cs.tmc.core.services.ProjectOpener;
import fi.helsinki.cs.tmc.core.services.ProjectUploader;
import fi.helsinki.cs.tmc.core.services.ReviewDAO;
import fi.helsinki.cs.tmc.core.services.http.ServerManager;
import fi.helsinki.cs.tmc.core.ui.IdeUIInvoker;

/**
 * 
 * Class that handles task creation and starting
 * 
 */
public final class TaskStarter {

    public static void startExerciseDownloadTask(List<Exercise> exercises, EclipseIdeUIInvoker invoker) {
        ProjectDownloader downloader = new ProjectDownloader(Core.getServerManager());
        Core.getTaskRunner().runTask(
                new DownloaderTask(downloader, new GenericProjectOpener(), exercises, Core.getProjectDAO(), Core
                        .getSettings(), invoker, Core.getIOFactory()));
    }

    public static void startFeedbackSubmissionTask(List<FeedbackAnswer> answers, String feedbackUrl,
            EclipseIdeUIInvoker invoker) {
        FeedbackAnswerSubmitter submitter = new FeedbackAnswerSubmitter(Core.getServerManager());
        Core.getTaskRunner().runTask(new FeedbackSubmissionTask(submitter, answers, feedbackUrl, invoker));
    }

    public static void startExerciseUploadTask(EclipseIdeUIInvoker invoker) {
        ProjectUploader uploader = new ProjectUploader(Core.getServerManager());
        WorkbenchHelper helper = CoreInitializer.getDefault().getWorkbenchHelper();
        EclipseProjectIconHandler handler = new EclipseProjectIconHandler();
        helper.initialize();

        Project project = helper.getActiveProject();

        UploaderTask task = new UploaderTask(uploader, project.getRootPath() + "/", Core.getProjectDAO(), invoker);
        Core.getTaskRunner().runTask(task, new UploadTaskListener(task, invoker, handler));
    }

    public static void startPastebinTask(EclipseIdeUIInvoker invoker, String pasteMessage) {
        ProjectUploader uploader = new ProjectUploader(Core.getServerManager());
        WorkbenchHelper helper = CoreInitializer.getDefault().getWorkbenchHelper();
        helper.initialize();

        Project project = helper.getActiveProject();

        PastebinTask task = new PastebinTask(uploader, project.getRootPath() + "/", pasteMessage, Core.getProjectDAO(),
                invoker);
        Core.getTaskRunner().runTask(task, new PastebinTaskListener(task, invoker));
    }

    public static void startCodeReviewRequestTask(EclipseIdeUIInvoker invoker, String requestMessage) {
        ProjectUploader uploader = new ProjectUploader(Core.getServerManager());
        WorkbenchHelper helper = CoreInitializer.getDefault().getWorkbenchHelper();
        helper.initialize();

        Project project = helper.getActiveProject();

        CodeReviewRequestTask task = new CodeReviewRequestTask(uploader, project.getRootPath() + "/", requestMessage,
                Core.getProjectDAO(), invoker);
        Core.getTaskRunner().runTask(task, new CodeReviewRequestListener(task, invoker));
    }

    public static void startMavenTestRunnerTask(Project project, EclipseIdeUIInvoker invoker) {
        TestrunnerTask testrun = new EclipseMavenTestrunnerTask(project, invoker);
        TestrunnerListener listener = new TestrunnerListener(testrun, invoker);
        Core.getTaskRunner().runTask(testrun, listener);
    }

    public static void startAntTestRunnerTask(String projectRoot, String javaExecutable, EclipseIdeUIInvoker invoker) {

        TestrunnerTask testrun = new EclipseAntTestrunnerTask(projectRoot, projectRoot + "/test", javaExecutable, null,
                Core.getSettings(), invoker);

        TestrunnerListener listener = new TestrunnerListener(testrun, invoker);
        Core.getTaskRunner().runTask(testrun, listener);
    }

    public static void startFetchCodeReviewsTask(IdeUIInvoker invoker, boolean showMessages) {
        Course course = Core.getCourseDAO().getCurrentCourse(Core.getSettings());
        if (course == null) {
            if (showMessages) {
                invoker.raiseVisibleException("Unable to check for new code reviews:\n" + "No course set.\n"
                        + "Please set a course in the TMC | Settings menu.");
            }
            return;
        }

        ServerManager server = Core.getServerManager();
        ReviewDAO reviewDAO = Core.getReviewDAO();
        FetchCodeReviewsTask task = new FetchCodeReviewsTask(course, server, reviewDAO);
        FetchCodeReviewsTaskListener listener = new FetchCodeReviewsTaskListener(task, invoker, reviewDAO, showMessages);

        Core.getTaskRunner().runTask(task, listener);
    }

    public static void startMarkCodereviewAsReadTask(Review review) {
        ServerManager server = Core.getServerManager();
        MarkReviewAsReadTask task = new MarkReviewAsReadTask(server, review);
        Core.getTaskRunner().runTask(task);
    }

    public static void startOpenAllDownloadedExercisesTask() {
        String description = "Opening previously downloaded exercises";
        List<Exercise> exercises = new ArrayList<Exercise>();
        for (Project p : Core.getProjectDAO().getProjects()) {
            exercises.add(p.getExercise());
        }

        ProjectOpener opener = new GenericProjectOpener();
        OpenAllDownloadedExercisesTask task = new OpenAllDownloadedExercisesTask(description, exercises, opener,
                Core.getIOFactory());
        Core.getTaskRunner().runTask(task);
    }
}
