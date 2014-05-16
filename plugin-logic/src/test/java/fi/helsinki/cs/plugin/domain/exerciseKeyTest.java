package fi.helsinki.cs.plugin.domain;

import static org.junit.Assert.*;

import org.junit.*;

import fi.helsinki.cs.plugin.tmc.domain.Exercise;
import fi.helsinki.cs.plugin.tmc.domain.ExerciseKey;

public class exerciseKeyTest {

	private ExerciseKey ek;
	private ExerciseKey ekDiffExName;
	private ExerciseKey ekDiffCourseName;
	private ExerciseKey ekSame;
	
	@Before
	public void setUp() throws Exception {
		ek=new ExerciseKey("asd", "asd");
		ekDiffExName=new ExerciseKey("asd", "asd1");
		ekDiffCourseName=new ExerciseKey("asd1", "asd");
		ekSame=new ExerciseKey("asd", "asd");
	}

	@Test
	public void testEqualsTrue() {
		assertTrue(ek.equals(ekSame));
	}
	
	@Test
	public void testEqualsFalseWithDifferentExNames() {
		assertFalse(ek.equals(ekDiffExName));
	}
	
	@Test
	public void testEqualsFalseWithDifferentCourseNames() {
		assertFalse(ek.equals(ekDiffCourseName));
	}
	
	@Test
	public void testEqualsFalseWithDifferentClass() {
		assertFalse(ek.equals(new Exercise("asd", "asd")));
	}

	@Test
	public void testToString() {
		assertEquals(ek.toString(), "asd/asd");
	}

}
