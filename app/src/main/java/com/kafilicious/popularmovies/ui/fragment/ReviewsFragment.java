package com.kafilicious.popularmovies.ui.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kafilicious.popularmovies.adapters.ReviewAdapter;
import com.kafilicious.popularmovies.data.models.db.Review;
import com.kafilicious.popularmovies.R;
import com.kafilicious.popularmovies.ui.activity.DetailActivity;
import com.kafilicious.popularmovies.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abdulkarim on 5/14/2017.
 */

public class ReviewsFragment extends Fragment {

    TextView errorMessageTextView;
    RecyclerView reviewRecyclerView;
    ReviewAdapter rAdapter;
    List<Review> reviewList;
    ProgressBar reviewsProgressBar;
    int movieId;

    public static ReviewsFragment newInstance() {
        return new ReviewsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review, container, false);

        reviewsProgressBar = view.findViewById(R.id.progressbar_review);
        errorMessageTextView = view.findViewById(R.id.review_error_tv);
        reviewRecyclerView = view.findViewById(R.id.review_rv);
        reviewRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        reviewRecyclerView.setLayoutManager(layoutManager);

        movieId = DetailActivity.id;
        new FetchReviewTask().execute(String.valueOf(movieId));
        return view;
    }

    public void showReviewErrorMessage() {
        reviewsProgressBar.setVisibility(View.INVISIBLE);
        reviewRecyclerView.setVisibility(View.INVISIBLE);
        errorMessageTextView.setVisibility(View.VISIBLE);
        String text = String.format(getResources().getString(R.string.review_error_message),
                DetailActivity.movieTitle);
        errorMessageTextView.setText(text);
    }

    public void showLoading() {
        reviewRecyclerView.setVisibility(View.INVISIBLE);
        errorMessageTextView.setVisibility(View.INVISIBLE);
        reviewsProgressBar.setVisibility(View.VISIBLE);
    }

    public void showData() {
        reviewsProgressBar.setVisibility(View.INVISIBLE);
        errorMessageTextView.setVisibility(View.INVISIBLE);
        reviewRecyclerView.setVisibility(View.VISIBLE);
    }


    public class FetchReviewTask extends AsyncTask<String, Void, Void> {


        @Override
        protected void onPreExecute() {
            showLoading();
        }

        @Override
        protected Void doInBackground(String... params) {
            String ids1 = params[0];

            reviewList = new ArrayList<>();
            try {
                URL url = NetworkUtils.buildVideoDetailsUrl(ids1, 2);
                String json = NetworkUtils.getResponseFromHttpUrl(url);
                try {
                    reviewList.clear();
                    JSONObject obj = new JSONObject(json);
                    JSONArray results = obj.getJSONArray("results");

                    for (int i = 0; i < results.length(); i++) {
                        JSONObject object = results.getJSONObject(i);

                        Review result = new Review();
                        result.setId(object.getString("id"));
                        result.setAuthor(object.getString("author"));
                        result.setContent(object.getString("content"));
                        result.setUrl(object.getString("url"));
                        reviewList.add(result);
                        Log.i("Results", "review results updated");
                    }
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (reviewList.isEmpty()) {
                showReviewErrorMessage();
            } else {
                showData();
                rAdapter = new ReviewAdapter(getActivity(), reviewList);
                reviewRecyclerView.setAdapter(rAdapter);
                rAdapter.setData(reviewList);
                Log.i("Results", "Adapter data updated");
            }
        }
    }
}
