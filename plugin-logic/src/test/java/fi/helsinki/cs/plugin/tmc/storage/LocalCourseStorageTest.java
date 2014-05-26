package fi.helsinki.cs.plugin.tmc.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

import fi.helsinki.cs.plugin.tmc.domain.Course;
import fi.helsinki.cs.plugin.tmc.io.IO;
import fi.helsinki.cs.plugin.tmc.services.http.jsonhelpers.CourseList;
import fi.helsinki.cs.plugin.tmc.ui.UserVisibleException;

public class LocalCourseStorageTest {

    private IO io;
    private CourseStorage lcs;

    @Before
    public void setUp() {
        this.io = mock(IO.class);
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

    private CourseList buildMockCourseList() {
        Course c1 = new Course("c1");
        Course[] cl = {c1};
        CourseList courseList = new CourseList();
        courseList.setCourses(cl);
        courseList.setApiVersion("7");
        return courseList;
    }

}
