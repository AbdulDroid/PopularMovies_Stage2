package com.kafilicious.popularmovies.data.models.db

/*
 * Copyright (C) 2017 Popular Movies, Stage 2
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "movies.db")
class Movie {

    @Expose
    @PrimaryKey
    var movie_id: Int = 0

    @Expose
    @SerializedName("posterPath")
    @ColumnInfo(name = "posterPath")
    var posterPath: String? = null

    @Expose
    @SerializedName("adult")
    @ColumnInfo(name = "adult")
    var adult: Boolean = false

    @Expose
    @SerializedName("overview")
    @ColumnInfo(name = "overview")
    var overview: String? = null

    @Expose
    @SerializedName("releaseDate")
    @ColumnInfo(name = "releaseDate")
    var releaseDate: String? = null


    @Expose
    @SerializedName("id")
    @ColumnInfo(name = "id")
    var id: Long = 0

    @Expose
    @SerializedName("originalTitle")
    @ColumnInfo(name = "originalTitle")
    var originalTitle: String? = null

    @Expose
    @SerializedName("originalLanguage")
    @ColumnInfo(name = "originalLanguage")
    var originalLanguage: String? = null

    @Expose
    @SerializedName("title")
    @ColumnInfo(name = "title")
    var title: String? = null

    @Expose
    @SerializedName("backdropPath")
    @ColumnInfo(name = "backdropPath")
    var backdropPath: String? = null

    @Expose
    @SerializedName("popularity")
    @ColumnInfo(name = "popularity")
    var popularity: Double = 0.toDouble()

    @Expose
    @SerializedName("voteCount")
    @ColumnInfo(name = "voteCount")
    var voteCount: Long = 0

    @Expose
    @SerializedName("video")
    @ColumnInfo(name = "video")
    var video: Boolean = false

    @Expose
    @SerializedName("voteAverage")
    @ColumnInfo(name = "voteAverage")
    var voteAverage: Double = 0.toDouble()
}