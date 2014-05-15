package fi.helsinki.cs.plugin.tmc.services;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import fi.helsinki.cs.plugin.tmc.domain.Course;
import fi.helsinki.cs.plugin.tmc.io.FileIO;
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
		lcs=new LocalCourseStorage(new FileIO("testi.txt"));
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
	public void testgetCourseByNameSuccess(){
		assertEquals(c.getCourseByName("c1"), c1);
	}
	
	@Test
	public void testgetCourseByNameFail(){
		assertNull(c.getCourseByName("c5"));
	}
	
	@Test
	public void testGetCourses(){
		assertEquals(c.getCourses(), cl);
	}
	
	@Test
	public void testUpdateCourse(){
		Course c5 =new Course("c4");
		c5.setCometUrl("asd");
		
		c.updateCourse(c5);
		assertEquals(c.getCourseByName("c4").getCometUrl(), "asd");
	}
	
	@Test
	public void testSetCourses(){
		cl.remove(3);
		c.setCourses(cl);
		assertEquals(c.getCourses(), cl);
	}

}
