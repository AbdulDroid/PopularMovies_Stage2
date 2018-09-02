package com.kafilicious.popularmovies.data.remote

import com.kafilicious.popularmovies.data.models.remote.MoviesResponse
import com.kafilicious.popularmovies.data.models.remote.ReviewResponse
import com.kafilicious.popularmovies.data.models.remote.TrailerResponse


import io.reactivex.Flowable
import io.reactivex.Single

interface ApiHelper {

    fun getAllMovies(type: String, sort: String, page: Int): Flowable<MoviesResponse>?

    fun getAllReviews(type: String, requestId: String, language: String, page: Int): Single<ReviewResponse>?

    fun getAllVideos(type: String, requestId: String, language: String): Single<TrailerResponse>?
}
