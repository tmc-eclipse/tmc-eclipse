package fi.helsinki.cs.tmc.core.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;

import fi.helsinki.cs.tmc.core.domain.ExerciseKey.GsonAdapter;

public class ExerciseKeyTest {
    ExerciseKey ek;

    @Before
    public void setUp() {
        ek = new ExerciseKey("a", "b");
    }

    @Test
    public void constructorSetMemberVariables() {
        ExerciseKey e = new ExerciseKey("a", "b");
        assertEquals("a", e.getCourseName());
        assertEquals("b", e.getExerciseName());
    }

    @Test
    public void equalityIfCourseAndExerciseAreSame() {
        ExerciseKey same = new ExerciseKey("a", "b");
        assertEquals(ek, same);
    }

    @Test
    public void notEqualIfCourseOrExerciseIsNotSame() {
        ExerciseKey other1 = new ExerciseKey("a", "c");
        ExerciseKey other2 = new ExerciseKey("c", "b");
        assertFalse(ek.equals(other1));
        assertFalse(ek.equals(other2));
    }

    @Test
    public void notEqualOnNull() {
        assertFalse(ek.equals(null));
    }

    @Test
    public void notEqualWithOtherClasses() {
        assertFalse(ek.equals(new Integer(1)));
    }

    @Test
    public void hashCodeIsSameIfObjectAreEqual() {
        ExerciseKey same = new ExerciseKey("a", "b");
        assertEquals(ek.hashCode(), same.hashCode());
    }

    @Test
    public void hashcodeNotSameIfCourseOrExerciseIsNotSame() {
        ExerciseKey other1 = new ExerciseKey("a", "c");
        ExerciseKey other2 = new ExerciseKey("c", "b");
        assertFalse(ek.hashCode() == other1.hashCode());
        assertFalse(ek.hashCode() == other2.hashCode());
    }

    @Test
    public void toStringReturnCorrectString() {
        assertEquals("a/b", ek.toString());
    }

    /*
     * GSONADAPTER
     */

    @Test
    public void gsonAdapterReturnEqualObjectAfterSerializationAndDeserialization() {
        GsonAdapter adapter = new GsonAdapter();
        JsonElement element = adapter.serialize(ek, getClass(), null);
        ExerciseKey deserializedKey = adapter.deserialize(element, getClass(), null);

        assertEquals(ek, deserializedKey);
    }

    @Test
    public void testGsonAdapterSerialize() {
        ExerciseKey.GsonAdapter ga = new ExerciseKey.GsonAdapter();
        assertEquals(ga.serialize(ek, ek.getClass(), null), new JsonPrimitive("a/b"));
    }

    @Test
    public void testGsonAdapterDeserialize() {
        ExerciseKey.GsonAdapter ga = new ExerciseKey.GsonAdapter();
        JsonPrimitive jp = new JsonPrimitive("a/b");
        assertEquals(ga.deserialize(jp, jp.getClass(), null), ek);
    }

    @Test(expected = JsonParseException.class)
    public void testGsonAdapterDeserializeFailure() throws JsonParseException {
        ExerciseKey.GsonAdapter ga = new ExerciseKey.GsonAdapter();
        JsonPrimitive jp = new JsonPrimitive("aa");
        ga.deserialize(jp, jp.getClass(), null);
    }

    @Test(expected = JsonParseException.class)
    public void testGsonAdapterDeserializeFailureEmptyString() throws JsonParseException {
        ExerciseKey.GsonAdapter ga = new ExerciseKey.GsonAdapter();
        JsonPrimitive jp = new JsonPrimitive("");
        ga.deserialize(jp, jp.getClass(), null);
    }
}
