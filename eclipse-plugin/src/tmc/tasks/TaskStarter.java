package tmc.tasks;

import java.util.List;

import tmc.activator.CoreInitializer;
import tmc.services.GenericProjectOpener;
import tmc.ui.EclipseIdeUIInvoker;
import tmc.util.WorkbenchHelper;
import fi.helsinki.cs.plugin.tmc.Core;
import fi.helsinki.cs.plugin.tmc.async.tasks.DownloaderTask;
import fi.helsinki.cs.plugin.tmc.async.tasks.FeedbackSubmissionTask;
import fi.helsinki.cs.plugin.tmc.async.tasks.PastebinTask;
import fi.helsinki.cs.plugin.tmc.async.tasks.UploaderTask;
import fi.helsinki.cs.plugin.tmc.async.tasks.listeners.PastebinTaskListener;
import fi.helsinki.cs.plugin.tmc.async.tasks.listeners.UploadTaskListener;
import fi.helsinki.cs.plugin.tmc.domain.Exercise;
import fi.helsinki.cs.plugin.tmc.domain.FeedbackAnswer;
import fi.helsinki.cs.plugin.tmc.domain.Project;
import fi.helsinki.cs.plugin.tmc.services.FeedbackAnswerSubmitter;
import fi.helsinki.cs.plugin.tmc.services.ProjectDownloader;
import fi.helsinki.cs.plugin.tmc.services.ProjectUploader;

public final class TaskStarter {

    public static void startExerciseDownloadTask(List<Exercise> exercises) {
        ProjectDownloader downloader = new ProjectDownloader(Core.getServerManager());
        Core.getTaskRunner().runTask(
                new DownloaderTask(downloader, new GenericProjectOpener(), exercises, Core.getProjectDAO(), Core
                        .getSettings()));
    }

    public static void startFeedbackSubmissionTask(List<FeedbackAnswer> answers, String feedbackUrl) {
        FeedbackAnswerSubmitter submitter = new FeedbackAnswerSubmitter(Core.getServerManager());
        Core.getTaskRunner().runTask(new FeedbackSubmissionTask(submitter, answers, feedbackUrl));
    }
    
    public static void startExerciseUploadTask(EclipseIdeUIInvoker invoker) {
        ProjectUploader uploader = new ProjectUploader(Core.getServerManager());
        WorkbenchHelper helper = CoreInitializer.getDefault().getWorkbenchHelper();
        helper.initialize();

        Project project = helper.getActiveProject();

        UploaderTask task = new UploaderTask(uploader, project.getRootPath() + "/");
        Core.getTaskRunner().runTask(task, new UploadTaskListener(task, invoker));
    }

    public static void startPastebinTask(EclipseIdeUIInvoker invoker, String pasteMessage) {
        ProjectUploader uploader = new ProjectUploader(Core.getServerManager());
        WorkbenchHelper helper = CoreInitializer.getDefault().getWorkbenchHelper();
        helper.initialize();

        Project project = helper.getActiveProject();

        PastebinTask task = new PastebinTask(uploader, project.getRootPath() + "/", pasteMessage);
        Core.getTaskRunner().runTask(task, new PastebinTaskListener(task, invoker));
    }
}
