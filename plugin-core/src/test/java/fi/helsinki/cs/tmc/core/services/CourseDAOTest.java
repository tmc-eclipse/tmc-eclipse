package fi.helsinki.cs.tmc.core.services;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import fi.helsinki.cs.tmc.core.domain.Course;
import fi.helsinki.cs.tmc.core.services.CourseDAO;
import fi.helsinki.cs.tmc.core.storage.DataSource;

public class CourseDAOTest {
    private CourseDAO dao;
    private DataSource<Course> source;
    private List<Course> courseList;

    @Before
    public void setUp() throws Exception {
        this.courseList = new ArrayList<>();
        this.source = new TestCourseDataSource();
        this.dao = new CourseDAO(source);

        courseList.add(new Course("course1"));
        courseList.add(new Course("course2"));
        courseList.add(new Course("course3"));
        courseList.add(new Course("course4"));
        courseList.add(new Course("course5"));

        source.save(courseList);
        dao.setCourses(courseList);
    }

    @Test
    public void testGetCourses() {
        assertEquals(5, dao.getCourses().size());
    }

    @Test
    public void testSetCourses() {
        List<Course> l = new ArrayList<>();
        l.add(new Course("aaa"));

        dao.setCourses(l);

        assertEquals(1, dao.getCourses().size());
    }

    @Test
    public void testUpdating() {
        Course c = new Course("course3");
        c.setId(42);

        dao.updateCourse(c);

        assertEquals(42, dao.getCourseByName("course3").getId());
        assertEquals(5, dao.getCourses().size());
    }

    @Test
    public void gettingNonExistentCourse() {
        assertEquals(null, dao.getCourseByName("asdasdasd"));
    }

    public class TestCourseDataSource implements DataSource<Course> {
        List<Course> list;

        @Override
        public List<Course> load() {
            return list;
        }

        @Override
        public void save(List<Course> elements) {
            elements = list;
        }

    }
}
