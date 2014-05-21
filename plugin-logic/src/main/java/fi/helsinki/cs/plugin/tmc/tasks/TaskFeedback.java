package fi.helsinki.cs.plugin.tmc.tasks;

public interface TaskFeedback {
    
    public void resetProgress(String message, int amountOfWork);
    public void updateProgress(int progress);
    
    public boolean isCanceled();

}
