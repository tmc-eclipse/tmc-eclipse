package tmc.eclipse.tasks;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import fi.helsinki.cs.tmc.core.Core;
import fi.helsinki.cs.tmc.core.services.Settings;

public class RecurringTaskRunner {
    private static int INITIAL_WAIT = 0;

    // 1200s = 60s * 20min = every 20 min
    private static int CODE_REVIEW_FETCH_INTERVAL = 1200;

    // 3600s = 60s * 60min = every 1h
    private static int EXERCISE_UPDATE_INTERVAL = 3600;

    private ScheduledExecutorService scheduler;
    private Settings settings;

    private ScheduledFuture<?> exerciseUpdaterBackgroundTask;

    public RecurringTaskRunner(Settings settings) {
        this.scheduler = Executors.newScheduledThreadPool(2);
        this.settings = settings;
    }

    public void startRecurringTasks() {
        Core.getTaskRunner();
        startCoreReviewChecks();
        updateBackgroundExerciseUpdateChecks();
    }

    public void updateBackgroundExerciseUpdateChecks() {
        boolean shouldRun = settings.isCheckingForUpdatesInTheBackground();
        boolean isRunning = exerciseUpdaterBackgroundTask != null;

        if (shouldRun && !isRunning) {
            startBackgroundExerciseUpdateChecks();
        } else if (!shouldRun && isRunning) {
            endBackgroundExerciseUpdateChecks();
        }
    }

    private void startCoreReviewChecks() {
        CheckForCodeReviewsOnBackgroundTask task = new CheckForCodeReviewsOnBackgroundTask();
        scheduler.scheduleAtFixedRate(task, INITIAL_WAIT, CODE_REVIEW_FETCH_INTERVAL, TimeUnit.SECONDS);
    }

    private void startBackgroundExerciseUpdateChecks() {
        CheckForNewOrUpdatedExercisesOnBackgroundTask task = new CheckForNewOrUpdatedExercisesOnBackgroundTask();
        exerciseUpdaterBackgroundTask = scheduler.scheduleAtFixedRate(task, INITIAL_WAIT, EXERCISE_UPDATE_INTERVAL,
                TimeUnit.SECONDS);
    }

    private void endBackgroundExerciseUpdateChecks() {
        if (exerciseUpdaterBackgroundTask != null) {
            exerciseUpdaterBackgroundTask.cancel(true);
            exerciseUpdaterBackgroundTask = null;
        }
    }
}