package fi.helsinki.cs.plugin.tmc.ui;

import static org.junit.Assert.*;

import org.junit.Test;

public class ObsoleteClientExceptionTest {

	@Test
	public void containsCorrectString(){
		assertEquals("Please update the TMC plugin.\nUse Help -> Check for Updates.",
				new ObsoleteClientException().getMessage());
	}

}
