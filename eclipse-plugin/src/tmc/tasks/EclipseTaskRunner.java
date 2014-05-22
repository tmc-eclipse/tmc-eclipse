package tmc.tasks;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import fi.helsinki.cs.plugin.tmc.tasks.BackgroundTask;
import fi.helsinki.cs.plugin.tmc.tasks.BackgroundTaskRunner;

public class EclipseTaskRunner implements BackgroundTaskRunner {

    @Override
    public void runTask(final BackgroundTask task) {

        Job job = new Job(task.getName()) {

            @Override
            protected IStatus run(IProgressMonitor monitor) {
                task.start(new EclipseTaskFeedbackAdapter(monitor));
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
