package fi.helsinki.cs.plugin.tmc.ui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;

public class UserVisibleExceptionTest {

	@Test
	public void messageIsSet(){
		assertEquals("msg", new UserVisibleException("msg").getMessage());
	}
	
	@Test
	public void messageAndCauseAreSet(){
		Throwable t = mock(Throwable.class);
		UserVisibleException e = new UserVisibleException("msg", t);
		assertEquals("msg", e.getMessage());
		assertEquals(t, e.getCause());
	}

}
