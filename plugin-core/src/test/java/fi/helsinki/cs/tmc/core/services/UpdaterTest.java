package fi.helsinki.cs.tmc.core.services;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import fi.helsinki.cs.tmc.core.domain.Course;
import fi.helsinki.cs.tmc.core.domain.Exercise;
import fi.helsinki.cs.tmc.core.services.CourseDAO;
import fi.helsinki.cs.tmc.core.services.ProjectDAO;
import fi.helsinki.cs.tmc.core.services.Updater;
import fi.helsinki.cs.tmc.core.services.http.ServerManager;

public class UpdaterTest {

    private ServerManager server;
    private CourseDAO courseDAO;
    private ProjectDAO projectDAO;

    private Updater updater;

    @Before
    public void setUp() {
        server = mock(ServerManager.class);
        courseDAO = mock(CourseDAO.class);
        projectDAO = mock(ProjectDAO.class);

        updater = new Updater(server, courseDAO, projectDAO);
    }

    @Test
    public void testUpdateCoursesCallsDAOandServer() {
        updater.updateCourses();

        verify(server, times(1)).getCourses();
        verify(courseDAO, times(1)).getCourses();

        verify(courseDAO, times(1)).setCourses(any(List.class));
    }

    @Test
    public void testUpdateCoursesUpdatesDAO() {
        List<Exercise> serverExerciseList = new ArrayList<Exercise>();
        Exercise serverExercise = new Exercise("serverExercise", "serverCourse");
        serverExerciseList.add(serverExercise);

        List<Course> serverCourseList = new ArrayList<Course>();
        Course serverCourse = new Course("serverCourse");
        serverCourseList.add(serverCourse);

        when(server.getCourses()).thenReturn(serverCourseList);
        when(courseDAO.getCourses()).thenReturn(new ArrayList<Course>());

        updater.updateCourses();

        verify(courseDAO, times(1)).setCourses(any(List.class));
    }

    @Test
    public void testUpdateExercisesCallsCourseAndServer() {
        Course course = mock(Course.class);
        when(course.getId()).thenReturn(1);
        when(course.getExercises()).thenReturn(new ArrayList<Exercise>());

        updater.updateExercises(course);

        verify(server, times(1)).getExercises("1");
        verify(course, times(1)).getExercises();

    }
}
