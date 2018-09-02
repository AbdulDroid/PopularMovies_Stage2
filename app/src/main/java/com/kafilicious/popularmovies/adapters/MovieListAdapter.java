package com.kafilicious.popularmovies.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.kafilicious.popularmovies.data.models.db.Movie;
import com.kafilicious.popularmovies.R;
import com.kafilicious.popularmovies.ui.activity.DetailActivity;
import com.kafilicious.popularmovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

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

    /*private static final int TYPE_CURSOR = 1;
    private static final int TYPE_ARRAY = 2;*/
    private Context context;/*
    private Cursor mCursor = null;*/
    private List<Movie> movie_list;


    public MovieListAdapter(List<Movie> movies) {
        this.movie_list = movies;
    }

    public void setMovieData(List<Movie> movieData) {
        movie_list = movieData;
        notifyDataSetChanged();
    }

    /*public Cursor swapCursor(Cursor c) {
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
    }*/

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.movie_list_item, parent,
                false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String movieTitle;
        long movieVoters;
        String movieRelease;
        String moviePoster;
        float rating_score;
        movieTitle = movie_list.get(position).getTitle();
        movieVoters = movie_list.get(position).getVoteCount();
        rating_score = (float) (movie_list.get(position).getVoteAverage() / 10) * 5;
        movieRelease = movie_list.get(position).getReleaseDate().substring(0, 4);
        moviePoster = movie_list.get(position).getPosterPath();

        holder.titleTextView.setText(movieTitle);
        holder.voterTextView.setText(String.valueOf(movieVoters));
        holder.releaseTextView.setText(movieRelease);
        holder.ratingTextView.setText(String.format(Locale.getDefault(),"%.1f",
                rating_score));
        holder.ratingBar.setRating(rating_score);
        String url = NetworkUtils.buildMovieUrl(moviePoster, 0).toString();
        Picasso.get()
                .load(url)
                .into(holder.posterImageView);
    }

    @Override
    public int getItemCount() {
        if (null == movie_list) return 0;
        return movie_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView titleTextView, ratingTextView, voterTextView, releaseTextView;
        ImageView posterImageView;
        RatingBar ratingBar;

        ViewHolder(View view) {
            super(view);
            view.setClickable(true);
            view.setOnClickListener(this);
            titleTextView = view.findViewById(R.id.tv_movie_title);
            ratingTextView = view.findViewById(R.id.rating_score);
            voterTextView = view.findViewById(R.id.num_of_votes);
            releaseTextView = view.findViewById(R.id.year);
            ratingBar = view.findViewById(R.id.rating_bar);
            posterImageView = view.findViewById(R.id.iv_poster);
            ratingBar.setStepSize((float) 0.1);
            context = view.getContext();
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Intent movieDetail = new Intent(context, DetailActivity.class);
            movieDetail.putExtra("title", movie_list.get(position).getTitle());
            movieDetail.putExtra("overview", movie_list.get(position).getOverview());
            movieDetail.putExtra("poster_path", movie_list.get(position).getPosterPath());
            movieDetail.putExtra("backdrop_path", movie_list.get(position).getBackdropPath());
            movieDetail.putExtra("release_date", movie_list.get(position).getReleaseDate());
            movieDetail.putExtra("vote_average",
                    String.valueOf(movie_list.get(position).getVoteAverage()));
            movieDetail.putExtra("vote_count",
                    String.valueOf(movie_list.get(position).getVoteCount()));
            movieDetail.putExtra("id", String.valueOf(movie_list.get(position).getId()));
            context.startActivity(movieDetail);
        }
    }

}
