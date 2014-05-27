package fi.helsinki.cs.plugin.tmc.async;

import java.util.List;

public abstract class SimpleBackgroundTask<T> implements BackgroundTask {

    private boolean isRunning;
    private String description;

    private List<T> list;

    public SimpleBackgroundTask(String description, List<T> list) {
        this.isRunning = true;
        this.description = description;
        this.list = list;
    }

    @Override
    public void start(TaskFeedback progress) {
        progress.startProgress(description, list.size());

        for (T t : list) {
            if (progress.isCancelRequested()) {
                this.stop();
            }
            if (!isRunning) {
                break;
            }

            run(t);

            progress.incrementProgress(1);
        }
    }

    @Override
    public void stop() {
        this.isRunning = false;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public abstract void run(T t);

}