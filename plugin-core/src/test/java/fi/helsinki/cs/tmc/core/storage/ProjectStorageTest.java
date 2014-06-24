package fi.helsinki.cs.tmc.core.storage;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import fi.helsinki.cs.tmc.core.io.FileIO;
import fi.helsinki.cs.tmc.core.ui.UserVisibleException;

public class ProjectStorageTest {

    private ProjectStorage storage;
    private FileIO io;

    @Before
    public void setUp() throws NoSuchFieldException, SecurityException, IllegalArgumentException,
            IllegalAccessException {
        io = mock(FileIO.class);
        storage = new ProjectStorage(io);

    }

    @Test
    public void loadReturnsEmptyListIfFileDoesNotExist() {
        when(io.fileExists()).thenReturn(false);
        assertEquals(0, storage.load().size());
    }

    @Test(expected = UserVisibleException.class)
    public void loadThrowsIfReaderIsNull() {
        when(io.fileExists()).thenReturn(true);
        when(io.getReader()).thenReturn(null);
        storage.load();
    }

}
