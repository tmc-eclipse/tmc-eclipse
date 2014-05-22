package tmc.tasks;

import org.eclipse.core.runtime.IProgressMonitor;

import fi.helsinki.cs.plugin.tmc.tasks.TaskFeedback;

public class EclipseTaskFeedbackAdapter implements TaskFeedback {

    private IProgressMonitor monitor;

    public EclipseTaskFeedbackAdapter(IProgressMonitor monitor) {
        this.monitor = monitor;
    }

    @Override
    public void resetProgress(String message, int amountOfWork) {
        monitor.beginTask(message, amountOfWork);
    }

    @Override
    public void updateProgress(int progress) {
        monitor.worked(progress);
    }

    @Override
    public boolean isCanceled() {
        return monitor.isCanceled();
    }

}
