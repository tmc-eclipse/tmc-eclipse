package tmc.tasks;

import java.util.List;

import tmc.activator.CoreInitializer;
import tmc.services.GenericProjectOpener;
import tmc.ui.EclipseIdeUIInvoker;
import tmc.util.WorkbenchHelper;
import fi.helsinki.cs.plugin.tmc.Core;
import fi.helsinki.cs.plugin.tmc.async.tasks.CodeReviewRequestTask;
import fi.helsinki.cs.plugin.tmc.async.tasks.DownloaderTask;
import fi.helsinki.cs.plugin.tmc.async.tasks.FeedbackSubmissionTask;
import fi.helsinki.cs.plugin.tmc.async.tasks.FetchCodeReviewsTask;
import fi.helsinki.cs.plugin.tmc.async.tasks.MarkReviewAsReadTask;
import fi.helsinki.cs.plugin.tmc.async.tasks.PastebinTask;
import fi.helsinki.cs.plugin.tmc.async.tasks.TestrunnerTask;
import fi.helsinki.cs.plugin.tmc.async.tasks.UploaderTask;
import fi.helsinki.cs.plugin.tmc.async.tasks.listeners.CodeReviewRequestListener;
import fi.helsinki.cs.plugin.tmc.async.tasks.listeners.FetchCodeReviewsTaskListener;
import fi.helsinki.cs.plugin.tmc.async.tasks.listeners.PastebinTaskListener;
import fi.helsinki.cs.plugin.tmc.async.tasks.listeners.TestrunnerListener;
import fi.helsinki.cs.plugin.tmc.async.tasks.listeners.UploadTaskListener;
import fi.helsinki.cs.plugin.tmc.domain.Course;
import fi.helsinki.cs.plugin.tmc.domain.Exercise;
import fi.helsinki.cs.plugin.tmc.domain.FeedbackAnswer;
import fi.helsinki.cs.plugin.tmc.domain.Project;
import fi.helsinki.cs.plugin.tmc.domain.Review;
import fi.helsinki.cs.plugin.tmc.services.FeedbackAnswerSubmitter;
import fi.helsinki.cs.plugin.tmc.services.ProjectDownloader;
import fi.helsinki.cs.plugin.tmc.services.ProjectUploader;
import fi.helsinki.cs.plugin.tmc.services.ReviewDAO;
import fi.helsinki.cs.plugin.tmc.services.http.ServerManager;
import fi.helsinki.cs.plugin.tmc.ui.IdeUIInvoker;

public final class TaskStarter {

    public static void startExerciseDownloadTask(List<Exercise> exercises, EclipseIdeUIInvoker invoker) {
        ProjectDownloader downloader = new ProjectDownloader(Core.getServerManager());
        Core.getTaskRunner().runTask(
                new DownloaderTask(downloader, new GenericProjectOpener(), exercises, Core.getProjectDAO(), Core
                        .getSettings(), invoker));
    }

    public static void startFeedbackSubmissionTask(List<FeedbackAnswer> answers, String feedbackUrl,
            EclipseIdeUIInvoker invoker) {
        FeedbackAnswerSubmitter submitter = new FeedbackAnswerSubmitter(Core.getServerManager());
        Core.getTaskRunner().runTask(new FeedbackSubmissionTask(submitter, answers, feedbackUrl, invoker));
    }

    public static void startExerciseUploadTask(EclipseIdeUIInvoker invoker) {
        ProjectUploader uploader = new ProjectUploader(Core.getServerManager());
        WorkbenchHelper helper = CoreInitializer.getDefault().getWorkbenchHelper();
        helper.initialize();

        Project project = helper.getActiveProject();

        UploaderTask task = new UploaderTask(uploader, project.getRootPath() + "/", Core.getProjectDAO(), invoker);
        Core.getTaskRunner().runTask(task, new UploadTaskListener(task, invoker));
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
}
