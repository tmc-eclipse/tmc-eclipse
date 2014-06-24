package fi.helsinki.cs.tmc.core.spyware;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import fi.helsinki.cs.tmc.core.spyware.DocumentInfo;
import fi.helsinki.cs.tmc.core.spyware.SnapshotInfo;
import fi.helsinki.cs.tmc.core.spyware.SpywarePluginLayer;
import fi.helsinki.cs.tmc.core.spyware.services.DocumentChangeHandler;
import fi.helsinki.cs.tmc.core.spyware.services.EventReceiver;
import fi.helsinki.cs.tmc.core.spyware.services.SnapshotTaker;
import fi.helsinki.cs.tmc.core.spyware.utility.ActiveThreadSet;

public class SpywarePluginLayerTest {
    private ActiveThreadSet activeThreads;
    private EventReceiver receiver;
    private SnapshotTaker taker;
    private DocumentChangeHandler documentHandler;

    private SpywarePluginLayer spl;

    @Before
    public void setUp() throws Exception {
        activeThreads = mock(ActiveThreadSet.class);
        receiver = mock(EventReceiver.class);
        taker = mock(SnapshotTaker.class);
        documentHandler = mock(DocumentChangeHandler.class);

        spl = new SpywarePluginLayer(activeThreads, receiver, taker, documentHandler);
    }

    @Test
    public void takeSnapshotExecutesTaker() {
        SnapshotInfo info = mock(SnapshotInfo.class);

        spl.takeSnapshot(info);
        verify(taker, times(1)).execute(info);
    }

    @Test
    public void documentChangeAsksDocumentHandlerToHandleEvent() {
        DocumentInfo info = mock(DocumentInfo.class);

        spl.documentChange(info);
        verify(documentHandler, times(1)).handleEvent(info);
    }

    @Test
    public void closeCallsJoinAllForActiveThreads() throws InterruptedException {
        spl.close();
        verify(activeThreads, times(1)).joinAll();
    }

    @Test
    public void closeGracefullyHandlesException() throws InterruptedException {
        doThrow(new InterruptedException()).when(activeThreads).joinAll();

        spl.close();
    }
}
