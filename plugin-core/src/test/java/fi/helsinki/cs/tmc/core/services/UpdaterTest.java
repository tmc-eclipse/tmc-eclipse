package fi.helsinki.cs.tmc.core.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
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
import fi.helsinki.cs.tmc.core.domain.Project;
import fi.helsinki.cs.tmc.core.services.http.ServerManager;
import fi.helsinki.cs.tmc.core.ui.UserVisibleException;

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

    @Test
    public void newCourseExercisesAreSetCorrectlyAsOldExercises() {

        List<Course> serverCourseList = new ArrayList<Course>();
        Course serverCourse = new Course("course_name");
        serverCourseList.add(serverCourse);
        when(server.getCourses()).thenReturn(serverCourseList);

        List<Course> daoCourseList = new ArrayList<Course>();
        Course daoCourse = new Course("course_name");

        List<Exercise> exercises = new ArrayList<Exercise>();
        exercises.add(new Exercise("foo", "bar"));
        exercises.get(0).setCourse(daoCourse);
        daoCourse.setExercises(exercises);
        daoCourseList.add(daoCourse);

        when(courseDAO.getCourses()).thenReturn(daoCourseList);

        updater.updateCourses();
        assertEquals(exercises, serverCourse.getExercises());
    }

    @Test
    public void newCourseExercisesHaveCorrectParentCourse() {

        List<Course> serverCourseList = new ArrayList<Course>();
        Course serverCourse = new Course("course_name");
        serverCourseList.add(serverCourse);
        when(server.getCourses()).thenReturn(serverCourseList);

        List<Course> daoCourseList = new ArrayList<Course>();
        Course daoCourse = new Course("course_name");

        List<Exercise> exercises = new ArrayList<Exercise>();
        exercises.add(new Exercise("foo", "bar"));
        exercises.get(0).setCourse(daoCourse);
        daoCourse.setExercises(exercises);
        daoCourseList.add(daoCourse);

        when(courseDAO.getCourses()).thenReturn(daoCourseList);

        updater.updateCourses();

        assertTrue(exercises.get(0).getCourse() == serverCourse);
    }

    @Test
    public void newCourseExercisesAreNotOverriddenIfOldCourseExercisesAreNull() {

        List<Course> serverCourseList = new ArrayList<Course>();
        Course serverCourse = new Course("course_name");
        serverCourseList.add(serverCourse);
        when(server.getCourses()).thenReturn(serverCourseList);

        List<Course> daoCourseList = new ArrayList<Course>();
        Course daoCourse = new Course("course_name");
        daoCourseList.add(daoCourse);
        daoCourse.setExercises(null);
        List<Exercise> exercises = new ArrayList<Exercise>();

        exercises.add(new Exercise("foo", "bar"));
        exercises.get(0).setCourse(serverCourse);

        serverCourse.setExercises(exercises);

        when(courseDAO.getCourses()).thenReturn(daoCourseList);

        updater.updateCourses();

        assertEquals(exercises, serverCourse.getExercises());
    }

    @Test
    public void newCourseExercisesAreNotOverriddenIfOldCourseIsNotEqual() {

        List<Course> serverCourseList = new ArrayList<Course>();
        Course serverCourse = new Course("course_name");
        serverCourseList.add(serverCourse);
        when(server.getCourses()).thenReturn(serverCourseList);

        List<Course> daoCourseList = new ArrayList<Course>();
        Course daoCourse = new Course("different_course_name");
        daoCourseList.add(daoCourse);

        List<Exercise> exercises = new ArrayList<Exercise>();

        exercises.add(new Exercise("foo", "bar"));
        exercises.get(0).setCourse(serverCourse);

        serverCourse.setExercises(exercises);

        when(courseDAO.getCourses()).thenReturn(daoCourseList);

        updater.updateCourses();

        assertEquals(exercises, serverCourse.getExercises());
    }

    @Test(expected = UserVisibleException.class)
    public void exceptionIsThrownIfCourseIsNull() {
        updater.updateExercises(null);
    }

    @Test
    public void exerciseIsUpdatedWhenThereIsOldExerciseForCourse() {
        Project project = mock(Project.class);

        Course course = new Course("foo");
        course.setId(1);

        List<Exercise> exercises = new ArrayList<Exercise>();
        exercises.add(new Exercise("foo", "bar"));
        exercises.add(new Exercise("baz", "qux"));
        exercises.get(1).setChecksum("1234");
        exercises.get(1).setUpdated(false);
        exercises.get(1).setCourse(course);
        exercises.get(1).setProject(project);

        course.setExercises(exercises);

        List<Exercise> serverExercises = new ArrayList<Exercise>();
        serverExercises.add(new Exercise("baz", "qux"));
        when(server.getExercises("1")).thenReturn(serverExercises);

        updater.updateExercises(course);
        assertEquals(exercises.get(1).getChecksum(), serverExercises.get(0).getOldChecksum());
        assertEquals(exercises.get(1).getUpdated(), serverExercises.get(0).getUpdated());
        assertEquals(exercises.get(1).getCourse(), serverExercises.get(0).getCourse());
        assertEquals(exercises.get(1).getProject(), serverExercises.get(0).getProject());
    }

    @Test
    public void projectHasCorrectExerciseSet() {
        Project project = mock(Project.class);

        Course course = new Course("foo");
        course.setId(1);

        List<Exercise> exercises = new ArrayList<Exercise>();
        exercises.add(new Exercise("foo", "bar"));
        exercises.add(new Exercise("baz", "qux"));
        exercises.get(1).setChecksum("1234");
        exercises.get(1).setUpdated(false);
        exercises.get(1).setCourse(course);
        exercises.get(1).setProject(project);

        course.setExercises(exercises);

        List<Exercise> serverExercises = new ArrayList<Exercise>();
        serverExercises.add(new Exercise("baz", "qux"));

        when(server.getExercises("1")).thenReturn(serverExercises);
        when(projectDAO.getProjectByExercise(exercises.get(1))).thenReturn(project);

        updater.updateExercises(course);
        verify(project, times(1)).setExercise(serverExercises.get(0));
    }

    @Test
    public void serverExerciseIsNotModifiedIfNoLocalMatchIsPresent() {
        Project project = mock(Project.class);

        Course course = new Course("foo");
        course.setId(1);

        List<Exercise> exercises = new ArrayList<Exercise>();
        exercises.add(new Exercise("foo", "bar"));

        course.setExercises(exercises);

        List<Exercise> serverExercises = new ArrayList<Exercise>();
        serverExercises.add(new Exercise("baz", "qux"));

        serverExercises.get(0).setOldChecksum("1234");
        serverExercises.get(0).setUpdated(false);
        serverExercises.get(0).setCourse(course);
        serverExercises.get(0).setProject(project);

        when(server.getExercises("1")).thenReturn(serverExercises);

        updater.updateExercises(course);

        assertEquals("1234", serverExercises.get(0).getOldChecksum());
        assertEquals(false, serverExercises.get(0).getUpdated());
        assertEquals(course, serverExercises.get(0).getCourse());
        assertEquals(project, serverExercises.get(0).getProject());
    }

}
