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
	
	@Before
	public void setUp() {
		lcs=new LocalCourseStorage(new File("testi.txt"));
    	Courses c=new Courses(lcs);
    	
    	Course c1=new Course("c1");
    	Course c2=new Course("c2");
    	Course c3=new Course("c3");
    	Course c4=new Course("c4");
    	
    	ArrayList<Course> cl=new ArrayList<>();
    	
    	cl.add(c1);
    	cl.add(c2);
    	cl.add(c3);
    	cl.add(c4);
    	
    	c.setCourses(cl);
	}
	
	
	

}
