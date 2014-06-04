package tmc.tasks;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PlatformUI;

import tmc.services.GenericProjectOpener;
import tmc.ui.EclipseIdeUIInvoker;
import fi.helsinki.cs.plugin.tmc.Core;
import fi.helsinki.cs.plugin.tmc.async.tasks.DownloaderTask;
import fi.helsinki.cs.plugin.tmc.async.tasks.UploaderTask;
import fi.helsinki.cs.plugin.tmc.async.tasks.listeners.UploadTaskListener;
import fi.helsinki.cs.plugin.tmc.domain.Exercise;
import fi.helsinki.cs.plugin.tmc.services.ProjectDownloader;
import fi.helsinki.cs.plugin.tmc.services.ProjectUploader;

public final class TaskStarter {

    public static void startExerciseUploadTask(EclipseIdeUIInvoker invoker) {

        ProjectUploader uploader = new ProjectUploader(Core.getServerManager());

        final IEditorPart activeEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                .getActiveEditor();
        if (activeEditor == null) {
            return;
        }
        IFileEditorInput input = (IFileEditorInput) activeEditor.getEditorInput();
        IFile file = input.getFile();
        IProject activeProject = file.getProject();

        UploaderTask task = new UploaderTask(uploader, activeProject.getRawLocation().toString() + "/");
        Core.getTaskRunner().runTask(task, new UploadTaskListener(task, invoker));
    }

    public static void startExerciseDownloadTask(List<Exercise> exercises) {
        ProjectDownloader downloader = new ProjectDownloader(Core.getServerManager());
        Core.getTaskRunner().runTask(
                new DownloaderTask(downloader, new GenericProjectOpener(), exercises, Core.getProjectDAO(), Core
                        .getSettings()));
    }
}
