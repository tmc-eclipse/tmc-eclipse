package fi.helsinki.cs.plugin.tmc.domain;

import static org.junit.Assert.*;

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

}
