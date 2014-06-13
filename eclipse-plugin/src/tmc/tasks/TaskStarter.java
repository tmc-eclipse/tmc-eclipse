package tmc.tasks;

import java.io.File;
import java.util.List;

import org.apache.maven.execution.MavenExecutionRequest;
import org.apache.maven.execution.MavenExecutionResult;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.embedder.IMaven;

import tmc.activator.CoreInitializer;
import tmc.services.GenericProjectOpener;
import tmc.ui.EclipseIdeUIInvoker;
import tmc.util.WorkbenchHelper;
import fi.helsinki.cs.plugin.tmc.Core;
import fi.helsinki.cs.plugin.tmc.async.tasks.AntTestrunnerTask;
import fi.helsinki.cs.plugin.tmc.async.tasks.CodeReviewRequestTask;
import fi.helsinki.cs.plugin.tmc.async.tasks.DownloaderTask;
import fi.helsinki.cs.plugin.tmc.async.tasks.FeedbackSubmissionTask;
import fi.helsinki.cs.plugin.tmc.async.tasks.MavenTestrunnerTask;
import fi.helsinki.cs.plugin.tmc.async.tasks.PastebinTask;
import fi.helsinki.cs.plugin.tmc.async.tasks.TestrunnerTask;
import fi.helsinki.cs.plugin.tmc.async.tasks.UploaderTask;
import fi.helsinki.cs.plugin.tmc.async.tasks.listeners.CodeReviewRequestListener;
import fi.helsinki.cs.plugin.tmc.async.tasks.listeners.PastebinTaskListener;
import fi.helsinki.cs.plugin.tmc.async.tasks.listeners.TestrunnerListener;
import fi.helsinki.cs.plugin.tmc.async.tasks.listeners.UploadTaskListener;
import fi.helsinki.cs.plugin.tmc.domain.Exercise;
import fi.helsinki.cs.plugin.tmc.domain.FeedbackAnswer;
import fi.helsinki.cs.plugin.tmc.domain.Project;
import fi.helsinki.cs.plugin.tmc.services.FeedbackAnswerSubmitter;
import fi.helsinki.cs.plugin.tmc.services.ProjectDownloader;
import fi.helsinki.cs.plugin.tmc.services.ProjectUploader;

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
        ProjectUploader uploader = new ProjectUploader(Core.getServerManager(), Core.getSettings());
        WorkbenchHelper helper = CoreInitializer.getDefault().getWorkbenchHelper();
        helper.initialize();

        Project project = helper.getActiveProject();

        UploaderTask task = new UploaderTask(uploader, project.getRootPath() + "/", Core.getProjectDAO(), invoker);
        Core.getTaskRunner().runTask(task, new UploadTaskListener(task, invoker));
    }

    public static void startPastebinTask(EclipseIdeUIInvoker invoker, String pasteMessage) {
        ProjectUploader uploader = new ProjectUploader(Core.getServerManager(), Core.getSettings());
        WorkbenchHelper helper = CoreInitializer.getDefault().getWorkbenchHelper();
        helper.initialize();

        Project project = helper.getActiveProject();

        PastebinTask task = new PastebinTask(uploader, project.getRootPath() + "/", pasteMessage, Core.getProjectDAO(),
                invoker);
        Core.getTaskRunner().runTask(task, new PastebinTaskListener(task, invoker));
    }

    public static void startCodeReviewRequestTask(EclipseIdeUIInvoker invoker, String requestMessage) {
        ProjectUploader uploader = new ProjectUploader(Core.getServerManager(), Core.getSettings());
        WorkbenchHelper helper = CoreInitializer.getDefault().getWorkbenchHelper();
        helper.initialize();

        Project project = helper.getActiveProject();

        CodeReviewRequestTask task = new CodeReviewRequestTask(uploader, project.getRootPath() + "/", requestMessage,
                Core.getProjectDAO(), invoker);
        Core.getTaskRunner().runTask(task, new CodeReviewRequestListener(task, invoker));
    }

    public static void startMavenTestRunnerTask(Project project, EclipseIdeUIInvoker invoker) {

        TestrunnerTask testrun = new MavenTestrunnerTask(project, invoker) {

            @Override
            public int runMaven(List<String> goals, Project project) {
                try {
                    IMaven maven = MavenPlugin.getMaven();
                    MavenExecutionRequest executionRequest = maven.createExecutionRequest(new NullProgressMonitor())
                            .setPom(new File(project.getRootPath() + "/pom.xml")).setGoals(goals);
                    MavenExecutionResult executionResult = maven.execute(executionRequest, new NullProgressMonitor());

                    System.out.println(executionResult.toString());

                    if (executionResult.hasExceptions()) {
                        return 1;
                    }

                    return 0;
                } catch (CoreException e) {
                    return 1;
                }
            }
        };

        TestrunnerListener listener = new TestrunnerListener(testrun, invoker);
        Core.getTaskRunner().runTask(testrun, listener);
    }

    public static void startAntTestRunnerTask(String projectRoot, String javaExecutable, EclipseIdeUIInvoker invoker) {

        TestrunnerTask testrun = new AntTestrunnerTask(projectRoot, projectRoot + "/test", javaExecutable, null,
                Core.getSettings(), invoker);
        TestrunnerListener listener = new TestrunnerListener(testrun, invoker);
        Core.getTaskRunner().runTask(testrun, listener);
    }
}
