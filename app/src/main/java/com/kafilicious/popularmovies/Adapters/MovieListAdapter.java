package com.kafilicious.popularmovies.Adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.kafilicious.popularmovies.Models.MovieList;
import com.kafilicious.popularmovies.R;
import com.kafilicious.popularmovies.ui.activity.DetailActivity;
import com.kafilicious.popularmovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

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

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder> {


    private static final int TYPE_CURSOR = 1;
    private static final int TYPE_ARRAY = 2;
    Context context;
    String MOVIE_TITLE = "title";
    String MOVIE_OVERVIEW = "overview";
    String MOVIE_RELEASE = "release_date";
    String MOVIE_POSTER = "poster_path";
    String MOVIE_VOTE_AVERAGE = "vote_average";
    String MOVIE_VOTE_COUNT = "vote_count";
    String MOVIE_BACK_DROP = "backdrop_path";
    String MOVIE_ID = "id";
    Cursor mCursor = null;
    private List<MovieList> movie_list;


    public MovieListAdapter(List<MovieList> movies) {
        this.movie_list = movies;
    }

    public void setMovieData(List<MovieList> movieData) {
        movie_list = movieData;
        notifyDataSetChanged();
    }

    public Cursor swapCursor(Cursor c) {
        //check if the new Cursor is the same as the local Cursor
        if (mCursor == c) {
            return null;
        }
        Cursor temp = mCursor;//keep a copy of the local Cursor
        this.mCursor = c; //new Cursor value assigned to the local Cursor
        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.movie_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        String movieTitle = null;
        long movieVoters = 0;
        String movieRelease = null;
        String moviePoster = null;
        float rating_score = 0;
        movieTitle = movie_list.get(position).title;
        movieVoters = movie_list.get(position).voteCount;
        rating_score = (float) (movie_list.get(position).voteAverage / 10) * 5;
        movieRelease = movie_list.get(position).releaseDate.substring(0, 4);
        moviePoster = movie_list.get(position).posterPath;

        holder.titleTextView.setText(movieTitle);
        holder.voterTextView.setText(String.valueOf(movieVoters));
        holder.releaseTextView.setText(movieRelease);
        holder.ratingTextView.setText(String.format("%.1f", rating_score));
        holder.ratingBar.setRating(rating_score);
        String url = NetworkUtils.buildMovieUrl(moviePoster, 0).toString();
        Picasso.with(context).load(url)
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.posterImageView);
    }

    @Override
    public int getItemCount() {
        if (null == movie_list) return 0;
        return movie_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView titleTextView, ratingTextView, voterTextView, releaseTextView;
        public final ImageView posterImageView;
        public final RatingBar ratingBar;

        public ViewHolder(View view) {
            super(view);
            view.setClickable(true);
            view.setOnClickListener(this);
            titleTextView = (TextView) view.findViewById(R.id.tv_movie_title);
            ratingTextView = (TextView) view.findViewById(R.id.rating_score);
            voterTextView = (TextView) view.findViewById(R.id.num_of_votes);
            releaseTextView = (TextView) view.findViewById(R.id.year);
            ratingBar = (RatingBar) view.findViewById(R.id.rating_bar);
            posterImageView = (ImageView) view.findViewById(R.id.iv_poster);
            ratingBar.setStepSize((float) 0.1);
            context = view.getContext();
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Intent movieDetail = new Intent(context, DetailActivity.class);
            movieDetail.putExtra(MOVIE_TITLE, movie_list.get(position).title);
            movieDetail.putExtra(MOVIE_OVERVIEW, movie_list.get(position).overview);
            movieDetail.putExtra(MOVIE_POSTER, movie_list.get(position).posterPath);
            movieDetail.putExtra(MOVIE_BACK_DROP, movie_list.get(position).backdropPath);
            movieDetail.putExtra(MOVIE_RELEASE, movie_list.get(position).releaseDate);
            movieDetail.putExtra(MOVIE_VOTE_AVERAGE, String.valueOf(movie_list.get(position).voteAverage));
            movieDetail.putExtra(MOVIE_VOTE_COUNT, String.valueOf(movie_list.get(position).voteCount));
            movieDetail.putExtra(MOVIE_ID, String.valueOf(movie_list.get(position).id));
            context.startActivity(movieDetail);
        }
    }

}
