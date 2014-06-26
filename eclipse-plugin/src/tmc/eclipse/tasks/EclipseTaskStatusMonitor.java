package tmc.eclipse.tasks;

import org.eclipse.core.runtime.IProgressMonitor;

import fi.helsinki.cs.tmc.core.async.TaskStatusMonitor;

public class EclipseTaskStatusMonitor implements TaskStatusMonitor {

    private IProgressMonitor monitor;

    public EclipseTaskStatusMonitor(IProgressMonitor monitor) {
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
