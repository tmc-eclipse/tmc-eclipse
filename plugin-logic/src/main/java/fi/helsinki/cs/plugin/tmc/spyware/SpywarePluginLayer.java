package fi.helsinki.cs.plugin.tmc.spyware;

import java.io.Closeable;

import fi.helsinki.cs.plugin.tmc.spyware.services.DocumentChangeHandler;
import fi.helsinki.cs.plugin.tmc.spyware.services.EventReceiver;
import fi.helsinki.cs.plugin.tmc.spyware.services.SnapshotTaker;
import fi.helsinki.cs.plugin.tmc.spyware.utility.ActiveThreadSet;

public class SpywarePluginLayer implements Closeable {
    private ActiveThreadSet activeThreads;
    private EventReceiver receiver;
    SnapshotTaker taker;
    private DocumentChangeHandler documentHandler;

    public SpywarePluginLayer(ActiveThreadSet activeThreads, EventReceiver receiver, SnapshotTaker taker,
            DocumentChangeHandler documentHandler) {
        this.activeThreads = activeThreads;
        this.receiver = receiver;
        this.taker = taker;
        this.documentHandler = documentHandler;
    }

    public void takeSnapshot(SnapshotInfo info) {
        taker.execute(info);
    }

    public void documentChange(DocumentInfo info) {
        documentHandler.handleEvent(info);
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
