package fi.helsinki.cs.tmc.core.async.tasks;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import fi.helsinki.cs.tmc.core.async.SimpleBackgroundTask;
import fi.helsinki.cs.tmc.core.domain.Exercise;
import fi.helsinki.cs.tmc.core.domain.ProjectStatus;
import fi.helsinki.cs.tmc.core.services.ProjectOpener;

public class OpenAllDownloadedExercisesTask extends SimpleBackgroundTask<Exercise> {
    private ProjectOpener opener;

    public OpenAllDownloadedExercisesTask(String description, List<Exercise> list, ProjectOpener opener) {
        super(description, list);
        this.opener = opener;
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

        File root = new File(exercise.getProject().getRootPath());
        File buildFile = new File(root.getPath() + File.separator
                + exercise.getProject().getProjectType().getBuildFile());

        if (buildFile.exists()) {
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
