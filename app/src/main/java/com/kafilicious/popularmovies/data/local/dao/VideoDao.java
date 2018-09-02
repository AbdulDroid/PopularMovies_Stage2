package com.kafilicious.popularmovies.data.local.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.kafilicious.popularmovies.data.models.db.Video;

import java.util.List;

@Dao
public interface VideoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Video video);

    @Insert(onConflict =  OnConflictStrategy.REPLACE)
    void insertAll(List<Video> videos);

    @Query("select * from `video.db`")
    List<Video> getAll();

    @Query("select * from `video.db` where id = :id")
    List<Video> getReviewsById(Long id);
}
