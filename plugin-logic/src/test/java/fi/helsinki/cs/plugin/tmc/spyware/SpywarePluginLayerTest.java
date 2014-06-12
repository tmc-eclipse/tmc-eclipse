package fi.helsinki.cs.plugin.tmc.spyware;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

public class SpywarePluginLayerTest {

    SpywarePluginLayer spl;
    SnapshotInfo info;

    @Before
    public void setUp() throws Exception {
        spl = mock(SpywarePluginLayer.class);
        info = mock(SnapshotInfo.class);
    }

    @Test
    public void testTakeSnapshot() {
        spl.takeSnapshot(info);
        verify(info, times(1));
    }

    @Test
    public void testClose() {
        spl.close();
        verify(spl, times(1)).close();
    }
}
