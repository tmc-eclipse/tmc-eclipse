package fi.helsinki.cs.tmc.core.services;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import fi.helsinki.cs.tmc.core.domain.Course;
import fi.helsinki.cs.tmc.core.services.DomainUtil;

public class DomainUtilTest {

    private List<Course> courses;

    @Before
    public void setUp() {
        courses = new ArrayList<Course>();

        courses.add(new Course("course1"));
        courses.add(new Course("course2"));
        courses.add(new Course("course3"));
    }

    @Test
    public void testGetCourseNames() {
        String[] names = DomainUtil.getCourseNames(courses);
        assertEquals(names.length, courses.size());

        for (int i = 0; i < names.length; i++) {
            assertEquals(names[i], courses.get(i).getName());
        }
    }

    @Test
    public void testGetCourseNamesWithEmptyList() {
        String[] names = DomainUtil.getCourseNames(new ArrayList<Course>());
        assertEquals(names.length, 0);
    }

}
