package tmc.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PlatformUI;

import fi.helsinki.cs.plugin.tmc.Core;
import fi.helsinki.cs.plugin.tmc.async.tasks.UploaderTask;
import fi.helsinki.cs.plugin.tmc.async.tasks.listeners.UploadTaskListener;
import fi.helsinki.cs.plugin.tmc.services.ProjectUploader;

public class UploadHandler extends AbstractHandler {

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        ProjectUploader uploader = new ProjectUploader(Core.getServerManager());

        final IEditorPart activeEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                .getActiveEditor();
        if (activeEditor == null) {
            return null;
        }
        IFileEditorInput input = (IFileEditorInput) activeEditor.getEditorInput();
        IFile file = input.getFile();
        IProject activeProject = file.getProject();

        UploaderTask task = new UploaderTask(uploader, activeProject.getRawLocation().toString() + "/");
        Core.getTaskRunner().runTask(task, new UploadTaskListener(task));

        return null;
    }
}
