package fi.helsinki.cs.plugin.tmc.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

public class ExcerciseTest {

    Exercise exercise;

    @Before
    public void setUp() {
        exercise = new Exercise();
    }

    @Test
    public void constructorSetsNameFieldCorrectly() {
        exercise = new Exercise("test name");
        assertEquals("test name", exercise.getName());
    }

    @Test
    public void constructorSetsNameAndCourseNameFieldSCorrectly() {
        exercise = new Exercise("test name", "test course name");
        assertEquals("test name", exercise.getName());
        assertEquals("test course name", exercise.getCourseName());
    }

    @Test(expected = NullPointerException.class)
    public void setNameThrowsIfParameterIsNull() {
        exercise.setName(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setNameThrowsIfParameterIsEmpty() {
        exercise.setName("");
    }

    @Test
    public void setNameWithValidParameterWorks() {
        exercise.setName("name 01");
        assertEquals("name 01", exercise.getName());
    }

    @Test(expected = NullPointerException.class)
    public void hasDeadlinePassedAtThrowsIfParameterIsNull() {
        exercise.hasDeadlinePassedAt(null);
    }

    @Test
    public void hasDeadlinePassedAtReturnsTrueIfDeadlineHasPassed() {
        setPassedDeadline();
        assertTrue(exercise.hasDeadlinePassedAt(new Date()));
    }

    private void setPassedDeadline() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        exercise.setDeadline(cal.getTime());
    }

    @Test
    public void hasDeadlinePassedAtReturnsFalseIfDeadlineHasNotPassed() {
        setNonPassedDeadline();
        assertFalse(exercise.hasDeadlinePassedAt(new Date()));
    }

    private void setNonPassedDeadline() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, +1);
        exercise.setDeadline(cal.getTime());
    }

    @Test
    public void hasDeadlinePassedAtReturnsFalseIfNoDeadlineIsSet() {
        assertFalse(exercise.hasDeadlinePassedAt(new Date()));
    }

    @Test
    public void hasDeadlinePassedReturnsFalseIfDeadlineIsNow() {
        Date date = new Date();
        exercise.setDeadline(date);
        assertFalse(exercise.hasDeadlinePassedAt(date));
    }

    @Test(expected = NullPointerException.class)
    public void setDownloadUrlThrowsIfParameterIsNull() {
        exercise.setDownloadUrl(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setDownloadUrlThrowsIfParameterIsEmpty() {
        exercise.setDownloadUrl("");
    }

    @Test
    public void setDownloadUrlWithValidParameterWorks() {
        exercise.setDownloadUrl("url 01");
        assertEquals("url 01", exercise.getDownloadUrl());
    }

    @Test(expected = NullPointerException.class)
    public void setReturnUrlThrowsIfParameterIsNull() {
        exercise.setReturnUrl(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setReturnUrlThrowsIfParameterIsEmpty() {
        exercise.setReturnUrl("");
    }

    @Test
    public void setReturnUrlWithValidParameterWorks() {
        exercise.setReturnUrl("url 01");
        assertEquals("url 01", exercise.getReturnUrl());
    }

    @Test
    public void isReturnableReturnsTrueIfIsReturnableAndDeadlineHasNotPassed() {
        setNonPassedDeadline();
        exercise.setReturnable(true);
        assertTrue(exercise.isReturnable());
    }

    @Test
    public void isReturnableReturnsFalseIfIsReturnableAndDeadlineHasPassed() {
        setPassedDeadline();
        exercise.setReturnable(true);
        assertFalse(exercise.isReturnable());
    }

    @Test
    public void isReturnableReturnsFalseIfIsNotReturnableAndDeadlineHasNotPassed() {
        setNonPassedDeadline();
        exercise.setReturnable(false);
        assertFalse(exercise.isReturnable());
    }

    @Test
    public void isReturnableReturnsFalseIfIsNotReturnableAndDeadlineHasPassed() {
        setPassedDeadline();
        exercise.setReturnable(false);
        assertFalse(exercise.isReturnable());
    }

    @Test
    public void deadlineIsNonNullAfterCallingFinalizeDeSerialization() {
        exercise.setDeadlineString("2012-03-20T20:34:00+0200");
        exercise.finalizeDeserialization();
        assertNotNull(exercise.getDeadline());
    }

    @Test
    public void deadlineIsNullAfterCallingFinalizeDeSerialization() {
        exercise.setDeadlineString("2012-03-20T20:34:00+0200");
        assertNull(exercise.getDeadline());
    }

    @Test
    public void deadlineIsNullAfterCallingFinalizeDeSerializationAndStringIsEmpty() {
        exercise.setDeadlineString("");
        assertNull(exercise.getDeadline());
    }

    @Test
    public void testId() {
        exercise.setId(1);
        assertEquals(1, exercise.getId());
    }

    @Test
    public void testLocked() {
        exercise.setLocked(true);
        assertEquals(true, exercise.isLocked());
    }

    @Test
    public void testDeadlineDescription() {
        exercise.setDeadlineDescription("desc");
        assertEquals("desc", exercise.getDeadlineDescription());
    }

    @Test
    public void testExerciseKey() {
        exercise.setName("name");
        exercise.setCourseName("course");

        ExerciseKey ek = exercise.getKey();
        assertEquals(exercise.getCourseName(), ek.getCourseName());
        assertEquals(exercise.getName(), ek.getExerciseName());
    }

    @Test
    public void testCourseName() {
        exercise.setCourseName("course");
        assertEquals("course", exercise.getCourseName());
    }

    @Test
    public void testDownloadUrl() {
        exercise.setDownloadUrl("url");
        assertEquals("url", exercise.getDownloadUrl());
    }

    @Test
    public void testSolutionDownloadUrl() {
        exercise.setSolutionDownloadUrl("url");
        assertEquals("url", exercise.getSolutionDownloadUrl());
    }

    @Test
    public void testRequiresReview() {
        exercise.setRequiresReview(true);
        assertEquals(true, exercise.requiresReview());
    }

    @Test
    public void testAttempted() {
        exercise.setAttempted(true);
        assertEquals(true, exercise.isAttempted());
    }

    @Test
    public void testCompleted() {
        exercise.setCompleted(true);
        assertEquals(true, exercise.isCompleted());
    }

    @Test
    public void testReviwed() {
        exercise.setReviewed(true);
        assertEquals(true, exercise.isReviewed());
    }

    @Test
    public void testAllReviewPointsGiven() {
        exercise.setAllReviewPointsGiven(true);
        assertEquals(true, exercise.isAllReviewPointsGiven());
    }

    @Test
    public void testChecksum() {
        exercise.setChecksum("checksum");
        assertEquals("checksum", exercise.getChecksum());
    }

    @Test
    public void testMemoryLimit() {
        exercise.setMemoryLimit(1);
        assertEquals(1, exercise.getMemoryLimit().intValue());
    }

    @Test
    public void testToStringReturnsName() {
        exercise.setName("exerciseName");
        assertEquals("exerciseName", exercise.toString());
    }

    @Test
    public void finalizeDeserializationDoesNotTouchDeadlineDateIfStringIsNull() {
        exercise.finalizeDeserialization();
        assertNull(exercise.getDeadline());
    }

    @Test
    public void finalizeDeserializationDoesNotTouchDeadlineDateIfStringIsEmpty() throws NoSuchFieldException,
            SecurityException, IllegalArgumentException, IllegalAccessException {
        // Not optimal but works
        Field field = Exercise.class.getDeclaredField("deadlineString");
        field.setAccessible(true);
        field.set(exercise, "");

        exercise.finalizeDeserialization();
        assertNull(exercise.getDeadline());

    }
}
