package fi.helsinki.cs.tmc.core.spyware.services;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import fi.helsinki.cs.tmc.core.domain.InvalidProjectTypeException;
import fi.helsinki.cs.tmc.core.domain.Project;
import fi.helsinki.cs.tmc.core.io.FileIO;
import fi.helsinki.cs.tmc.core.io.zip.RecursiveZipper;
import fi.helsinki.cs.tmc.core.services.ProjectDAO;
import fi.helsinki.cs.tmc.core.services.Settings;
import fi.helsinki.cs.tmc.core.spyware.ChangeType;
import fi.helsinki.cs.tmc.core.spyware.SnapshotInfo;
import fi.helsinki.cs.tmc.core.spyware.utility.ActiveThreadSet;
import fi.helsinki.cs.tmc.core.spyware.utility.JsonMaker;

public class SnapshotTaker {
    private SnapshotInfo info;
    private ActiveThreadSet threadSet;
    private EventReceiver receiver;
    private Settings settings;
    private ProjectDAO projectDAO;

    private static final Logger log = Logger.getLogger(SnapshotTaker.class.getName());

    public SnapshotTaker(ActiveThreadSet threadSet, EventReceiver receiver, Settings settings, ProjectDAO projectDAO) {
        this.threadSet = threadSet;
        this.receiver = receiver;
        this.settings = settings;
        this.projectDAO = projectDAO;
    }

    public void execute(SnapshotInfo info) {
        this.info = info;

        if (info.getChangeType() == ChangeType.FILE_RENAME || info.getChangeType() == ChangeType.FOLDER_RENAME) {
            handleRename();
        } else {
            handleChange();
        }
    }

    private void handleChange() {

        String metadata = JsonMaker.create().add("cause", info.getChangeType().name().toLowerCase())
                .add("file", info.getCurrentFilePath()).toString();

        startSnapshotThread(metadata, info.getCurrentFullFilePath());

    }

    private void handleRename() {

        String metadata = JsonMaker.create().add("cause", info.getChangeType().name().toLowerCase())
                .add("file", info.getCurrentFilePath()).add("previous_name", info.getOldFilePath()).toString();

        startSnapshotThread(metadata, info.getOldFullFilePath());

    }

    private void startSnapshotThread(final String metadata, String path) {
        if (!settings.isSpywareEnabled()) {
            return;
        }

        Project project = projectDAO.getProjectByFile(path);

        // Note: Should *only* log TMC courses.
        if (project == null) {
            return;
        }

        SnapshotThread thread = new SnapshotThread(receiver, project, metadata);
        threadSet.addThread(thread);
        thread.setDaemon(true);
        thread.start();

    }

    private static class SnapshotThread extends Thread {
        private final EventReceiver receiver;
        private final Project project;
        private final String metadata;

        private SnapshotThread(EventReceiver receiver, Project project, String metadata) {
            super("Source snapshot");
            this.receiver = receiver;
            this.project = project;
            this.metadata = metadata;
        }

        @Override
        public void run() {

            // Note note: the following note is from the original netbeans code

            // Note that, being in a thread, this is inherently prone to races
            // that modify the project. For now we just accept that. Not sure if
            // the File Object API would allow some sort of global locking of
            // the project.
            RecursiveZipper zipper;
            try {
                zipper = new RecursiveZipper(new FileIO(project.getRootPath()), project.getZippingDecider());
            } catch (InvalidProjectTypeException e) {
                // this exception is thrown when file list is empty
                return;
            }
            try {
                byte[] data = zipper.zipProjectSources();
                LoggableEvent event = new LoggableEvent(project.getExercise(), "code_snapshot", data, metadata);
                receiver.receiveEvent(event);
            } catch (IOException ex) {
                // Warning might be also appropriate, but this often races with
                // project closing during integration tests, and there warning
                // would cause a dialog to appear, failing the test.
                log.log(Level.INFO, "Error zipping project sources in: " + project.getRootPath(), ex);
            }

        }
    }

}
