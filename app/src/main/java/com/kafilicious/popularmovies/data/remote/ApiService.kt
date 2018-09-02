package com.kafilicious.popularmovies.data.remote

import com.kafilicious.popularmovies.data.models.remote.MoviesResponse
import com.kafilicious.popularmovies.data.models.remote.ReviewResponse
import com.kafilicious.popularmovies.data.models.remote.TrailerResponse

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by apple on 30/12/2017.
 */

interface ApiService {


    @GET("{type}/{sort}")
    fun getMovies(@Path("type") type: String,
                  @Path("sort") sort: String,
                  @Query("api_key") key: String,
                  @Query("page") page: Int): Call<MoviesResponse>

    @GET("{type}/{id}/reviews")
    fun getReviews(@Path("type") type: String,
                   @Path("id") requestId: String,
                   @Query("api_key") key: String,
                   @Query("language") language: String,
                   @Query("page") page: Int): Call<ReviewResponse>

    @GET("{type}/{id}/videos")
    fun getTrailers(@Path("type") type: String,
                    @Path("id") requestId: String,
                    @Query("api_key") key: String,
                    @Query("language") language: String): Call<TrailerResponse>

    companion object {
        val BASE_URL = "https://api.themoviedb.org/3/"
    }
}
