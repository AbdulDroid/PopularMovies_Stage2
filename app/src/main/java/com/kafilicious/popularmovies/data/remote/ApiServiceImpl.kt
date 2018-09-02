package com.kafilicious.popularmovies.data.remote

import com.kafilicious.popularmovies.data.models.remote.MoviesResponse
import com.kafilicious.popularmovies.data.models.remote.ReviewResponse
import com.kafilicious.popularmovies.data.models.remote.TrailerResponse

import javax.inject.Inject

import io.reactivex.Flowable
import io.reactivex.Single

class ApiServiceImpl @Inject internal constructor(private val apiService: ApiService) : ApiHelper {

    override fun getAllMovies(type: String, sort: String, page: Int): Flowable<MoviesResponse>? {
        return null
    }

    override fun getAllReviews(type: String, requestId: String, language: String, page: Int): Single<ReviewResponse>? {
        return null
    }

    override fun getAllVideos(type: String, requestId: String, language: String): Single<TrailerResponse>? {
        return null
    }
}
