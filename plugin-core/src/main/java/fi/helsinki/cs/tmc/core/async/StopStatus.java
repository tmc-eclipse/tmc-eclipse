package fi.helsinki.cs.tmc.core.async;

/**
 * Helper interface; sometimes stop status needs to be polled from inside the
 * methods the bg task is calling. (Example: submission feedback task polls this
 * while it waits server to respond so that the task can be stopped during the
 * wait) It should provide the information through this interface
 * 
 */
public interface StopStatus {

    public abstract boolean mustStop();

}