package fi.helsinki.cs.plugin.tmc.tasks;

public interface BackgroundTaskRunner<V> {

    public V runTask(BackgroundTask<V> task);
    public void cancelTask();
    
}