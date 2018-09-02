package com.kafilicious.popularmovies.data.local.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.kafilicious.popularmovies.data.models.db.Review;

import java.util.List;

@Dao
public interface ReviewDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Review review);

    @Insert(onConflict =  OnConflictStrategy.REPLACE)
    void insertAll(List<Review> reviews);

    @Query("select * from `reviews.db`")
    List<Review> getAll();

    @Query("select * from `reviews.db` where id = :id")
    List<Review> getReviewsById(Long id);
}
