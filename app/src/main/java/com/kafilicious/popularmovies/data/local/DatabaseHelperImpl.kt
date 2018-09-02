package com.kafilicious.popularmovies.data.local

import com.kafilicious.popularmovies.data.models.db.Movie
import com.kafilicious.popularmovies.data.models.db.Review
import com.kafilicious.popularmovies.data.models.db.Video

import javax.inject.Inject
import javax.inject.Singleton

import io.reactivex.Observable

@Singleton
class DatabaseHelperImpl @Inject
constructor(private val appDatabase: AppDatabase) : DatabaseHelper {


    override fun getAllMovies(): Observable<List<Movie>> {
        return Observable.fromCallable { appDatabase.movieDao().all }
    }

    override fun getAllReviews(): Observable<List<Review>> {
        return Observable.fromCallable { appDatabase.reviewDao().all }
    }

    override fun getAllVideos(): Observable<List<Video>> {
        return Observable.fromCallable { appDatabase.videoDao().all }
    }

    override fun insertMovie(movie: Movie): Observable<Boolean> {
        return Observable.fromCallable {
            appDatabase.movieDao().insert(movie)
            true
        }
    }

    override fun insertMovies(movies: List<Movie>): Observable<Boolean> {
        return Observable.fromCallable {
            appDatabase.movieDao().insertAll(movies)
            true
        }
    }

    override fun insertVideos(videos: List<Video>): Observable<Boolean> {
        return Observable.fromCallable {
            appDatabase.videoDao().insertAll(videos)
            true
        }
    }

    override fun insertVideo(video: Video): Observable<Boolean> {
        return Observable.fromCallable {
            appDatabase.videoDao().insert(video)
            true
        }
    }

    override fun insertReviews(reviews: List<Review>): Observable<Boolean> {
        return Observable.fromCallable {
            appDatabase.reviewDao().insertAll(reviews)
            true
        }
    }

    override fun insertReview(review: Review): Observable<Boolean> {
        return Observable.fromCallable {
            appDatabase.reviewDao().insert(review)
            true
        }
    }
}
