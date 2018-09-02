package com.kafilicious.popularmovies.data.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

import com.kafilicious.popularmovies.data.local.dao.MovieDao
import com.kafilicious.popularmovies.data.local.dao.ReviewDao
import com.kafilicious.popularmovies.data.local.dao.VideoDao
import com.kafilicious.popularmovies.data.models.db.Movie
import com.kafilicious.popularmovies.data.models.db.Review
import com.kafilicious.popularmovies.data.models.db.Video

@Database(entities = arrayOf(Movie::class, Review::class, Video::class), version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao

    abstract fun reviewDao(): ReviewDao

    abstract fun videoDao(): VideoDao
}
