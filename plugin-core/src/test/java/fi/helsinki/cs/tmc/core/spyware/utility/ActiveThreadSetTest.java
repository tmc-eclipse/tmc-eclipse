package fi.helsinki.cs.tmc.core.spyware.utility;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ActiveThreadSetTest {
    private ActiveThreadSet set;

    @Before
    public void setUp() {
        set = new ActiveThreadSet();
    }

    @Test
    public void newSetContainsNoThreads() {
        assertEquals(0, getThreads().size());

    }

    @Test
    public void newThreadIsAddedToSet() {
        Thread thread = mock(Thread.class);
        set.addThread(thread);
        assertEquals(1, getThreads().size());
    }

    @Test
    public void newThreadIsAddedToSetWhenThereAlreadyIsAThreadThatIsNotTerminated() {
        Thread thread = mock(Thread.class);
        when(thread.getState()).thenReturn(Thread.State.RUNNABLE);
        set.addThread(thread);
        thread = mock(Thread.class);
        set.addThread(thread);

        assertEquals(2, getThreads().size());
    }

    @Test
    public void whenNewThreadIsAddedToSetTerminatedThreadsAreRemoved() {
        Thread thread = mock(Thread.class);
        when(thread.getState()).thenReturn(Thread.State.TERMINATED);
        set.addThread(thread);
        Thread thread2 = mock(Thread.class);
        set.addThread(thread2);

        assertEquals(1, getThreads().size());
        assertEquals(thread2, getThreads().get(0));
    }

    List<Thread> getThreads() {
        try {
            Field field;
            field = ActiveThreadSet.class.getDeclaredField("threads");
            field.setAccessible(true);
            return (List<Thread>) field.get(set);
        } catch (NoSuchFieldException e) {
            fail("No such field");
        } catch (SecurityException e) {
            fail("Security exception");
        } catch (IllegalArgumentException e) {
            fail("Illegal argument exception");
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            fail("Illegal access exception");
        }
        return null;

    }

}
