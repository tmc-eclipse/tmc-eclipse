package fi.helsinki.cs.plugin.tmc.async;

public interface BackgroundTaskListener {

    public void onBegin();

    public void onSuccess();

    public void onFailure();

}
