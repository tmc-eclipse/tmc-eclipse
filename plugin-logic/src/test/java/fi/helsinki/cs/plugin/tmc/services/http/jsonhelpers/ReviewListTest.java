package fi.helsinki.cs.plugin.tmc.services.http.jsonhelpers;

import org.junit.Before;
import org.junit.Test;

import fi.helsinki.cs.plugin.tmc.domain.Review;

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
