package fi.helsinki.cs.tmc.core.async.tasks;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.google.common.util.concurrent.Futures;

public class SingletonTask {
    private ScheduledExecutorService scheduler;
    private Runnable runnable;
    private Future<?> task;
    private ScheduledFuture<?> autostartTask = null;

    public SingletonTask(Runnable runnable, ScheduledExecutorService scheduler) {
        this.scheduler = scheduler;
        this.runnable = runnable;
        this.task = Futures.immediateFuture(null);
    }

    public synchronized void setInterval(long delay) {
        unsetInterval();

        autostartTask = scheduler.scheduleWithFixedDelay(autostartRunnable, delay, delay, TimeUnit.MILLISECONDS);
    }

    public synchronized void unsetInterval() {
        if (autostartTask != null) {
            autostartTask.cancel(true);
            autostartTask = null;
        }
    }

    private final Runnable autostartRunnable = new Runnable() {
        @Override
        public void run() {
            start();
        }
    };

    /**
     * Starts the task unless it's already running.
     */
    public synchronized void start() {
        if (task.isDone()) {
            task = scheduler.submit(runnable);
        }
    }

    /**
     * Waits for the task to finish if it is currently running.
     * 
     * Note: this method does not indicate in any way whether the task succeeded
     * or failed.
     * 
     * @param timeout
     *            Maximum time in milliseconds to wait before throwing a
     *            TimeoutException.
     */
    public synchronized void waitUntilFinished(long timeout) throws TimeoutException, InterruptedException {
        try {
            task.get(timeout, TimeUnit.MILLISECONDS);
        } catch (ExecutionException ex) {
            // Ignore
        }
    }

    public synchronized boolean isRunning() {
        return !task.isDone();
    }
}
