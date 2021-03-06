package fi.helsinki.cs.tmc.core.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import fi.helsinki.cs.tmc.core.domain.Review;

public class ReviewDAOTest {
    private ReviewDAO dao;

    @Before
    public void setUp() throws Exception {
        dao = new ReviewDAO();
    }

    @Test
    public void callingAllForNewlyConstructedDaoDoesNotReturnNull() {
        assertNotNull(dao.all());
    }

    @Test
    public void callingUnseenForNewlyConstructedDaoDoesNotReturnNull() {
        assertNotNull(dao.unseen());
    }

    @Test
    public void unseenReturnsOnlyAllReviewsNotMarkedAsRead() {
        Review r1 = new Review();
        r1.setId(1);
        r1.setMarkedAsRead(false);
        dao.add(r1);

        Review r2 = new Review();
        r2.setId(2);
        r2.setMarkedAsRead(true);
        dao.add(r2);

        Review r3 = new Review();
        r3.setId(3);
        r3.setMarkedAsRead(false);
        dao.add(r3);

        assertTrue(dao.unseen().contains(r1));
        assertTrue(dao.unseen().contains(r3));
        assertFalse(dao.unseen().contains(r2));

    }

    @Test
    public void addingNotCurrentlyPresentReviewAddsItToTheDB() {
        Review r = new Review();
        assertTrue(dao.add(r));
        assertTrue(dao.all().contains(r));
        assertEquals(1, dao.all().size());
    }

    @Test
    public void addingReviewAgainOnlyAddsIfCurrentIsOlderThanTheNewOne() {
        Review r1 = new Review();
        r1.setUpdatedAt(new Date(100000000L));
        r1.setExerciseName("a");

        Review r2 = new Review();
        r2.setUpdatedAt(new Date(2000000000L));
        r2.setExerciseName("b");

        Review r3 = new Review();
        r3.setUpdatedAt(new Date(30000000000L));
        r3.setExerciseName("c");

        dao.add(r2);

        assertFalse(dao.add(r1));
        assertEquals(1, dao.all().size());
        assertEquals("b", dao.all().get(0).getExerciseName());

        assertTrue(dao.add(r3));
        assertEquals(1, dao.all().size());
        assertEquals("c", dao.all().get(0).getExerciseName());
    }

    @Test
    public void addAllAddsAllReviewsOnSameRulesAsAddingASingleNewReview() {
        Review r1 = new Review();
        r1.setUpdatedAt(new Date(100000000L));
        r1.setExerciseName("a");

        Review r2 = new Review();
        r2.setUpdatedAt(new Date(2000000000L));
        r2.setExerciseName("b");

        Review r3 = new Review();
        r3.setUpdatedAt(new Date(30000000000L));
        r3.setExerciseName("c");

        List<Review> list = new ArrayList<Review>();
        list.add(r1);
        list.add(r3);

        dao.add(r2);
        dao.addAll(list);
        assertEquals(1, dao.all().size());
        assertEquals("c", dao.all().get(0).getExerciseName());
    }

}
