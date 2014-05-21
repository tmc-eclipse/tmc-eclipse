package fi.helsinki.cs.plugin.tmc.tasks;

public class TaskFeedback {
    
    private int totalWork;
    private int progress;
    private String message;

    public TaskFeedback() {
        progress = 0;
    }

    public int getAmountOfWork() {
        return totalWork;
    }

    public void setAmountOfWork(int amountOfWork) {
        totalWork = amountOfWork;
    }
    
    public int getCurrentProgressPercentage() {
        return (int)((double)progress) / totalWork * 100;
    }

    public int getProgress() {
        return progress;
    }
    
    public void setProgress(int newProgress) {
        progress = newProgress;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }

}
