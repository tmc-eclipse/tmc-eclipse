package fi.helsinki.cs.plugin.domain;

import static org.junit.Assert.*;

import org.junit.*;

import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;

import fi.helsinki.cs.plugin.tmc.domain.Exercise;
import fi.helsinki.cs.plugin.tmc.domain.ExerciseKey;

public class ExerciseKeyTest {

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
	
	@Test
	public void testGsonAdapterSerialize(){
		ExerciseKey.GsonAdapter ga=new ExerciseKey.GsonAdapter();
		assertEquals(ga.serialize(ek, ek.getClass(), null), new JsonPrimitive("asd/asd"));
	}
	
	@Test
	public void testGsonAdapterDeserialize(){
		ExerciseKey.GsonAdapter ga=new ExerciseKey.GsonAdapter();
		JsonPrimitive jp=new JsonPrimitive("asd/asd");
		assertEquals(ga.deserialize(jp, jp.getClass(), null), ek);
	}
	
	@Test(expected=JsonParseException.class)
	public void testGsonAdapterDeserializeFailure() throws JsonParseException{
		ExerciseKey.GsonAdapter ga=new ExerciseKey.GsonAdapter();
		JsonPrimitive jp=new JsonPrimitive("asdasdasd");
		ga.deserialize(jp, jp.getClass(), null);
	}
	
	@Test(expected=JsonParseException.class)
	public void testGsonAdapterDeserializeFailureEmptyString() throws JsonParseException{
		ExerciseKey.GsonAdapter ga=new ExerciseKey.GsonAdapter();
		JsonPrimitive jp=new JsonPrimitive("");
		ga.deserialize(jp, jp.getClass(), null);
	}
	
}
