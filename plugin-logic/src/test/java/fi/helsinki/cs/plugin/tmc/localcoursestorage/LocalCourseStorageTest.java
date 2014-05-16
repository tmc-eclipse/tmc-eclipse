package fi.helsinki.cs.plugin.tmc.localcoursestorage;

import static org.mockito.Mockito.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import fi.helsinki.cs.plugin.tmc.domain.Course;
import fi.helsinki.cs.plugin.tmc.io.IO;
import fi.helsinki.cs.plugin.tmc.services.web.UserVisibleException;
import fi.helsinki.cs.plugin.tmc.storage.LocalCourseStorage;

public class LocalCourseStorageTest {

	private IO io;
	private LocalCourseStorage lcs;
	
	@Before
	public void setUp() {
		this.io = mock(IO.class);
		lcs = new LocalCourseStorage(io);
	}
	
	@Test(expected=UserVisibleException.class)
	public void testExceptionIsThrownIfNullIO() throws UserVisibleException {
		this.io = null;
		lcs.load();
	}
	
	@Test(expected=UserVisibleException.class)
	public void testExceptionIsThrownIfFileDoesntExist() throws UserVisibleException {
		when(io.exists()).thenReturn(false);
		lcs.load();
	}
	
	@Test(expected=UserVisibleException.class)
	public void testExceptionIsThrownIfReaderIsNull() throws UserVisibleException {
		when(io.getReader()).thenReturn(null);
		lcs.load();
	}
	
	@Test(expected=UserVisibleException.class)
	public void testExceptionIsThrownIfWriterIsNull() throws UserVisibleException {
		when(io.getWriter()).thenReturn(null);
		lcs.save(new ArrayList<Course>());
	}
	
}
