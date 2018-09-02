package com.kafilicious.popularmovies.data.models.remote

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.kafilicious.popularmovies.data.models.db.Movie

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

data class MoviesResponse(
        @Expose
        @SerializedName("page")
        var page: Double = 0.toDouble(),
        @Expose
        @SerializedName("results")
        var results: List<Movie>? = null,
        @Expose
        @SerializedName("totalResults")
        var totalResults: Double = 0.toDouble(),
        @Expose
        @SerializedName("totalPages")
        var totalPages: Double = 0.toDouble()
)
