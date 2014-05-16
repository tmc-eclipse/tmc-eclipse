package fi.helsinki.cs.plugins.tmc.domain;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import java.util.Calendar;
import java.util.Date;
import java.util.prefs.Preferences;

import org.junit.Before;
import org.junit.Test;

import fi.helsinki.cs.plugin.tmc.domain.Exercise;
import fi.helsinki.cs.plugin.tmc.services.Settings;

public class ExcerciseTest {

	Exercise exercise;
	
	@Before
	public void setUp() {
		exercise = new Exercise();
	}
	/*
	 * 	public Exercise(String name) {
		this(name, "unknown-course");
	}

	public Exercise(String name, String courseName) {
		this.name = name;
		this.courseName = courseName;
	}

	 * */
	
	@Test
	public void constructorSetsNameFieldCorrectly() {
		exercise = new Exercise("test name");
		assertEquals("test name", exercise.getName());
	}
	
	@Test
	public void constructorSetsNameAndCourseNameFieldSCorrectly() {
		exercise = new Exercise("test name", "test course name");
		assertEquals("test name", exercise.getName());
		assertEquals("test course name", exercise.getCourseName());
	}
	
	
	@Test(expected=NullPointerException.class)
	public void setNameThrowsIfParameterIsNull() {
		exercise.setName(null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void setNameThrowsIfParameterIsEmpty() {
		exercise.setName("");
	}
		
	@Test
	public void setNameWithValidParameterWorks() {
		exercise.setName("name 01");
		assertEquals("name 01", exercise.getName());
	}
	
	@Test(expected=NullPointerException.class)
	public void hasDeadlinePassedAtThrowsIfParameterIsNull() {
		exercise.hasDeadlinePassedAt(null);		
	}
	
	@Test
	public void hasDeadlinePassedAtReturnsTrueIfDeadlineHasPassed() {
		setPassedDeadline();
		assertTrue(exercise.hasDeadlinePassedAt(new Date()));		
	}

	private void setPassedDeadline() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		exercise.setDeadline(cal.getTime());
	}
	

	@Test
	public void hasDeadlinePassedAtReturnsFalseIfDeadlineHasNotPassed() {
		setNonPassedDeadline();
		assertFalse(exercise.hasDeadlinePassedAt(new Date()));		
	}

	private void setNonPassedDeadline() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, +1);
		exercise.setDeadline(cal.getTime());
	}
	
	@Test
	public void hasDeadlinePassedAtReturnsFalseIfNoDeadlineIsSet() {
		assertFalse(exercise.hasDeadlinePassedAt(new Date()));		
	}
	
	
	@Test(expected=NullPointerException.class)
	public void setDownloadUrlThrowsIfParameterIsNull() {
		exercise.setDownloadUrl(null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void setDownloadUrlThrowsIfParameterIsEmpty() {
		exercise.setDownloadUrl("");
	}
	
	@Test
	public void setDownloadUrlWithValidParameterWorks() {
		exercise.setDownloadUrl("url 01");
		assertEquals("url 01", exercise.getDownloadUrl());
	}
	
	@Test(expected=NullPointerException.class)
	public void setReturnUrlThrowsIfParameterIsNull() {
		exercise.setReturnUrl(null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void setReturnUrlThrowsIfParameterIsEmpty() {
		exercise.setReturnUrl("");
	}
	
	@Test
	public void setReturnUrlWithValidParameterWorks() {
		exercise.setReturnUrl("url 01");
		assertEquals("url 01", exercise.getReturnUrl());
	}

	@Test
	public void isReturnableReturnsTrueIfIsReturnableAndDeadlineHasNotPassed() {
		setNonPassedDeadline();
		exercise.setReturnable(true);
		assertTrue(exercise.isReturnable());
	}
	
	@Test
	public void isReturnableReturnsFalseIfIsReturnableAndDeadlineHasPassed() {
		setPassedDeadline();
		exercise.setReturnable(true);
		assertFalse(exercise.isReturnable());
	}
	
	@Test
	public void isReturnableReturnsFalseIfIsNotReturnableAndDeadlineHasNotPassed() {
		setNonPassedDeadline();
		exercise.setReturnable(false);
		assertFalse(exercise.isReturnable());
	}
	
	@Test
	public void isReturnableReturnsFalseIfIsNotReturnableAndDeadlineHasPassed() {
		setPassedDeadline();
		exercise.setReturnable(false);
		assertFalse(exercise.isReturnable());
	}
}


