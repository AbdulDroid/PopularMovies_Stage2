package com.kafilicious.popularmovies.callbacks;


import com.kafilicious.popularmovies.data.models.db.Movie;

import java.util.List;

public interface MoviesListener {
    void gotMovies(List<Movie> result);
    void getError(String error);
}
