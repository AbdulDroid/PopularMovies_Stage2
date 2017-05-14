package com.kafilicious.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kafilicious.popularmovies.Adapters.ReviewAdapter;
import com.kafilicious.popularmovies.Adapters.VideoAdapter;
import com.kafilicious.popularmovies.Database.MovieContract;
import com.kafilicious.popularmovies.Models.ReviewResults;
import com.kafilicious.popularmovies.Models.VideoResults;
import com.kafilicious.popularmovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

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

public class DetailActivity extends AppCompatActivity {

    String MOVIE_TITLE = "title";
    String MOVIE_OVERVIEW = "overview";
    String MOVIE_RELEASE = "release_date";
    String MOVIE_POSTER = "poster_path";
    String MOVIE_VOTE_AVERAGE = "vote_average";
    String MOVIE_VOTE_COUNT = "vote_count";
    String MOVIE_BACK_DROP = "backdrop_path";
    String MOVIE_ID = "id";
    String text;
    private ArrayList<VideoResults>video_results;
    private ArrayList<ReviewResults>review_results;
    ReviewAdapter rAdapter;
    VideoAdapter vAdapter;
    RecyclerView reviewRecyclerView, videoRecyclerView;
    TextView videoErrorTV, reviewErrorTV;
    Button favoriteButton;
    private ContentValues contentValues = new ContentValues();
    private String selection = MovieContract.MovieEntry.COLUMN_MOVIE_ID + "=?";
    private static String[] selectionArgs;

