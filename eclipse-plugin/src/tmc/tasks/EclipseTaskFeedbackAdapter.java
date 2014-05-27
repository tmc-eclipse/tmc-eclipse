package tmc.tasks;

import org.eclipse.core.runtime.IProgressMonitor;

import fi.helsinki.cs.plugin.tmc.async.TaskFeedback;

public class EclipseTaskFeedbackAdapter implements TaskFeedback {

    private IProgressMonitor monitor;

    public EclipseTaskFeedbackAdapter(IProgressMonitor monitor) {
        this.monitor = monitor;
    }

    @Override
    public void startProgress(String message, int amountOfWork) {
        monitor.beginTask(message, amountOfWork);
    }

    @Override
    public void incrementProgress(int progress) {
        monitor.worked(progress);
    }

    @Override
    public boolean isCancelRequested() {
        return monitor.isCanceled();
    }

}
