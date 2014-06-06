package fi.helsinki.cs.plugin.tmc.async.tasks;

import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

public class PeriodicTask {

    private Runnable task;
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> scheduledTask;
    private long period;

    public PeriodicTask(Runnable task) {
        this.task = task;
    }

    public void setInterval(long period) {
        this.period = period;
        startTask();
    }

    public void unsetInterval() {
        if (task != null) {
            scheduledTask.cancel(true);
        }

    }

    public void start() {
        task.run();
    }

    public void waitUntilFinished(long delay) {

    }

    private void startTask() {
        if (timer != null) {
            timer.cancel();
        }

        timer.scheduleAtFixedRate(task, (Calendar.getInstance()).getTime(), period);
    }

}