package fi.helsinki.cs.plugin.tmc.services;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import org.junit.Before;
import org.junit.Test;

public class SettingsTest {

	Preferences prefs;
	Settings settings;
	
	@Before
	public void setUp() {
		prefs = mock(Preferences.class);
		settings = new Settings(prefs);
	}
	
	@Test
	public void currentCourseReturnsCorrectValueWhenSet(){
		when(prefs.get(Settings.PREF_KEY_CURRENT_COURSE, "")).thenReturn("correct");
		assertEquals("correct", settings.getCurrentCourseName());
		verify(prefs, times(1)).get(Settings.PREF_KEY_CURRENT_COURSE, "");
		verifyNoMoreInteractions(prefs);
	}
	
	@Test
	public void serverBaseUrlReturnsCorrectValueWhenSet(){
		when(prefs.get(Settings.PREF_KEY_TMC_SERVER_URL, "")).thenReturn("correct");
		assertEquals("correct", settings.getServerBaseUrl());
		verify(prefs, times(1)).get(Settings.PREF_KEY_TMC_SERVER_URL, "");
		verifyNoMoreInteractions(prefs);
	}
	
	@Test
	public void usernameReturnsCorrectValueWhenSet(){
		when(prefs.get(Settings.PREF_KEY_USERNAME, "")).thenReturn("correct");
		assertEquals("correct", settings.getUsername());
		verify(prefs, times(1)).get(Settings.PREF_KEY_USERNAME, "");
		verifyNoMoreInteractions(prefs);
	}
	
	@Test
	public void passwordReturnsCorrectValueWhenSet(){
		when(prefs.get(Settings.PREF_KEY_PASSWORD, "")).thenReturn("correct");
		assertEquals("correct", settings.getPassword());
		verify(prefs, times(1)).get(Settings.PREF_KEY_PASSWORD, "");
		verifyNoMoreInteractions(prefs);
	}
	
	@Test
	public void canSaveServerBaseUrl() {
		settings.setServerBaseUrl("http://tmc.mooc.fi");
		verify(prefs, times(1)).put(Settings.PREF_KEY_TMC_SERVER_URL, "http://tmc.mooc.fi");
		verifyNoMoreInteractions(prefs);
	}
	
	@Test
	public void canSaveCurrentCourse(){
		settings.setCurrentCourseName("kurssi");
		verify(prefs, times(1)).put(Settings.PREF_KEY_CURRENT_COURSE, "kurssi");
		verifyNoMoreInteractions(prefs);
	}

	@Test
	public void canSaveUsername() {
		settings.setUsername("matti.meikalainen");
		verify(prefs, times(1)).put(Settings.PREF_KEY_USERNAME, "matti.meikalainen");
		verifyNoMoreInteractions(prefs);
	}
	
	@Test
	public void canSavePassword() {
		settings.setPassword("password123");
		verify(prefs, times(1)).put(Settings.PREF_KEY_PASSWORD, "password123");
		verifyNoMoreInteractions(prefs);
	}
	
	@Test
	public void testSavingFailure() throws BackingStoreException{
		settings.setUsername("matti.meikalainen");
		settings.setPassword("password123");
		
		doThrow(new BackingStoreException("")).when(prefs).flush();
		assertFalse(settings.save());
	}
	
	@Test
	public void testSavingSuccess() {
		settings.setUsername("matti.meikalainen");
		settings.setPassword("password123");
		
		assertTrue(settings.save());
	}
	
	@Test
	public void testDefaultSettingsSuccess(){
		assertNotNull(Settings.getDefaultSettings());
	}
}
