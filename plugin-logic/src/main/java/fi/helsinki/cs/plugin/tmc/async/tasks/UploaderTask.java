package fi.helsinki.cs.plugin.tmc.async.tasks;

import java.util.List;

import fi.helsinki.cs.plugin.tmc.Core;
import fi.helsinki.cs.plugin.tmc.async.SimpleBackgroundTask;
import fi.helsinki.cs.plugin.tmc.services.ProjectUploader;

public class UploaderTask extends SimpleBackgroundTask<String> {

    private ProjectUploader uploader;

    public UploaderTask(ProjectUploader uploader, List<String> path) {
        super("Uploading exercises", path);
        this.uploader = uploader;
    }

    @Override
    public void run(String path) {
        try {
            uploader.uploadProject(path);

        } catch (Exception ex) {
            Core.getErrorHandler().raise("An error occurred while uploading exercises: " + ex.getMessage());
        }
    }

}