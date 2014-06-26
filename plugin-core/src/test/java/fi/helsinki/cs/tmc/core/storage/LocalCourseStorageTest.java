package fi.helsinki.cs.tmc.core.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

import fi.helsinki.cs.tmc.core.domain.Course;
import fi.helsinki.cs.tmc.core.io.FileIO;
import fi.helsinki.cs.tmc.core.ui.UserVisibleException;
import fi.helsinki.cs.tmc.core.utils.jsonhelpers.CourseList;

public class LocalCourseStorageTest {

    private FileIO io;
    private CourseStorage lcs;

    @Before
    public void setUp() {
        this.io = mock(FileIO.class);
        lcs = new CourseStorage(io);

        when(io.fileExists()).thenReturn(true);
    }

    @Test(expected = UserVisibleException.class)
    public void testExceptionIsThrownIfNullIO() throws UserVisibleException {
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
        CourseStorage l = new CourseStorage(null);
        l.save(new ArrayList<Course>());
    }

    @Test
    public void loadReturnsCorrectListOfCourses() {
        CourseList cl = buildMockCourseList();
        String clJson = new Gson().toJson(cl);
        Reader reader = new StringReader(clJson);
        when(io.getReader()).thenReturn(reader);

        List<Course> returned = lcs.load();
        assertEquals("c1", returned.get(0).getName());
    }

    @Test(expected = UserVisibleException.class)
    public void saveThrowsIfWritesIsNull() {
        when(io.getWriter()).thenReturn(null);
        lcs.save(new ArrayList<Course>());
    }

    @Test
    public void exceptionIsCaughtIfClosingWriterThrows() throws IOException {
        Writer writer = mock(Writer.class);
        when(io.getWriter()).thenReturn(writer);
        doThrow(new IOException("Foo")).when(writer).close();

        lcs.save(new ArrayList<Course>());
        verify(writer, times(1)).close();
    }

    private CourseList buildMockCourseList() {
        Course c1 = new Course("c1");
        Course[] cl = {c1};
        CourseList courseList = new CourseList();
        courseList.setCourses(cl);
        courseList.setApiVersion("7");
        return courseList;
    }

}
