package fi.helsinki.cs.plugin.tmc.services;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import fi.helsinki.cs.plugin.tmc.domain.Course;
import fi.helsinki.cs.plugin.tmc.io.FileIO;
import fi.helsinki.cs.plugin.tmc.storage.LocalCourseStorage;
import fi.helsinki.cs.plugin.tmc.ui.UserVisibleException;

public class CoursesTest {

	private LocalCourseStorage lcs;
	private Courses courses;
	private Course c1;
	private Course c2;
	private Course c3;
	private Course c4;
	ArrayList<Course> cl;
	
	@Before
	public void setUp() throws UserVisibleException {
		lcs = new LocalCourseStorage(new FileIO("testi.txt"));
    	courses = new Courses(lcs);
    	
    	c1 = new Course("c1");
    	c2 = new Course("c2");
    	c3 = new Course("c3");
    	c4 = new Course("c4");
    	
    	cl = new ArrayList<>();
    	
    	cl.add(c1);
    	cl.add(c2);
    	cl.add(c3);
    	cl.add(c4);
    	
    	courses.setCourses(cl);
	}
	

	@Test
	public void testgetCourseByNameSuccess() {
		assertEquals(courses.getCourseByName("c1"), c1);
	}
	
	@Test
	public void testgetCourseByNameFail() {
		assertNull(courses.getCourseByName("c5"));
	}
	
	@Test
	public void testGetCourses() {
		assertEquals(courses.getCourses(), cl);
	}
	
	@Test
	public void testUpdateCourse() throws UserVisibleException {
		Course c5 =new Course("c4");
		c5.setCometUrl("asd");
		
		courses.updateCourse(c5);
		assertEquals(courses.getCourseByName("c4").getCometUrl(), "asd");
	}
	
	@Test
	public void testSetCourses() throws UserVisibleException {
		cl.remove(3);
		courses.setCourses(cl);
		assertEquals(courses.getCourses(), cl);
	}

}
