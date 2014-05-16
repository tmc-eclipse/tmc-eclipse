package fi.helsinki.cs.plugin.tmc.localcoursestorage;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import fi.helsinki.cs.plugin.tmc.domain.Course;
import fi.helsinki.cs.plugin.tmc.io.IO;
import fi.helsinki.cs.plugin.tmc.storage.LocalCourseStorage;

public class LocalCourseStorageTest {

	private IO io;
	private LocalCourseStorage lcs;
	
	@Before
	public void setUp() {
		this.io = mock(IO.class);
		lcs = new LocalCourseStorage(io);
	}
	
	@Test
	public void loadReturnsEmptyListIfFileDoesntExist() {
		when(io.exists()).thenReturn(false);
		
		assertTrue(lcs.load() != null && lcs.load() instanceof List && lcs.load().size() == 0);
	}
	
	@Test
	public void testNullIO(){
		LocalCourseStorage lcs = new LocalCourseStorage(null);
		
	}

}
