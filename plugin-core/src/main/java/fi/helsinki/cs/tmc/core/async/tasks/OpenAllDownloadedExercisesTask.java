package fi.helsinki.cs.tmc.core.async.tasks;

import java.util.ArrayList;
import java.util.List;

import fi.helsinki.cs.tmc.core.async.SimpleBackgroundTask;
import fi.helsinki.cs.tmc.core.domain.Exercise;
import fi.helsinki.cs.tmc.core.domain.ProjectStatus;
import fi.helsinki.cs.tmc.core.io.FileIO;
import fi.helsinki.cs.tmc.core.io.IOFactory;
import fi.helsinki.cs.tmc.core.services.ProjectOpener;

public class OpenAllDownloadedExercisesTask extends SimpleBackgroundTask<Exercise> {

    private ProjectOpener opener;
    private IOFactory io;

    public OpenAllDownloadedExercisesTask(String description, List<Exercise> list, ProjectOpener opener, IOFactory io) {
        super(description, list);
        this.opener = opener;
        this.io = io;
    }

    @Override
    public void run(Exercise exercise) {
        if (canOpen(exercise)) {
            opener.open(exercise);
            exercise.getProject().setStatus(ProjectStatus.DOWNLOADED);
        }
    }

    private boolean canOpen(Exercise exercise) {
        if (exercise.getProject().getReadOnlyProjectFiles().isEmpty()) {
            return false;
        }

        FileIO root = io.newFile(exercise.getProject().getRootPath());
        FileIO buildFile = io.newFile(root.getPath() + "/" + exercise.getProject().getProjectType().getBuildFile());

        if (buildFile.fileExists()) {
            return true;
        } else {
            cleanup(exercise);
            return false;
        }
    }

    private void cleanup(Exercise exercise) {
        exercise.getProject().setProjectFiles(new ArrayList<String>());
    }

}
