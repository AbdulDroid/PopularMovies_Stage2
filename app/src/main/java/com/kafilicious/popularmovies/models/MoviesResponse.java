package com.kafilicious.popularmovies.models;

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

public class MoviesResponse {

    public double page;
    public List<MovieList> results = null;
    public double totalResults;
    public double totalPages;

    public double getPage() {
        return page;
    }

    public void setPage(double page) {
        this.page = page;
    }

    public List<MovieList> getResults() {
        return results;
    }

    public void setResults(List<MovieList> results) {
        this.results = results;
    }

    public double getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(double totalPages) {
        this.totalPages = totalPages;
    }

    public double getTotalResults() {

        return totalResults;
    }

    public void setTotalResults(double totalResults) {
        this.totalResults = totalResults;
    }
}
