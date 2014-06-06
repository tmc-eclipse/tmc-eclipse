package fi.helsinki.cs.plugin.tmc.spyware;

import fi.helsinki.cs.plugin.tmc.spyware.services.EventReceiver;

import java.io.Closeable;

import fi.helsinki.cs.plugin.tmc.spyware.services.SnapshotTaker;
import fi.helsinki.cs.plugin.tmc.spyware.utility.ActiveThreadSet;


public class SpywarePluginLayer implements Closeable {
	private ActiveThreadSet activeThreads;
	private EventReceiver receiver;
	public SpywarePluginLayer() {
		// todo - refactor
		activeThreads = new ActiveThreadSet();
		receiver = null;
	}
	
	public void takeSnapshot(SnapshotInfo info) {
		(new SnapshotTaker(info, activeThreads, receiver)).execute();
	}
	
	@Override
	public void close() {
		// TODO run in a separate thread 
		try {
			activeThreads.joinAll();
		} catch (InterruptedException e) {
			// do nothing
		}		
	}
	
}
