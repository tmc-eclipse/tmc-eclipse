package fi.helsinki.cs.plugin.tmc.spyware.services;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import fi.helsinki.cs.plugin.tmc.Core;
import fi.helsinki.cs.plugin.tmc.domain.Course;
import fi.helsinki.cs.plugin.tmc.domain.Exercise;
import fi.helsinki.cs.plugin.tmc.services.Settings;
import fi.helsinki.cs.plugin.tmc.spyware.ChangeType;
import fi.helsinki.cs.plugin.tmc.spyware.SnapshotInfo;
import fi.helsinki.cs.plugin.tmc.spyware.utility.ActiveThreadSet;
import fi.helsinki.cs.plugin.tmc.spyware.utility.JsonMaker;
import fi.helsinki.cs.plugin.tmc.spyware.utility.zipping.MavenZippingDecider;
import fi.helsinki.cs.plugin.tmc.spyware.utility.zipping.ZippingDecider;

public class SnapshotTaker {
	private SnapshotInfo info;
	private ActiveThreadSet threadSet;
	private EventReceiver receiver;
	
	private static final Logger log = Logger.getLogger(SnapshotTaker.class.getName());
	
	public SnapshotTaker(SnapshotInfo info, ActiveThreadSet threadSet, EventReceiver receiver) {
		this.info = info;
		this.threadSet = threadSet;
		this.receiver = receiver;
	}
	
		
	public void execute() {
		System.out.println("Project name: " + info.getProjectName());
		System.out.println("Old path: " + info.getOldFilePath());
		System.out.println("Current path: " + info.getCurrentFilePath());
		System.out.println("Change type: " + info.getChangeType().name().toLowerCase());
		
		if (info.getChangeType() == ChangeType.FILE_RENAME || info.getChangeType() == ChangeType.FOLDER_RENAME) {
			handleRename();
		} else {
			handleChange();
		}
	}

	private void handleChange() {

		
		String metadata = JsonMaker.create()
                .add("cause", info.getChangeType().name().toLowerCase())
                .add("file", info.getCurrentFilePath())
                .toString();

		startSnapshotThread(metadata);
		
	}

	private void handleRename() {

		String metadata = JsonMaker.create()
                .add("cause", info.getChangeType().name().toLowerCase())
                .add("file", info.getCurrentFilePath())
                .add("previous_name", info.getOldFilePath())
                .toString();
		
		startSnapshotThread(metadata);
		
	}
	
    private void startSnapshotThread(String metadata) {
        if (!Settings.getDefaultSettings().isSpywareEnabled()) {
            return;
        }
        
        // kinda stupid, probably should figure a better way to do this
        // basically we need to find the exercise that contains this project
        
        Exercise exercise = null;
       
        // Note: Should *only* log TMC courses.
      /*  out:
        for (Course c : Core.getCourses().getCourses()) {
        	for (Exercise e : c.getExercises()) {
        		if (e.getName().contains(info.getProjectName())) {
        			exercise = e;
        			break out;
        		}
        	}        	
        }
        
        if (exercise != null) {
            SnapshotThread thread = new SnapshotThread(receiver, exercise, new MavenZippingDecider(), metadata);
            threadSet.addThread(thread);
            thread.setDaemon(true);
            thread.start();
        }*/
    }
    
    private static class SnapshotThread extends Thread {
        private final EventReceiver receiver;
        private final Exercise exercise;
       // private final TmcProjectInfo projectInfo;
        private final String metadata;
        private ZippingDecider decider;

        private SnapshotThread(EventReceiver receiver, Exercise exercise, ZippingDecider decider, String metadata) {
            super("Source snapshot");
            this.receiver = receiver;
            this.exercise = exercise;
         //   this.projectInfo = projectInfo;
            this.metadata = metadata;
        }

        @Override
        public void run() {
        /*	
            // Note that, being in a thread, this is inherently prone to races that modify the project.
            // For now we just accept that. Not sure if the FileObject API would allow some sort of
            // global locking of the project.
            File projectDir = projectInfo.getProjectDirAsFile();
            
            RecursiveZipper zipper = new RecursiveZipper(projectDir, zippingDecider);
            try {
                byte[] data = zipper.zipProjectSources();
                LoggableEvent event = new LoggableEvent(exercise, "code_snapshot", data, metadata);
                receiver.receiveEvent(event);
            } catch (IOException ex) {
                // Warning might be also appropriate, but this often races with project closing
                // during integration tests, and there warning would cause a dialog to appear,
                // failing the test.
                log.log(Level.INFO, "Error zipping project sources in: " + projectDir, ex);
            }*/
        }
    }
    

}