    private TextView titleTextView, releaseDateTextView,ratingTextView,voteCountTextView,overviewTextView,voteAverageTextView;
    private ImageView posterImageView, backDropImageView;
    private RatingBar ratingBar;
    private CollapsingToolbarLayout collapsingToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_main);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        final Intent intent = this.getIntent();

        video_results = new ArrayList<VideoResults>();
        review_results = new ArrayList<ReviewResults>();
        titleTextView = (TextView) findViewById(R.id.title_details);
        releaseDateTextView = (TextView) findViewById(R.id.year_details);
        ratingTextView = (TextView) findViewById(R.id.rating_score_detail);
        voteAverageTextView = (TextView) findViewById(R.id.average_vote);
        overviewTextView = (TextView) findViewById(R.id.plot_synopsis);
        voteCountTextView = (TextView) findViewById(R.id.num_of_votes_detail);
        videoErrorTV = (TextView) findViewById(R.id.video_error_tv);
        reviewErrorTV = (TextView) findViewById(R.id.review_error_tv);
        ratingBar = (RatingBar) findViewById(R.id.rating_bar_detail);
        posterImageView = (ImageView) findViewById(R.id.iv_movie_poster_details);
        backDropImageView = (ImageView) findViewById(R.id.iv_backdrop);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);
        reviewRecyclerView = (RecyclerView) findViewById(R.id.review_rv);
        videoRecyclerView = (RecyclerView) findViewById(R.id.video_rv);
        favoriteButton = (Button) findViewById(R.id.favorite);
        reviewRecyclerView.setHasFixedSize(true);
        videoRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        reviewRecyclerView.setLayoutManager(layoutManager);
        videoRecyclerView.setLayoutManager(layoutManager1);
        rAdapter =  new ReviewAdapter(review_results);
        reviewRecyclerView.setAdapter(rAdapter);
        vAdapter = new VideoAdapter(this, video_results);
        videoRecyclerView.setAdapter(vAdapter);

        if (intent != null && intent.hasExtra(MOVIE_TITLE)){
            actionBar.setTitle(intent.getStringExtra(MOVIE_TITLE) + " (" +
                    intent.getStringExtra(MOVIE_RELEASE).substring(0,4) + ")");
            collapsingToolbar.setTitle(intent.getStringExtra(MOVIE_TITLE) + " (" +
                    intent.getStringExtra(MOVIE_RELEASE).substring(0,4) + ")");
            collapsingToolbar.setExpandedTitleColor(getResources()
                    .getColor(android.R.color.darker_gray));
            collapsingToolbar.setCollapsedTitleTextColor(getResources()
                    .getColor(android.R.color.white));
            collapsingToolbar.setContentScrimColor(getResources()
                    .getColor(R.color.colorPrimary));
            collapsingToolbar.setStatusBarScrimColor(getResources()
                    .getColor(R.color.colorPrimaryDark));

            final String movieTitle = intent.getStringExtra(MOVIE_TITLE);
            final String id = intent.getStringExtra(MOVIE_ID);
            final String movieRelease = intent.getStringExtra(MOVIE_RELEASE);
            final String movieVoteCount = intent.getStringExtra(MOVIE_VOTE_COUNT);
            final String movieOverview = intent.getStringExtra(MOVIE_OVERVIEW);
            final String movieVoteAverage = intent.getStringExtra(MOVIE_VOTE_AVERAGE);
            final String moviePoster = intent.getStringExtra(MOVIE_POSTER);
            final String movieBackdrop = intent.getStringExtra(MOVIE_BACK_DROP);

            selectionArgs = new String[]{id};


            titleTextView.setText(movieTitle);
            releaseDateTextView.setText(movieRelease);
            voteAverageTextView.setText(movieVoteAverage + "/10");
            voteCountTextView.setText(movieVoteCount);
            overviewTextView.setText(movieOverview);
            String url1 = NetworkUtils.buildMovieUrl(moviePoster).toString();
            Picasso.with(this)
                    .load(url1)
                    .into(posterImageView);

            String url2 = NetworkUtils.buildMovieUrl(movieBackdrop).toString();
            Picasso.with(this)
                    .load(url2)
                    .into(backDropImageView);

            double voteAverage = Double.parseDouble(movieVoteAverage);
            voteAverage = (voteAverage/10)*5;
            String rating = String.format("%.1f",voteAverage);
            voteAverage = Double.parseDouble(rating);
            ratingTextView.setText(rating);
            ratingBar.setRating((float)voteAverage);
            ratingBar.setStepSize((float)0.1);

            text = intent.getStringExtra(MOVIE_TITLE) + "(" +
                    intent.getStringExtra(MOVIE_RELEASE).substring(0,4) + ")";

            Log.i("Results", "ID set successful");
            new FetchVideosDetails().execute(id);
            new FetchReviewTask().execute(id);

            favoriteButton.setOnClickListener(new View.OnClickListener() {
                boolean clicked = false;
                @Override
                public void onClick(View v) {

                    if (clicked){
                        favoriteButton.setText(R.string.button_text);
                        int deletedMovie = getContentResolver().delete(MovieContract.MovieEntry
                                        .CONTENT_URI, selection, selectionArgs);
                        Log.i("Movie deleted", String.valueOf(deletedMovie));
                        Toast.makeText(DetailActivity.this, movieTitle +
                                " has being removed from My Favorites", Toast.LENGTH_LONG).show();
                        clicked = false;
                    }else{
                        favoriteButton.setText(R.string.button_text_marked);
                        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE, movieTitle);
                        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, id);
                        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW, movieOverview);
                        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_COUNT,movieVoteCount);
                        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE, movieVoteAverage);
                        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_BACKDROP_PATH, movieBackdrop);
                        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTR_PATH, moviePoster);
                        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE, movieRelease);
                        Uri updatedMovie = getContentResolver().insert(MovieContract.MovieEntry
                                .CONTENT_URI, contentValues);
                        Log.i("Movie Added", String.valueOf(updatedMovie) + " | Title: " + movieTitle);
                        Toast.makeText(DetailActivity.this, movieTitle +
                                " has being added to My Favorites", Toast.LENGTH_LONG).show();
                        clicked = true;
                    }

                }
            });



        }
    }

    public class FetchVideosDetails extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            String ids = params[0];

            try{
                URL url = NetworkUtils.buildVideoDetailsUrl(ids, 1);
                String json = NetworkUtils.getResponseFromHttpUrl(url);
                try{
                    video_results.clear();
                    JSONObject object = new JSONObject(json);
                    JSONArray result = object.getJSONArray("results");
                    for (int i = 0; i<result.length(); i++){
                        JSONObject obj = result.getJSONObject(i);

                        VideoResults results = new VideoResults();
                        results.type = obj.getString("type");
                        results.key = obj.getString("key");
                        results.site = obj.getString("site");
                        results.name = obj.getString("name");

                        if (results.type.equals("Trailer") || results.type.equals("Teaser")) {
                            video_results.add(results);
//                            Log.i("Results", "video results updated");
                        }
                    }
                }catch(JSONException e){
                    e.printStackTrace();
                }
            }catch (IOException e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(video_results.isEmpty()){
                showVideoErrorMessage();
            }else {
                vAdapter.setData(video_results);
                Log.i("Results", "Adapter data updated");
            }
        }
    }

    public class FetchReviewTask extends AsyncTask<String, Void, Void>{

        @Override
        protected Void doInBackground(String... params) {
            String ids1 = params[0];
            try{
                URL url = NetworkUtils.buildVideoDetailsUrl(ids1, 2);
                String json = NetworkUtils.getResponseFromHttpUrl(url);
                try{
                    review_results.clear();
                    JSONObject obj = new JSONObject(json);
                    JSONArray results = obj.getJSONArray("results");

                    for (int i = 0; i < results.length(); i++){
                        JSONObject object = results.getJSONObject(i);

                        ReviewResults result = new ReviewResults();
                        result.author = object.getString("author");
                        result.content = object.getString("content");
                        review_results.add(result);
                        Log.i("Results", "review results updated");
                    }
                }catch (JSONException ex){
                    ex.printStackTrace();
                }
            }catch (IOException ex){
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(review_results.isEmpty()){
                showReviewErrorMessage();
            }else {
                rAdapter.setData(review_results);
                Log.i("Results", "Adapter data updated");
            }
        }
    }

    public void showVideoErrorMessage(){
        videoRecyclerView.setVisibility(View.INVISIBLE);
        videoErrorTV.setVisibility(View.VISIBLE);
        videoErrorTV.setText(text);
    }

    public void showReviewErrorMessage(){
        reviewRecyclerView.setVisibility(View.INVISIBLE);
        reviewErrorTV.setVisibility(View.VISIBLE);
        text = String.format(getResources().getString(R.string.review_error_message),text);
        reviewErrorTV.setText(text);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
