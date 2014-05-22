package fi.helsinki.cs.plugin.tmc.domain;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class CourseTest {

    private Course course;

    @Before
    public void setUp() {
        course = new Course();
    }

    @Test
    public void testGetSpywareUrlsDoesntReturnNull() {
        assertNotNull(course.getSpywareUrls());
    }

    @Test
    public void testGetUnlockablesDoesntReturnNull() {
        assertNotNull(course.getUnlockables());
    }

    @Test
    public void testGetExercisesDoesntReturnNull() {
        assertNotNull(course.getExercises());
    }

    @Test
    public void testId() {
        course.setId(1);
        assertEquals(1, course.getId());
    }

    @Test
    public void testName() {
        course.setName("name");
        assertEquals("name", course.getName());
    }

    @Test
    public void testDetailsUrl() {
        course.setDetailsUrl("url");
        assertEquals("url", course.getDetailsUrl());
    }

    @Test
    public void testUnlockUrl() {
        course.setUnlockUrl("url");
        assertEquals("url", course.getUnlockUrl());
    }

    @Test
    public void testReviewUrl() {
        course.setReviewsUrl("url");
        assertEquals("url", course.getReviewsUrl());
    }

    @Test
    public void testCometUrl() {
        course.setCometUrl("url");
        assertEquals("url", course.getCometUrl());
    }

    @Test
    public void testSpywareUrl() {
        List<String> spywareUrls = new ArrayList<String>();
        course.setSpywareUrls(spywareUrls);
        assertEquals(spywareUrls, course.getSpywareUrls());
    }

    @Test
    public void testExerciseLoaded() {
        course.setExercisesLoaded(true);
        assertEquals(true, course.isExercisesLoaded());
    }

    @Test
    public void testUnlockables() {
        List<String> unlockables = new ArrayList<String>();
        course.setUnlockables(unlockables);
        assertEquals(unlockables, course.getUnlockables());
    }

    @Test
    public void testExercises() {
        List<Exercise> exercises = new ArrayList<Exercise>();
        course.setExercises(exercises);
        assertEquals(exercises, course.getExercises());
    }

    @Test
    public void toStringReturnsName() {
        course.setName("CourseName");
        assertEquals("CourseName", course.toString());
    }

}
