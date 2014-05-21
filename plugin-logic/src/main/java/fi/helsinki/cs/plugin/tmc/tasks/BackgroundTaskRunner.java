package fi.helsinki.cs.plugin.tmc.tasks;

public interface BackgroundTaskRunner {
 
    public void runTask(BackgroundTask task);
    public void cancelTask(BackgroundTask task);
    
}