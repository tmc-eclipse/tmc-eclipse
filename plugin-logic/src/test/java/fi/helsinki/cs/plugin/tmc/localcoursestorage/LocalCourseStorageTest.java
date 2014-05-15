package fi.helsinki.cs.plugin.tmc.localcoursestorage;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;

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
	public void loadReturnsNullIfFileDoesntExist() {
		when(io.exists()).thenReturn(false);
		
		assertNull(lcs.load());
	}

}
