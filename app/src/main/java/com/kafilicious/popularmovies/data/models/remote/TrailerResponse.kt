package com.kafilicious.popularmovies.data.models.remote

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.kafilicious.popularmovies.data.models.db.Video

import java.io.Serializable

data class TrailerResponse(
        @Expose
        @SerializedName("id")
        var id: Long = 0,
        @Expose
        @SerializedName("results")
        var results: List<Video>? = null
)