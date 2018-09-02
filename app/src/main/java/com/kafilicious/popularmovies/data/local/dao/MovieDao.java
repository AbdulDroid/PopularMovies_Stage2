package com.kafilicious.popularmovies.data.local.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.kafilicious.popularmovies.data.models.db.Movie;

import java.util.List;

@Dao
public interface MovieDao {

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insert(Movie movie);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Movie> movies);

    @Query("SELECT * FROM `movies.db`")
    List<Movie> getAll();

    @Query("SELECT * FROM `movies.db` WHERE id = :id")
    List<Movie> getMoviesById(long id);
}
