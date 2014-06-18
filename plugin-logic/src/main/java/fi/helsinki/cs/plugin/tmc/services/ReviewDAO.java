package fi.helsinki.cs.plugin.tmc.services;

import java.util.ArrayList;
import java.util.List;

import fi.helsinki.cs.plugin.tmc.domain.Review;

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
