package fi.helsinki.cs.plugin.tmc.storage;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import fi.helsinki.cs.plugin.tmc.domain.Course;
import fi.helsinki.cs.plugin.tmc.io.IO;
import fi.helsinki.cs.plugin.tmc.ui.UserVisibleException;

public class LocalCourseStorageTest {

    private IO io;
    private LocalCourseStorage lcs;

    @Before
    public void setUp() {
        this.io = mock(IO.class);
        lcs = new LocalCourseStorage(io);
    }

    @Test(expected = UserVisibleException.class)
    public void testExceptionIsThrownIfNullIO() throws UserVisibleException {
        when(io.fileExists()).thenReturn(true);
        this.io = null;
        lcs.load();
    }

    @Test
    public void testExceptionIsThrownIfFileDoesntExist() throws UserVisibleException {
        when(io.fileExists()).thenReturn(false);
        assertTrue(lcs.load() instanceof List && lcs.load().size() == 0);
    }

    @Test(expected = UserVisibleException.class)
    public void testExceptionIsThrownIfReaderIsNull() throws UserVisibleException {
        when(io.fileExists()).thenReturn(true);
        when(io.getReader()).thenReturn(null);
        lcs.load();
    }

    @Test(expected = UserVisibleException.class)
    public void testExceptionIsThrownIfWriterIsNull() throws UserVisibleException {
        when(io.getWriter()).thenReturn(null);
        lcs.save(new ArrayList<Course>());
    }

    @Test(expected = UserVisibleException.class)
    public void saveThrowsErrorWhenIoIsNull() {
        LocalCourseStorage l = new LocalCourseStorage(null);
        l.save(new ArrayList<Course>());
    }

}
