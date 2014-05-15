package fi.helsinki.cs.plugin.tmc.getJson;

import java.util.concurrent.Callable;

import org.openide.util.Cancellable;


public interface CancellableCallable<V> extends  Callable<V>, Cancellable{

}
