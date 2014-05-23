package fi.helsinki.cs.plugin.tmc.services.http;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

import fi.helsinki.cs.plugin.tmc.domain.Course;
import fi.helsinki.cs.plugin.tmc.domain.Exercise;
import fi.helsinki.cs.plugin.tmc.domain.ZippedProject;
import fi.helsinki.cs.plugin.tmc.services.http.jsonhelpers.CourseList;
import fi.helsinki.cs.plugin.tmc.services.http.jsonhelpers.ExerciseList;

public class ServerManagerTest {
    private ConnectionBuilder connectionBuilder;
    private Gson gson;
    private ServerManager server;

    @Before
    public void setup() {
        connectionBuilder = mock(ConnectionBuilder.class);
        gson = new Gson();
        server = new ServerManager(gson, connectionBuilder);
    }

    @Test
    public void getCoursesReturnsValidCoursesOnSuccefullHttpRequest() throws Exception {
        CourseList cl = buildMockCourseList();
        String mockJson = gson.toJson(cl);

        RequestBuilder rb = mock(RequestBuilder.class);
        when(connectionBuilder.createConnection()).thenReturn(rb);
        when(rb.getForText(any(String.class))).thenReturn(mockJson);

        List<Course> returnedCourses = server.getCourses();

        for (int i = 1; i < cl.getCourses().length; i++) {
            Course c = cl.getCourses()[i];
            boolean found = false;
            for (Course returned : returnedCourses) {
                if (returned.getName().equals(c.getName())) {
                    found = true;
                }
            }
            assertTrue("Didn't find all courses that should have been present, based on the JSON", found);
        }

        assertEquals(cl.getCourses().length, returnedCourses.size());
        assertEquals("7", cl.getApiVersion());
    }

    @Test
    public void getcoursesReturnEmptyListOnFailure() throws Exception {
        RequestBuilder rb = mock(RequestBuilder.class);
        when(connectionBuilder.createConnection()).thenReturn(rb);
        when(rb.getForText(any(String.class))).thenReturn("");

        assertEquals(0, server.getCourses().size());
    }

    @Test
    public void getExercisesReturnsValidObjectOnSuccesfullHttpRequest() throws Exception {
        ExerciseList el = buildMockExerciseList();
        String mockJson = gson.toJson(el);

        RequestBuilder rb = mock(RequestBuilder.class);
        when(connectionBuilder.createConnection()).thenReturn(rb);
        when(rb.getForText(any(String.class))).thenReturn(mockJson);

        List<Exercise> returned = server.getExercises("11");

        for (Exercise orig : el.getExercises()) {
            String name = orig.getName();
            boolean found = false;
            for (Exercise ret : returned) {
                if (ret.getName().equals(orig.getName())) {
                    found = true;
                }
            }
            assertTrue("The returned list of exercises should contain all exercises of the original list", found);
        }
    }

    @Test
    public void getExerciseZipShouldReturnTheByteArrayGottenFromTheHttpRequest() throws Exception {
        byte[] byteArray = {1, 0, 1};
        RequestBuilder rb = mock(RequestBuilder.class);
        when(connectionBuilder.createConnection()).thenReturn(rb);
        when(rb.getForBinary(any(String.class))).thenReturn(byteArray);

        ZippedProject returned = server.getExerciseZip("url");
        assertEquals(byteArray, returned.getBytes());
    }

    @Test
    public void getExerciseZipShouldReturnEmptyByteArrayOnFailedRequest() throws Exception {
        RequestBuilder rb = mock(RequestBuilder.class);
        when(connectionBuilder.createConnection()).thenReturn(rb);
        when(rb.getForBinary(any(String.class))).thenThrow(new IOException());

        assertEquals(0, server.getExerciseZip("url").getBytes().length);
    }

    private CourseList buildMockCourseList() {
        Course c1 = new Course("c1");
        Course c2 = new Course("c2");
        Course c3 = new Course("c3");
        Course[] cl = {c1, c2, c3};
        CourseList courseList = new CourseList();
        courseList.setCourses(cl);
        courseList.setApiVersion("7");
        return courseList;
    }

    private ExerciseList buildMockExerciseList() {
        Exercise e1 = new Exercise("e1");
        Exercise e2 = new Exercise("e2");
        Exercise e3 = new Exercise("e3");
        List<Exercise> exercises = new ArrayList<Exercise>();
        exercises.add(e1);
        exercises.add(e2);
        exercises.add(e3);

        Course course = new Course("c1");
        course.setExercises(exercises);

        ExerciseList eList = new ExerciseList();
        eList.setCourse(course);
        return eList;
    }

}
