package fi.helsinki.cs.plugin.tmc.domain;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonElement;

import fi.helsinki.cs.plugin.tmc.domain.ExerciseKey.GsonAdapter;

public class ExerciseKeyTest {
	ExerciseKey ek;
	
	@Before
	public void setUp(){
		ek = new ExerciseKey("a", "b");
	}
	
	@Test
	public void constructorSetMemberVariables(){
		ExerciseKey e = new ExerciseKey("a", "b");
		assertEquals("a", e.courseName);
		assertEquals("b", e.exerciseName);
	}
	
	@Test
	public void equalityIfCourseAndExerciseAreSame(){
		ExerciseKey same = new ExerciseKey("a", "b");
		assertEquals(ek, same);
	}
	
	@Test
	public void notEqualIfCourseOrExerciseIsNotSame(){
		ExerciseKey other1 = new ExerciseKey("a", "c");
		ExerciseKey other2 = new ExerciseKey("c", "b");
		assertFalse(ek.equals(other1));
		assertFalse(ek.equals(other2));
	}
	
	@Test
	public void notEqualOnNull(){
		assertFalse(ek.equals(null));
	}
	
	@Test
	public void notEqualWithOtherClasses(){
		assertFalse(ek.equals(new Integer(1)));
	}
	
	@Test
	public void hashCodeIsSameIfObjectAreEqual() {
		ExerciseKey same = new ExerciseKey("a", "b");
		assertEquals(ek.hashCode(), same.hashCode());
	}
	
	@Test
	public void hashcodeNotSameIfCourseOrExerciseIsNotSame(){
		ExerciseKey other1 = new ExerciseKey("a", "c");
		ExerciseKey other2 = new ExerciseKey("c", "b");
		assertFalse(ek.hashCode() == other1.hashCode());
		assertFalse(ek.hashCode() == other2.hashCode());
	}
	
	@Test
	public void toStringReturnCorrectString(){
		assertEquals("a/b", ek.toString());
	}
	
	
	/*
	 * GSONADAPTER 
	 */
	
	@Test
	public void gsonAdapterReturnEqualObjectAfterSerializationAndDeserialization(){
		GsonAdapter adapter = new GsonAdapter();
		JsonElement element = adapter.serialize(ek, getClass(), null);
		ExerciseKey deserializedKey = adapter.deserialize(element, getClass(), null);
		
		assertEquals(ek, deserializedKey);
	}
}
