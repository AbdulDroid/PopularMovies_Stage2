package com.kafilicious.popularmovies.utils;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

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

public class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String BASE_API_URL =
            "https://api.themoviedb.org/3/movie/";
    private static final String API_PAGE = "&page=";
    private static final String API_KEY =
            "?api_key=";
    private static final String BASE_IMAGE_URL =
            "http://image.tmdb.org/t/p/";
    private static final String DEFAULT_IMAGE_SIZE =
            "w185/";
    private static final String BACKDROP_IMAGE_SIZE =
            "w342/";
    private static final String BASE_YOUTUBE_IMG_URL =
            "https://img.youtube.com/vi/";
    private static final String END_TAG_YOUTUBE_IMAGE_URL =
            "/default.jpg";
    private static final String BASE_YOUTUBE_VIDEO_URL =
            "https://www.youtube.com/watch?v=";
    private static final String VIDEO_URL_TAG =
            "&language=en-US";
    private static final String REVIEW_URL_TAG =
            "&language=en-US&page=1";


    public static URL buildUrl(String ads){
        String address = ads;
        URL url = null;
        try{
            url = new URL(address);
        }catch (MalformedURLException e){
            e.printStackTrace();
            return null;
        }

        Log.i(TAG, "Built URL " + url);

        return url;
    }

    public static URL buildUrl(String sort, int page){
        String address = BASE_API_URL + sort + API_KEY + API_PAGE + page;
        return buildUrl(address);
    }


    public static URL buildVideoDetailsUrl(String id, int task){
        String Url = null;
        if(task == 1)
            Url = BASE_API_URL + id + "/videos" + API_KEY + VIDEO_URL_TAG;
        else if (task == 2)
            Url = BASE_API_URL + id + "/reviews" + API_KEY + REVIEW_URL_TAG;
        return buildUrl(Url);
    }

    public static URL buildYoutubeImageUrl(String key){
        String imageAds = BASE_YOUTUBE_IMG_URL + key + END_TAG_YOUTUBE_IMAGE_URL;
        return buildUrl(imageAds);
    }

    public static URL buildYoutubeVideoUrl(String key) {
        String videoUrl = BASE_YOUTUBE_VIDEO_URL + key;
        return buildUrl(videoUrl);
    }

    public static URL buildMovieUrl(String path, int task) {
        String movieAds = null;
        if (task == 0)
            movieAds = BASE_IMAGE_URL + DEFAULT_IMAGE_SIZE + path;
        else if (task == 1)
            movieAds = BASE_IMAGE_URL + BACKDROP_IMAGE_SIZE + path;

        return buildUrl(movieAds);
    }


    public static String getResponseFromHttpUrl(URL url) throws IOException{
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try{
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput){
                Log.v(TAG, "HttpRequest successful");
                return scanner.next();

            }else {
                return null;
            }
        }finally {
            urlConnection.disconnect();
        }
    }
}
