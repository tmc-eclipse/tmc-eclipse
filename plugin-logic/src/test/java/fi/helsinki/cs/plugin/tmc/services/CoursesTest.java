package fi.helsinki.cs.plugin.tmc.services;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import fi.helsinki.cs.plugin.tmc.domain.Course;
import fi.helsinki.cs.plugin.tmc.io.FileIO;
import fi.helsinki.cs.plugin.tmc.storage.CourseDAO;
import fi.helsinki.cs.plugin.tmc.storage.LocalCourseStorage;
import fi.helsinki.cs.plugin.tmc.ui.UserVisibleException;

public class CoursesTest {
    private Courses courses;
    private CourseDAO courseDAO;

    private List<Course> courseList = new ArrayList<Course>();
    private Course c1 = new Course("c1");
    private Course c2 = new Course("c2");
    private Course c3 = new Course("c3");

    @Before
    public void setUp() throws UserVisibleException {
        courseDAO = mock(CourseDAO.class);

        courseList.add(c1);
        courseList.add(c2);
        courseList.add(c3);

        when(courseDAO.load()).thenReturn(courseList);

        courses = new Courses(courseDAO);
    }

    @Test
    public void constructorLoadsCourses() {
        new Courses(courseDAO);
        verify(courseDAO, times(2)).load(); // first in setup, then here
    }

    @Test
    public void getCoursesReturnCorrectList() {
        assertEquals(3, courses.getCourses().size());
        assertTrue(courses.getCourses().contains(c1));
        assertTrue(courses.getCourses().contains(c2));
        assertTrue(courses.getCourses().contains(c3));
    }

    @Test
    public void loadCoursesRefreshesFromDAO() {
        courses.loadCourses();
        verify(courseDAO, times(2)).load(); // first in setup, then here
    }

    @Test
    public void setCourseListSavesToDAO() {
        List<Course> newList = new ArrayList<Course>();
        courses.setCourses(newList);
        verify(courseDAO, times(1)).save(newList);
        assertEquals(0, courses.getCourses().size());
    }

    @Test
    public void updateCourseSavesToDAO() {
        courses.updateCourse(new Course("c1"));
        verify(courseDAO, times(1)).save(any(List.class));
    }

    @Test
    public void updateCourseOverwritesCourseWithSameName() {
        c1.setId(1337);
        courses.updateCourse(new Course("c2"));
        assertTrue(1337 != courses.getCourseByName("c2").getId());
    }

    @Test
    public void updateCourseDoesNotOverwriteIfNoCourseWithSameNameIsFound() {
        courses.updateCourse(new Course("c15"));
        assertNull(courses.getCourseByName("c15"));
    }

    @Test
    public void getCourseByNameReturnsCorrectCourseWhenPresent() {
        assertEquals(c2, courses.getCourseByName("c2"));
    }

    @Test
    public void getCoursesByNameReturnsNullIfNoSuchCoursePresent() {
        assertNull(courses.getCourseByName("asd"));
    }

    @Test
    public void getCoursesByNameReturnsNullIfNoCourses() {
        courses.setCourses(new ArrayList<Course>());
        assertNull(courses.getCourseByName("c1"));
    }

}
