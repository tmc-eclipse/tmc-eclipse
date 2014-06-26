package fi.helsinki.cs.tmc.core.async.tasks;

import fi.helsinki.cs.tmc.core.async.BackgroundTask;
import fi.helsinki.cs.tmc.core.async.BackgroundTaskListener;
import fi.helsinki.cs.tmc.core.async.BackgroundTaskRunner;
import fi.helsinki.cs.tmc.core.async.TaskStatusMonitor;

public class TestTaskRunner implements BackgroundTaskRunner {

    class TaskFeedbackDummy implements TaskStatusMonitor {

        @Override
        public void startProgress(String message, int amountOfWork) {
        }

        @Override
        public void incrementProgress(int progress) {
        }

        @Override
        public boolean isCancelRequested() {
            return false;
        }

    }

    private TaskStatusMonitor taskFeedback;

    public TestTaskRunner() {
        this.taskFeedback = new TaskFeedbackDummy();
    }

    @Override
    public void runTask(BackgroundTask task) {
        task.start(taskFeedback);
    }

    @Override
    public void runTask(BackgroundTask task, BackgroundTaskListener listener) {
        listener.onBegin();

        int returnValue = task.start(taskFeedback);

        if (returnValue == BackgroundTask.RETURN_FAILURE) {
            listener.onFailure();
        }
        if (returnValue == BackgroundTask.RETURN_SUCCESS) {
            listener.onSuccess();
        }
    }

    @Override
    public void cancelTask(BackgroundTask task) {
    }

}
