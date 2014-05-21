package fi.helsinki.cs.plugin.tmc.tasks;

import java.io.IOException;
import java.util.List;

import fi.helsinki.cs.plugin.tmc.Core;
import fi.helsinki.cs.plugin.tmc.domain.Exercise;
import fi.helsinki.cs.plugin.tmc.io.FileIO;
import fi.helsinki.cs.plugin.tmc.io.Unzipper;
import fi.helsinki.cs.plugin.tmc.services.ExerciseDownloader;
import fi.helsinki.cs.plugin.tmc.services.ZippedProject;

public class DownloaderTask implements BackgroundTask<Object> {

    private boolean isRunning;
    private List<Exercise> exerciseList;

    public DownloaderTask(List<Exercise> exerciseList) {
        this.isRunning = true;
        this.exerciseList = exerciseList;
    }

    @Override
    public Object start(TaskFeedback tf) {
        tf.setAmountOfWork(exerciseList.size() * 2);
        
        ExerciseDownloader downloader = new ExerciseDownloader();
        
        for(Exercise e : exerciseList) {
            if(!isRunning) {
                break;
            }
            
            ZippedProject zip = downloader.downloadExercise(e);
            tf.setProgress(tf.getProgress() + 1);
            
            try {
                Unzipper unzipper = new Unzipper(zip);
                unzipper.unzipTo(new FileIO(Core.getSettings().getExerciseFilePath() + "/" + Core.getSettings().getCurrentCourseName()));
                tf.setProgress(tf.getProgress() + 1);
            } catch(IOException exception) {
                // TODO: handle exception
            }
        }
        
        return null;
    }

    @Override
    public void stop() {
        this.isRunning = false;
    }
    
    @Override
    public String getName() {
        return "DownloaderTask";
    }

}
