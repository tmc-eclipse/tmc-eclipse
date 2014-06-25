package fi.helsinki.cs.tmc.core.services;

import java.util.ArrayList;
import java.util.List;

import fi.helsinki.cs.tmc.core.domain.Review;

/**
 * Class that handles storing the code reviews locally. Prevents the situation
 * where same review is shown multiple times (after each review background
 * check) if user has not marked them as read. The reviews will only be shown
 * again during new session.
 */
public class ReviewDAO {
    private List<Review> reviews;

    public ReviewDAO() {
        this.reviews = new ArrayList<Review>();
    }

    public boolean add(Review newReview) {
        if (!reviews.contains(newReview)) {
            reviews.add(newReview);
            return true;
        } else {
            return overwriteIfNecessary(newReview);
        }
    }

    private boolean overwriteIfNecessary(Review newReview) {
        int index = reviews.indexOf(newReview);
        Review current = reviews.get(index);

        if (current.getUpdatedAt().before(newReview.getUpdatedAt())) {
            reviews.set(index, newReview);
            return true;
        } else {
            return false;
        }
    }

    public void addAll(List<Review> reviews) {
        for (Review r : reviews) {
            add(r);
        }
    }

    public List<Review> all() {
        return reviews;
    }

    public List<Review> unseen() {
        List<Review> unseen = new ArrayList<Review>();
        for (Review r : reviews) {
            if (!r.isMarkedAsRead()) {
                unseen.add(r);
            }
        }
        return unseen;
    }
}
