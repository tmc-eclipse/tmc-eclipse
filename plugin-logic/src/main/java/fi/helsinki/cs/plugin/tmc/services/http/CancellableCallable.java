package fi.helsinki.cs.plugin.tmc.services.http;

import java.util.concurrent.Callable;

public interface CancellableCallable<V> extends Callable<V>{

    public boolean cancel();
    
}
