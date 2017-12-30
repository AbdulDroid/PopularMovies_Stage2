package com.kafilicious.popularmovies.ui.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kafilicious.popularmovies.adapters.MovieListAdapter;
import com.kafilicious.popularmovies.database.MovieContract;
import com.kafilicious.popularmovies.models.MovieList;
import com.kafilicious.popularmovies.R;
import com.kafilicious.popularmovies.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<MovieList>>, View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int THEMOVIEDB_SEARCH_LOADER = 0;
    private static final String SEARCH_QUERY_URL_EXTRA = "query";
    private static final String SORT_POPULAR = "popular";
    private static final String SORT_RATED = "top_rated";
    private static final String SORT_BY_FAVORITES = "favorites";
    private static final String LIST_STATE_KEY = "key_position";
    int currentPageNo = 1, totalPageNo = 1;
    long totalPages = 0;
    long totalResults = 0;
    String sortType = null;
    MovieListAdapter adapter;
    Parcelable mListState = null;
    RecyclerView.LayoutManager layoutManager;
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;
    private List<MovieList> movie_list;
    private TextView mMovieCountTextView, mPageTextView, mCurrentPageTextView, ofTextView;
    private ImageView rightArrow, leftArrow;
    private CoordinatorLayout coordinatorLayout;
    private SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Popular Movies");
        setSupportActionBar(toolbar);

        sortType = SORT_POPULAR;

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("sortPref")
                    && savedInstanceState.containsKey("pageNo")) {
                sortType = savedInstanceState.getString("sortPref");
                currentPageNo = savedInstanceState.getInt("pageNo");
            }
        }

        //Initializing variables for the various views used in the MainActivity
        mProgressBar = findViewById(R.id.progress_bar);
        mMovieCountTextView = findViewById(R.id.total_movies);
        mPageTextView = findViewById(R.id.tv_total_pages);
        mPageTextView.setVisibility(View.GONE);
        rightArrow = findViewById(R.id.right_arrow);
        rightArrow.setOnClickListener(this);
        leftArrow = findViewById(R.id.left_arrow);
        leftArrow.setVisibility(View.GONE);
        leftArrow.setOnClickListener(this);
        ofTextView = findViewById(R.id.tv_of);
        ofTextView.setVisibility(View.GONE);
        mCurrentPageTextView = findViewById(R.id.tv_page_no);
        coordinatorLayout = findViewById(R.id.activity_main);
        mCurrentPageTextView.setText(String.valueOf(currentPageNo));
        if (currentPageNo == 1)
            leftArrow.setVisibility(View.GONE);
        else
            leftArrow.setVisibility(View.VISIBLE);

        //Setting the RecyclerView to a fixed size
        mRecyclerView = findViewById(R.id.rv_movie_posters);
        mRecyclerView.setHasFixedSize(true);
        //Setting the layout manager for the RecyclerView
        final int columns = getResources().getInteger(R.integer.grid_columns);
        layoutManager = new GridLayoutManager(this, columns,
                LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        //Setting the adapter for the RecyclerView
        adapter = new MovieListAdapter(movie_list);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setNestedScrollingEnabled(false);
        fetchMovies(sortType, currentPageNo);

        LoaderCallbacks<List<MovieList>> callback = MainActivity.this;

        getSupportLoaderManager().initLoader(THEMOVIEDB_SEARCH_LOADER, null, callback);

        prefs = getSharedPreferences("com.kafilicious.popularmovies", MODE_PRIVATE);
    }

    @Override
    protected void onPause() {
        mListState = mRecyclerView.getLayoutManager().onSaveInstanceState();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (prefs.getBoolean("firstrun", true)) {
            // Do first run stuff here then set 'first run' as false
            // using the following line to edit/commit prefs
            prefs.edit().putBoolean("firstrun", false).apply();
        }
        if (sortType.equalsIgnoreCase(SORT_BY_FAVORITES))
            fetchMovies(sortType, currentPageNo);
    }

    @Override
    public Loader<List<MovieList>> onCreateLoader(int id, final Bundle args) {

        return new AsyncTaskLoader<List<MovieList>>(this) {

            List<MovieList> movies = new ArrayList<MovieList>();


            @Override
            protected void onStartLoading() {
                if (!movies.isEmpty()) {

                    deliverResult(movies);
                    Log.i(TAG, "Loader: movie list not empty, delivering results");
                } else {

                    showLoading();
                    forceLoad();
                    Log.i(TAG, "Loader: movie list empty, loading started");
                }
            }

            @Override
            public List<MovieList> loadInBackground() {
                String urlQuery = null;
                if (args != null) {
                    urlQuery = args.getString(SEARCH_QUERY_URL_EXTRA);
                    if (urlQuery == null || TextUtils.isEmpty(urlQuery)) {
                        Log.i(TAG, "Loader: Request URL is null or is empty");
                        return null;
                    }
                }

                try {

                    Log.i(TAG, "Loader: Request URL is not null and its not empty, loading started");
                    URL movieRequestUrl = new URL(urlQuery);
                    String jsonResponse =
                            NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);
                    try {
                        List<MovieList> movieLists = new ArrayList<MovieList>();
                        JSONObject object = new JSONObject(jsonResponse);
                        totalPages = object.getLong("total_pages");
                        totalPageNo = (int) totalPages;
                        totalResults = object.getLong("total_results");
                        JSONArray jsonArray = object.getJSONArray("results");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);


                            MovieList addMovies = new MovieList();
                            addMovies.overview = obj.getString("overview");
                            addMovies.releaseDate = obj.getString("release_date");
                            addMovies.title = obj.getString("title");
                            addMovies.voteAverage = obj.getDouble("vote_average");
                            addMovies.voteCount = obj.getLong("vote_count");
                            addMovies.id = obj.getInt("id");
                            addMovies.posterPath = obj.getString("poster_path");
                            addMovies.backdropPath = obj.getString("backdrop_path");
                            movieLists.add(addMovies);

                        }

                        return movieLists;

                    } catch (JSONException j) {
                        j.printStackTrace();
                        return null;
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            public void deliverResult(List<MovieList> data) {
                movies = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<MovieList>> loader, List<MovieList> data) {
        if (data != null && !data.isEmpty()) {
            updateUI();
            showViews();
            setTextViews(totalPages, totalResults);
            adapter.setMovieData(data);
            if (mListState != null)
                mRecyclerView.getLayoutManager().onRestoreInstanceState(mListState);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<MovieList>> loader) {
        adapter.setMovieData(new ArrayList<MovieList>());
    }

    public void fetchMovies(final String sort, final int page) {
        if (isNetworkAvailable()) {
            loadMovies(sort, page);

        } else {
            mProgressBar.setVisibility(View.INVISIBLE);
            mRecyclerView.setVisibility(View.INVISIBLE);
            Snackbar snackbar = Snackbar.make(coordinatorLayout, "No Internet Connect, Please" +
                    " Turn ON data and click RETRY", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("RETRY", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fetchMovies(sortType, currentPageNo);
                }
            });
            snackbar.show();
        }
    }

    public void showViews() {
        mPageTextView.setVisibility(View.VISIBLE);
        ofTextView.setVisibility(View.VISIBLE);
        mCurrentPageTextView.setVisibility(View.VISIBLE);
        rightArrow.setVisibility(View.VISIBLE);
    }

    public void hideViews() {
        mPageTextView.setVisibility(View.GONE);
        ofTextView.setVisibility(View.GONE);
        mCurrentPageTextView.setVisibility(View.GONE);
        rightArrow.setVisibility(View.GONE);
        leftArrow.setVisibility(View.GONE);
    }

    public void setTextViews(long pages, long results) {
        mMovieCountTextView.setText(String.valueOf(results));
        mPageTextView.setText(String.valueOf(pages));
    }

    public void setSubtitle(String sort_type) {
        if (getSupportActionBar() != null && sort_type != null) {
            switch (sort_type) {
                case SORT_POPULAR:
                default:
                    getSupportActionBar().setSubtitle("Most Popular Movies");
                    break;
                case SORT_RATED:
                    getSupportActionBar().setSubtitle("Top Rated Movies");
                    break;
                case SORT_BY_FAVORITES:
                    getSupportActionBar().setSubtitle("My Favorite Movies");
                    break;
            }
        }
    }

    public void updateUI() {
        mProgressBar.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
        if (currentPageNo == totalPageNo)
            rightArrow.setVisibility(View.GONE);
    }

    public void showLoading() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    public void loadMovies(String sort, int page) {
        setSubtitle(sort);
        if (sort.equalsIgnoreCase(SORT_BY_FAVORITES))
            new FetchFavorites().execute();
        else {
            URL movieRequestUrl = NetworkUtils.buildUrl(sort, page);
            Log.i(TAG, "Formed URL: " + movieRequestUrl);
            Bundle queryBundle = new Bundle();
            queryBundle.putString(SEARCH_QUERY_URL_EXTRA, movieRequestUrl.toString());

            LoaderManager loaderManager = getSupportLoaderManager();

            Loader<MovieList> movieSearchLoader = loaderManager.getLoader(THEMOVIEDB_SEARCH_LOADER);

            if (movieSearchLoader == null) {
                loaderManager.initLoader(THEMOVIEDB_SEARCH_LOADER, queryBundle, this);
                Log.i(TAG, "Loader initialized");
            } else {
                loaderManager.restartLoader(THEMOVIEDB_SEARCH_LOADER, queryBundle, this);
                Log.i(TAG, "Loader restarted");
            }
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.right_arrow:
                if (currentPageNo < totalPageNo) {
                    currentPageNo++;
                    mCurrentPageTextView.setText(String.valueOf(currentPageNo));
                    leftArrow.setVisibility(View.VISIBLE);
                    fetchMovies(sortType, currentPageNo);
                    break;
                } else if (currentPageNo == (totalPageNo - 1)) {
                    currentPageNo++;
                    mCurrentPageTextView.setText(String.valueOf(currentPageNo));
                    rightArrow.setVisibility(View.GONE);
                    fetchMovies(sortType, currentPageNo);
                    break;
                }
            case R.id.left_arrow:
                if (currentPageNo > 1) {
                    currentPageNo--;
                    mCurrentPageTextView.setText(String.valueOf(currentPageNo));
                    fetchMovies(sortType, currentPageNo);
                    break;
                } else if (currentPageNo == 2) {
                    leftArrow.setVisibility(View.INVISIBLE);
                    currentPageNo--;
                    mCurrentPageTextView.setText(String.valueOf(currentPageNo));
                    fetchMovies(sortType, currentPageNo);
                    break;
                }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about:
                //will start about activity yet to be added
                return true;
            case R.id.action_settings:
                //if setting will be later added this will implement it
                return true;
            case R.id.action_sort_popular:
                if (!sortType.equals(SORT_POPULAR)) {
                    sortType = SORT_POPULAR;
                    fetchMovies(sortType, currentPageNo);
                }
                return true;
            case R.id.action_sort_top_rated:
                if (!sortType.equals(SORT_RATED)) {
                    sortType = SORT_RATED;
                    fetchMovies(sortType, currentPageNo);
                }
                return true;
            case R.id.action_sort_favorite:
                if (!sortType.equals(SORT_BY_FAVORITES)) {
                    sortType = SORT_BY_FAVORITES;
                    fetchMovies(sortType, currentPageNo);
                }

        }

        return super.onOptionsItemSelected(item);
    }

    public boolean isNetworkAvailable() {
        boolean status;
        try {
            ConnectivityManager cm =
                    (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            status = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return status;

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("sortPref", sortType);
        outState.putInt("pageNo", currentPageNo);
        mListState = mRecyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(LIST_STATE_KEY, mListState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle state) {
        if (state != null) {
            sortType = state.getString("sortPref");
            currentPageNo = state.getInt("pageNo");
            mListState = state.getParcelable(LIST_STATE_KEY);
            super.onRestoreInstanceState(state);
        }
    }

    public class FetchFavorites extends AsyncTask<Void, Void, Cursor> {
        /*String selection = null;
        String[] selectionArgs = null;
        String sortOrder = MovieContract.MovieEntry._ID + " ASC";
        List<MovieList> movieLists = new ArrayList<MovieList>();*/
        String[] projection = {
                MovieContract.MovieEntry.COLUMN_MOVIE_ID, MovieContract.MovieEntry.COLUMN_MOVIE_TITLE,
                MovieContract.MovieEntry.COLUMN_MOVIE_POSTR_PATH, MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW,
                MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE, MovieContract.MovieEntry.COLUMN_MOVIE_BACKDROP_PATH,
                MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_COUNT, MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE
        };

        @Override
        protected void onPreExecute() {
            showLoading();
        }

        @Override
        protected Cursor doInBackground(Void... params) {
            /*Cursor mCursor = null;*/
            try {
                return getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                        projection,
                        null,
                        null,
                        null);
            } catch (Exception e) {
                Log.e(TAG, "Failed to asynchronously load data.");
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(Cursor data) {
            List<MovieList> movieList_from_cursor = new ArrayList<>();
            if (data != null) {
                if (data.moveToFirst()) {
                    do {
                        MovieList movies = new MovieList();
                        movies.id = Long.parseLong(data.getString(data.getColumnIndex(MovieContract
                                .MovieEntry.COLUMN_MOVIE_ID)));
                        movies.title = data.getString(data.getColumnIndex(MovieContract.MovieEntry
                                .COLUMN_MOVIE_TITLE));
                        movies.posterPath = data.getString(data.getColumnIndex(MovieContract.MovieEntry
                                .COLUMN_MOVIE_POSTR_PATH));
                        movies.backdropPath = data.getString(data.getColumnIndex(MovieContract
                                .MovieEntry.COLUMN_MOVIE_BACKDROP_PATH));
                        movies.overview = data.getString(data.getColumnIndex(MovieContract.MovieEntry
                                .COLUMN_MOVIE_OVERVIEW));
                        movies.releaseDate = data.getString(data.getColumnIndex(MovieContract
                                .MovieEntry.COLUMN_MOVIE_RELEASE_DATE));
                        movies.voteCount = Long.parseLong(data.getString(data
                                .getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_COUNT)));
                        movies.voteAverage = Double.parseDouble(data.getString(data.
                                getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE)));

                        movieList_from_cursor.add(movies);
                    } while (data.moveToNext());

                    movie_list = movieList_from_cursor;
                    adapter.setMovieData(movie_list);
                    if (mListState != null) {
                        mRecyclerView.getLayoutManager().onRestoreInstanceState(mListState);
                    }
                    updateUI();
                    hideViews();
                    mMovieCountTextView.setText(String.valueOf(movie_list.size()));
                } else {
                    Toast.makeText(MainActivity.this, "No Movies added yet, Please add movies",
                            Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
