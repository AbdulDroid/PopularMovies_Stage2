package com.kafilicious.popularmovies.utils;

import android.content.Context;

import com.kafilicious.popularmovies.models.MovieList;
import com.kafilicious.popularmovies.models.MoviesResponse;
import com.kafilicious.popularmovies.adapters.MovieListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

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

public class OpenMovieJsonUtils {

    private static MoviesResponse moviesResponse = new MoviesResponse();
    private static List<MovieList> movie_list;
    private static MovieListAdapter movieListAdapter;

    public static void getSimpleMovieStringsFromJson(Context context, String movieJsonStr)
        throws JSONException{
        JSONObject movieJson = new JSONObject(movieJsonStr);

        moviesResponse.setPage(movieJson.getDouble("page"));

        JSONArray results = movieJson.getJSONArray("results");

        moviesResponse.setTotalPages(movieJson.getDouble("total_pages"));

        moviesResponse.setTotalResults(movieJson.getDouble("total_results"));

        for (int i=0; i<results.length(); i++){
            JSONObject obj = results.getJSONObject(i);

            MovieList addMovie = new MovieList();
            addMovie.posterPath = obj.getString("poster_path");
            addMovie.voteCount = obj.getLong("vote_count");
            addMovie.overview = obj.getString("overview");
            addMovie.voteAverage = obj.getDouble("vote_average");
            addMovie.releaseDate = obj.getString("release_date");
            addMovie.id = obj.getInt("id");

            movie_list.add(addMovie);
        }
    }
}
