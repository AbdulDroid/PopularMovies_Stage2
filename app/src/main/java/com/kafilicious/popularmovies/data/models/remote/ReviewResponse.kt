package com.kafilicious.popularmovies.data.models.remote

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.kafilicious.popularmovies.data.models.db.Review


data class ReviewResponse(
        @Expose
        @SerializedName("id")
        var id: Long = 0,
        @SerializedName("page")
        var page: Int = 0,
        @Expose
        @SerializedName("results")
        var results: List<Review>? = null,
        @Expose
        @SerializedName("total_pages")
        var total_pages: Int = 0,
        @Expose
        @SerializedName("total_results")
        var total_results: Int = 0
)
