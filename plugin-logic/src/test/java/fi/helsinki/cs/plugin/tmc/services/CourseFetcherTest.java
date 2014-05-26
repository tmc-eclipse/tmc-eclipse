package fi.helsinki.cs.plugin.tmc.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import fi.helsinki.cs.plugin.tmc.Core;
import fi.helsinki.cs.plugin.tmc.TMCErrorHandler;
import fi.helsinki.cs.plugin.tmc.domain.Course;
import fi.helsinki.cs.plugin.tmc.services.http.ServerManager;
import fi.helsinki.cs.plugin.tmc.ui.UserVisibleException;

public class CourseFetcherTest {
    private ServerManager server;
    private CourseDAO courseDAO;
    private CourseFetcher courseFetcher;

    @Before
    public void setup() {
        server = mock(ServerManager.class);
        courseDAO = mock(CourseDAO.class);
        courseFetcher = new CourseFetcher(server, courseDAO);
    }

    @Test
    public void updateCoursesFetchesDataFromWebDAOAndInputsDataToCourses() {
        List<Course> courseList = new ArrayList<Course>();
        when(server.getCourses()).thenReturn(courseList);

        courseFetcher.updateCourses();

        verify(server, times(1)).getCourses();
        verifyNoMoreInteractions(server);

        verify(courseDAO, times(1)).setCourses(courseList);
        verifyNoMoreInteractions(courseDAO);
    }

    @Test
    public void updateCoursesPassesExceptiontoErrorHandlerAndStopsExecution() {
        TMCErrorHandler errorHandler = mock(TMCErrorHandler.class);
        Core.setErrorHandler(errorHandler);

        when(server.getCourses()).thenThrow(new UserVisibleException("mock"));
        courseFetcher.updateCourses();

        // Do not update courselist on exception
        verifyNoMoreInteractions(courseDAO);

        // Pass error to ErrorHandler
        verify(errorHandler, times(1)).handleException(any(Exception.class));
    }

    @Test
    public void getCourseNamesReturnArrayContainingAllCourseNames() {
        List<Course> mockCourseList = new ArrayList<Course>();
        mockCourseList.add(new Course("course1"));
        mockCourseList.add(new Course("course2"));
        mockCourseList.add(new Course("course3"));

        when(courseDAO.getCourses()).thenReturn(mockCourseList);
        String[] courseNames = courseFetcher.getCourseNames();

        assertTrue(Arrays.asList(courseNames).contains("course1"));
        assertTrue(Arrays.asList(courseNames).contains("course2"));
        assertTrue(Arrays.asList(courseNames).contains("course3"));
        assertEquals(3, courseNames.length);
    }

}
