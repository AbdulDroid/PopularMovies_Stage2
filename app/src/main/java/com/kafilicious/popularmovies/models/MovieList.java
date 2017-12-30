package com.kafilicious.popularmovies.models;

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

import java.util.List;

public class MovieList {

    public String posterPath;
    public boolean adult;
    public String overview;
    public String releaseDate;
    public List<Long> genreIds = null;
    public long id;
    public String originalTitle;
    public String originalLanguage;
    public String title;
    public String backdropPath;
    public double popularity;
    public long voteCount;
    public boolean video;
    public double voteAverage;

}