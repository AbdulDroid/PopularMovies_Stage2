package com.kafilicious.popularmovies.ui.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kafilicious.popularmovies.Database.MovieContract;
import com.kafilicious.popularmovies.Database.MovieDbHelper;
import com.kafilicious.popularmovies.R;
import com.kafilicious.popularmovies.ui.fragment.OverviewFragment;
import com.kafilicious.popularmovies.ui.fragment.ReviewsFragment;
import com.kafilicious.popularmovies.ui.fragment.TrailersFragment;
import com.kafilicious.popularmovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

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

    public static int id;
    public static String movieTitle = null;
    public static String movieOverview = null;
    private static String[] selectionArgs;
    String MOVIE_TITLE = "title";
    String MOVIE_OVERVIEW = "overview";
    String MOVIE_RELEASE = "release_date";
    String MOVIE_POSTER = "poster_path";
    String MOVIE_VOTE_AVERAGE = "vote_average";
    String MOVIE_VOTE_COUNT = "vote_count";
    String MOVIE_BACK_DROP = "backdrop_path";
    String MOVIE_ID = "id";
    String text;
    SQLiteDatabase db;
    MovieDbHelper dbHelper;
    TabLayout tabLayout;
    ViewPager viewPager;
    NestedScrollView scrollView;
    FloatingActionButton fab;
    CollapsingToolbarLayout collapsingToolbarLayout;
    LinearLayout layoutDetail;
    AppBarLayout myAppBarLayout;
    SectionPagerAdapter sectionPagerAdapter;
    TextView videoErrorTV, reviewErrorTV;
    Button favoriteButton;
    private String selection = MovieContract.MovieEntry.COLUMN_MOVIE_ID + "=?";
    private TextView titleTextView, releaseDateTextView, voteAverageTextView, ratingTextView, voteCountTextView;
    private TextView titleDetailCard, releaseDetailCard, ratingDetailCard, ratingScoreDetailCard, voteCountDetailCard;
    private ImageView backDropImageView, posterDetailCard;
    private RatingBar ratingBar, ratingBarPortrait;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_main);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        final Intent intent = this.getIntent();

        titleTextView = (TextView) findViewById(R.id.title_details);
        releaseDateTextView = (TextView) findViewById(R.id.year_details);
        ratingTextView = (TextView) findViewById(R.id.rating_score_detail);
        voteCountTextView = (TextView) findViewById(R.id.num_of_votes_detail);
        voteAverageTextView = (TextView) findViewById(R.id.vote_average_details);
        videoErrorTV = (TextView) findViewById(R.id.video_error_tv);
        reviewErrorTV = (TextView) findViewById(R.id.review_error_tv);
        ratingBar = (RatingBar) findViewById(R.id.rating_bar_detail);
        backDropImageView = (ImageView) findViewById(R.id.iv_backdrop);
        favoriteButton = (Button) findViewById(R.id.favorite);

        int orientation = this.getResources().getConfiguration().orientation;


        scrollView = (NestedScrollView) findViewById(R.id.nested_scrollView);
        if (scrollView != null) {
            scrollView.setFillViewport(true);
        }

        tabLayout = (TabLayout) findViewById(R.id.movie_details_tabLayout);
        viewPager = (ViewPager) findViewById(R.id.movie_details_container);

        dbHelper = new MovieDbHelper(this);

        sectionPagerAdapter = new SectionPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(sectionPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);

        if (movieIsStored()) {
            favoriteButton.setText(R.string.button_text_marked);
        } else {
            favoriteButton.setText(R.string.button_text);
        }
        if (intent != null && intent.hasExtra(MOVIE_TITLE)) {

            movieTitle = intent.getStringExtra(MOVIE_TITLE);
            id = Integer.parseInt(intent.getStringExtra(MOVIE_ID));
            final String movieRelease = intent.getStringExtra(MOVIE_RELEASE);
            final String movieVoteCount = intent.getStringExtra(MOVIE_VOTE_COUNT);
            movieOverview = intent.getStringExtra(MOVIE_OVERVIEW);
            final String movieVoteAverage = intent.getStringExtra(MOVIE_VOTE_AVERAGE);
            final String moviePoster = intent.getStringExtra(MOVIE_POSTER);
            final String movieBackdrop = intent.getStringExtra(MOVIE_BACK_DROP);

            getSupportActionBar().setTitle(intent.getStringExtra(MOVIE_TITLE) + " (" +
                    intent.getStringExtra(MOVIE_RELEASE).substring(0, 4) + ")");

            selectionArgs = new String[]{String.valueOf(id)};

            titleTextView.setText(movieTitle);
            releaseDateTextView.setText(movieRelease.substring(0, 4));
            voteAverageTextView.setText(movieVoteAverage + "/10");
            voteCountTextView.setText(movieVoteCount);
            String url2 = NetworkUtils.buildMovieUrl(movieBackdrop, 1).toString();
            Picasso.with(this)
                    .load(url2)
                    .into(backDropImageView);

            double voteAverage = Double.parseDouble(movieVoteAverage);
            voteAverage = (voteAverage / 10) * 5;
            String rating = String.format("%.1f", voteAverage);
            voteAverage = Double.parseDouble(rating);
            ratingTextView.setText(rating);
            ratingBar.setRating((float) voteAverage);
            ratingBar.setStepSize((float) 0.1);

            text = intent.getStringExtra(MOVIE_TITLE) + "(" +
                    intent.getStringExtra(MOVIE_RELEASE).substring(0, 4) + ")";

            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                favoriteButton.setVisibility(View.GONE);
                fab = (FloatingActionButton) findViewById(R.id.fab);
                collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar);
                layoutDetail = (LinearLayout) findViewById(R.id.layout_detail_card);
                layoutDetail.setVisibility(View.GONE);
                posterDetailCard = (ImageView) findViewById(R.id.poster_detail_card);
                titleDetailCard = (TextView) findViewById(R.id.movie_title);
                releaseDetailCard = (TextView) findViewById(R.id.movie_release_date);
                ratingDetailCard = (TextView) findViewById(R.id.rating);
                ratingScoreDetailCard = (TextView) findViewById(R.id.rating_score_details);
                voteCountDetailCard = (TextView) findViewById(R.id.num_of_votes_details);
                ratingBarPortrait = (RatingBar) findViewById(R.id.rating_bar_details);
                myAppBarLayout = (AppBarLayout) findViewById(R.id.myAppbar);

                if (movieIsStored()) {
                    fab.setImageResource(R.drawable.ic_star_black_24dp);
                } else {
                    fab.setImageResource(R.drawable.ic_star_border_black_24dp);
                }

                myAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                    @Override
                    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                        if (i == -collapsingToolbarLayout.getHeight() + toolbar.getHeight()) {
                            collapsingToolbarLayout.setCollapsedTitleTextColor(getResources()
                                    .getColor(android.R.color.white));
                            layoutDetail.setVisibility(View.VISIBLE);
                        } else {
                            layoutDetail.setVisibility(View.GONE);
                            collapsingToolbarLayout.setExpandedTitleColor(getResources()
                                    .getColor(R.color.transparent));
                        }
                    }
                });

                titleDetailCard.setText(movieTitle);
                releaseDetailCard.setText(movieRelease);
                ratingDetailCard.setText(movieVoteAverage + "/10");
                voteCountDetailCard.setText(movieVoteCount);
                ratingScoreDetailCard.setText(rating);
                ratingBarPortrait.setRating((float) voteAverage);
                ratingBarPortrait.setStepSize((float) 0.1);
                ratingBarPortrait.setNumStars(5);
                String url = NetworkUtils.buildMovieUrl(moviePoster, 0).toString();
                Picasso.with(this)
                        .load(url)
                        .into(posterDetailCard);
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (movieIsStored()) {
                            fab.setImageResource(R.drawable.ic_star_border_black_24dp);
                            int deletedMovie = getContentResolver().delete(MovieContract.MovieEntry
                                    .CONTENT_URI, selection, selectionArgs);
                            Log.i("Movie deleted", String.valueOf(deletedMovie));
                            Toast.makeText(DetailActivity.this, movieTitle +
                                    " has being removed from My Favorites", Toast.LENGTH_LONG).show();
                        } else {
                            fab.setImageResource(R.drawable.ic_star_black_24dp);
                            ContentValues values = new ContentValues();
                            values.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE, movieTitle);
                            values.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, String.valueOf(id));
                            values.put(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW, movieOverview);
                            values.put(MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_COUNT, movieVoteCount);
                            values.put(MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE, movieVoteAverage);
                            values.put(MovieContract.MovieEntry.COLUMN_MOVIE_BACKDROP_PATH, movieBackdrop);
                            values.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTR_PATH, moviePoster);
                            values.put(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE, movieRelease);
                            Uri updatedMovie = getContentResolver().insert(MovieContract.MovieEntry
                                    .CONTENT_URI, values);
                            Log.i("Movie Added ", String.valueOf(updatedMovie) + " | Title: " + movieTitle);
                            Toast.makeText(DetailActivity.this, movieTitle +
                                    " has being added to My Favorites", Toast.LENGTH_LONG).show();
                        }

                    }
                });

            } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                favoriteButton.setVisibility(View.VISIBLE);
                favoriteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (movieIsStored()) {
                            favoriteButton.setText(R.string.button_text);
                            int deletedMovie = getContentResolver().delete(MovieContract.MovieEntry
                                    .CONTENT_URI, selection, selectionArgs);
                            Log.i("Movie deleted", String.valueOf(deletedMovie));
                            Toast.makeText(DetailActivity.this, movieTitle +
                                    " has being removed from My Favorites", Toast.LENGTH_LONG).show();
                        } else {
                            favoriteButton.setText(R.string.button_text_marked);
                            ContentValues values = new ContentValues();
                            values.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE, movieTitle);
                            values.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, String.valueOf(id));
                            values.put(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW, movieOverview);
                            values.put(MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_COUNT, movieVoteCount);
                            values.put(MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE, movieVoteAverage);
                            values.put(MovieContract.MovieEntry.COLUMN_MOVIE_BACKDROP_PATH, movieBackdrop);
                            values.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTR_PATH, moviePoster);
                            values.put(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE, movieRelease);
                            Uri updatedMovie = getContentResolver().insert(MovieContract.MovieEntry
                                    .CONTENT_URI, values);
                            Log.i("Movie Added", String.valueOf(updatedMovie) + " | Title: " + movieTitle);
                            Toast.makeText(DetailActivity.this, movieTitle +
                                    " has being added to My Favorites", Toast.LENGTH_LONG).show();
                        }

                    }
                });
            }

            Log.i("Results", "ID set successful");

        }
    }

    private boolean movieIsStored() {
        db = dbHelper.getReadableDatabase();
        String whereClause = MovieContract.MovieEntry.COLUMN_MOVIE_ID + "=?";
        String[] whereArgs = {String.valueOf(id)};
        Cursor cursor = db.query(
                MovieContract.MovieEntry.TABLE_ONE_NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        if (cursor.getCount() == 0) {
            cursor.close();
            return false;

        } else {
            cursor.close();
            return true;

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private class SectionPagerAdapter extends FragmentPagerAdapter {

        public SectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                default:
                    return OverviewFragment.newInstance();
                case 1:
                    return TrailersFragment.newInstance();
                case 2:
                    return ReviewsFragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Overview";
                case 1:
                    return "Trailers";
                case 2:
                    return "Reviews";
            }
            return null;
        }
    }
}
