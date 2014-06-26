package tmc.eclipse.tasks;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import fi.helsinki.cs.tmc.core.async.BackgroundTask;
import fi.helsinki.cs.tmc.core.async.BackgroundTaskListener;
import fi.helsinki.cs.tmc.core.async.BackgroundTaskRunner;

public class EclipseTaskRunner implements BackgroundTaskRunner {

    @Override
    public void runTask(final BackgroundTask task) {
        runTask(task, null);
    }

    @Override
    public void runTask(final BackgroundTask task, final BackgroundTaskListener listener) {

        Job job = new Job(task.getDescription()) {

            @Override
            protected IStatus run(IProgressMonitor monitor) {
                if (listener != null) {
                    listener.onBegin();
                }

                int returnValue = task.start(new EclipseTaskStatusMonitor(monitor));

                switch (returnValue) {
                case BackgroundTask.RETURN_FAILURE:
                    if (listener != null) {
                        listener.onFailure();
                    }
                    return Status.CANCEL_STATUS;
                case BackgroundTask.RETURN_INTERRUPTED:
                    if (listener != null) {
                        listener.onInterruption();
                    }
                    return Status.CANCEL_STATUS;
                case BackgroundTask.RETURN_SUCCESS:
                    if (listener != null) {
                        listener.onSuccess();
                    }
                    return Status.OK_STATUS;
                }

                return Status.OK_STATUS;
            }

        };

        job.schedule();
    }

    @Override
    public void cancelTask(BackgroundTask task) {
        task.stop();
    }

}
