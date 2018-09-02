package com.kafilicious.popularmovies.callbacks;

import com.kafilicious.popularmovies.data.models.db.Video;

import java.util.List;

public interface TrailerListener {
    void trailerResults(List<Video> results);
    void trailerErrors(String error);
}
