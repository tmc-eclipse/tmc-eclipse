package fi.helsinki.cs.plugin.tmc.services.web;

import static org.junit.Assert.*;

import org.junit.Test;

public class UrlExtensionTest {

	@Test
	public void coursesReturnsCorrectUrl(){
		assertEquals("courses.json", UrlExtension.COURSES.getExtension());
	}
	
	@Test
	public void exercisesReturnsCorrectUrl(){
		assertEquals("courses/1.json", UrlExtension.EXERCISES.getExtension("1"));
	}

}
