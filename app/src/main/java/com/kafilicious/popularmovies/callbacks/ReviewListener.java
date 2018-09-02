package com.kafilicious.popularmovies.callbacks;

import com.kafilicious.popularmovies.data.models.db.Review;

import java.util.List;

public interface ReviewListener {
    void reviewResults(List<Review> results);
    void reviewError(String error);
}
