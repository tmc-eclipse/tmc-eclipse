package fi.helsinki.cs.plugin.tmc.services;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import java.io.File;
import java.util.ArrayList;
import java.util.prefs.Preferences;

import org.junit.Before;
import org.junit.Test;

import fi.helsinki.cs.plugin.tmc.domain.Course;
import fi.helsinki.cs.plugin.tmc.storage.LocalCourseStorage;

public class CoursesTest {

	private LocalCourseStorage lcs;
	private Courses c;
	private Course c1;
	private Course c2;
	private Course c3;
	private Course c4;
	ArrayList<Course> cl;
	
	@Before
	public void setUp() {
		lcs=new LocalCourseStorage(new File("testi.txt"));
    	c=new Courses(lcs);
    	
    	c1=new Course("c1");
    	c2=new Course("c2");
    	c3=new Course("c3");
    	c4=new Course("c4");
    	
    	cl=new ArrayList<>();
    	
    	cl.add(c1);
    	cl.add(c2);
    	cl.add(c3);
    	cl.add(c4);
    	
    	c.setCourses(cl);
	}
	

	@Test
	public void testCourses(){
		
	}
	
	@Test
	public void testGetCourses(){
		assertEquals(c.getCourses(), cl);
	}
	

	@Test
	public void testSetCourses(){
		cl.remove(3);
		c.setCourses(cl);
		assertEquals(c.getCourses(), cl);
	}

}
