package fi.helsinki.cs.tmc.core.utils.jsonhelpers;

import org.junit.Before;
import org.junit.Test;

import fi.helsinki.cs.tmc.core.domain.Review;
import fi.helsinki.cs.tmc.core.utils.jsonhelpers.ReviewList;

public class ReviewListTest {
    private ReviewList rl;

    @Before
    public void setUp() throws Exception {
        rl = new ReviewList();
    }

    @Test
    public void hasNeededFields() {
        // These won't compile if the required fields are missing
        rl.apiVersion = 1;
        rl.reviews = new Review[1];
    }

}
