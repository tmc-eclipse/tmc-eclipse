package fi.helsinki.cs.plugin.tmc.async.tasks;

import fi.helsinki.cs.plugin.tmc.async.BackgroundTask;
import fi.helsinki.cs.plugin.tmc.async.BackgroundTaskListener;
import fi.helsinki.cs.plugin.tmc.async.BackgroundTaskRunner;
import fi.helsinki.cs.plugin.tmc.async.TaskFeedback;

public class TestTaskRunner implements BackgroundTaskRunner {

    class TaskFeedbackDummy implements TaskFeedback {

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

    private TaskFeedback taskFeedback;

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
