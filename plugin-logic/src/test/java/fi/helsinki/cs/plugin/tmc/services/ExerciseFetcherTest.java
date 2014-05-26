package fi.helsinki.cs.plugin.tmc.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
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
import fi.helsinki.cs.plugin.tmc.domain.Exercise;
import fi.helsinki.cs.plugin.tmc.services.http.ServerManager;
import fi.helsinki.cs.plugin.tmc.ui.UserVisibleException;

public class ExerciseFetcherTest {
    private ServerManager webDAO;
    private Courses courses;
    private TMCErrorHandler errorHandler;
    private ExerciseFetcher fetcher;

    @Before
    public void setup() {
        webDAO = mock(ServerManager.class);
        courses = mock(Courses.class);
        errorHandler = mock(TMCErrorHandler.class);
        Core.setErrorHandler(errorHandler);

        fetcher = new ExerciseFetcher(courses, webDAO);
    }

    @Test
    public void updateExercisesForCurrentCourseCallsWebDAO() {
        List<Exercise> exerciseList = new ArrayList<Exercise>();
        when(courses.getCourseByName(any(String.class))).thenReturn(new Course("kurssi"));
        when(webDAO.getExercises(any(String.class))).thenReturn(exerciseList);

        fetcher.updateExercisesForCurrentCourse();

        verify(webDAO, times(1)).getExercises(any(String.class));
        verifyNoMoreInteractions(webDAO);

        assertEquals(exerciseList, fetcher.getExercisesForCurrentCourse());
    }

    @Test
    public void updateExerciseExceptionIsPassedToCoreErrorHandler() {
        when(courses.getCourseByName(any(String.class))).thenReturn(new Course("kurssi"));

        UserVisibleException mockException = new UserVisibleException("mock");
        when(webDAO.getExercises(any(String.class))).thenThrow(mockException);

        fetcher.updateExercisesForCurrentCourse();

        verify(errorHandler, times(1)).handleException(mockException);
    }

    @Test
    public void getExerciseByNameReturnCorrectWhenPresent() {
        Exercise e1 = new Exercise("e1", "c1");
        Exercise e2 = new Exercise("e2", "c1");
        Exercise e3 = new Exercise("e3", "c1");

        List<Exercise> exerciseList = new ArrayList<Exercise>();
        exerciseList.add(e1);
        exerciseList.add(e2);
        exerciseList.add(e3);

        Course course = new Course("c1");

        when(courses.getCourseByName(any(String.class))).thenReturn(course);
        when(webDAO.getExercises(any(String.class))).thenReturn(exerciseList);

        fetcher.updateExercisesForCurrentCourse();

        assertEquals(e1, fetcher.getExerciseByName("e1"));
        assertEquals(e2, fetcher.getExerciseByName("e2"));
        assertEquals(e3, fetcher.getExerciseByName("e3"));
    }

    @Test
    public void getExerciseByNameReturnNullWhenNotPresent() {
        Exercise e1 = new Exercise("e1", "c1");
        Exercise e2 = new Exercise("e2", "c1");
        Exercise e3 = new Exercise("e3", "c1");

        List<Exercise> exerciseList = new ArrayList<Exercise>();
        exerciseList.add(e1);
        exerciseList.add(e2);
        exerciseList.add(e3);

        Course course = new Course("c1");

        when(courses.getCourseByName(any(String.class))).thenReturn(course);
        when(webDAO.getExercises(any(String.class))).thenReturn(exerciseList);

        fetcher.updateExercisesForCurrentCourse();

        assertNull(fetcher.getExerciseByName("asd"));
    }

    @Test
    public void getExerciseNamesReturnCorrectValues() {
        Exercise e1 = new Exercise("e1", "c1");
        Exercise e2 = new Exercise("e2", "c1");
        Exercise e3 = new Exercise("e3", "c1");

        List<Exercise> exerciseList = new ArrayList<Exercise>();
        exerciseList.add(e1);
        exerciseList.add(e2);
        exerciseList.add(e3);

        Course course = new Course("c1");

        when(courses.getCourseByName(any(String.class))).thenReturn(course);
        when(webDAO.getExercises(any(String.class))).thenReturn(exerciseList);

        fetcher.updateExercisesForCurrentCourse();

        assertTrue(Arrays.asList(fetcher.getCurrentCoursesExerciseNames()).contains("e1"));
        assertTrue(Arrays.asList(fetcher.getCurrentCoursesExerciseNames()).contains("e2"));
        assertTrue(Arrays.asList(fetcher.getCurrentCoursesExerciseNames()).contains("e3"));
        assertEquals(3, fetcher.getCurrentCoursesExerciseNames().length);
    }

}
