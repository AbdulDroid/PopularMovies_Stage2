package com.kafilicious.popularmovies.data.local

import com.kafilicious.popularmovies.data.models.db.Movie
import com.kafilicious.popularmovies.data.models.db.Review
import com.kafilicious.popularmovies.data.models.db.Video

import io.reactivex.Observable

interface DatabaseHelper {

    fun getAllMovies(): Observable<List<Movie>>

    fun getAllReviews(): Observable<List<Review>>

    fun getAllVideos(): Observable<List<Video>>

    fun insertMovie(movie: Movie): Observable<Boolean>

    fun insertMovies(movies: List<Movie>): Observable<Boolean>

    fun insertVideos(videos: List<Video>): Observable<Boolean>

    fun insertVideo(video: Video): Observable<Boolean>

    fun insertReviews(reviews: List<Review>): Observable<Boolean>

    fun insertReview(review: Review): Observable<Boolean>
}
