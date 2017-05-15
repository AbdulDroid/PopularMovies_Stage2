package com.kafilicious.popularmovies.ui.fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kafilicious.popularmovies.Adapters.VideoAdapter;
import com.kafilicious.popularmovies.Models.VideoResults;
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

public class TrailersFragment extends Fragment {

    TextView errorMessageTextView;
    ProgressBar trailersProgressBar;
    RecyclerView trailerRecyclerView;
    VideoAdapter vAdapter;
    int movieId;
    List<VideoResults> videosList;

    public static TrailersFragment newInstance() {
        TrailersFragment fragment = new TrailersFragment();
        return fragment;
    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trailers, container, false);
        trailersProgressBar = (ProgressBar) view.findViewById(R.id.progressbar_trailer);
        errorMessageTextView = (TextView) view.findViewById(R.id.video_error_tv);
        trailerRecyclerView = (RecyclerView) view.findViewById(R.id.video_rv);
        trailerRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        RecyclerView.LayoutManager gridLayout = new GridLayoutManager(getActivity(), 2, LinearLayoutManager.VERTICAL, false);

        if (isTablet(getActivity()))
            trailerRecyclerView.setLayoutManager(gridLayout);
        else
            trailerRecyclerView.setLayoutManager(layoutManager1);

        vAdapter = new VideoAdapter(getActivity(), videosList);
        trailerRecyclerView.setAdapter(vAdapter);


        movieId = DetailActivity.id;
        new FetchVideosDetails().execute(String.valueOf(movieId));
        return view;
    }

    public void showVideoErrorMessage() {
        trailersProgressBar.setVisibility(View.INVISIBLE);
        trailerRecyclerView.setVisibility(View.INVISIBLE);
        errorMessageTextView.setVisibility(View.VISIBLE);
        String text = String.format(getResources().getString(R.string.video_error_message), DetailActivity.movieTitle);
        errorMessageTextView.setText(text);
    }

    public void showLoading() {
        errorMessageTextView.setVisibility(View.INVISIBLE);
        trailerRecyclerView.setVisibility(View.INVISIBLE);
        trailersProgressBar.setVisibility(View.VISIBLE);
    }

    public void showData() {
        trailersProgressBar.setVisibility(View.INVISIBLE);
        errorMessageTextView.setVisibility(View.INVISIBLE);
        trailerRecyclerView.setVisibility(View.VISIBLE);
    }

    public class FetchVideosDetails extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            showLoading();
        }

        @Override
        protected Void doInBackground(String... params) {
            String ids = params[0];
            videosList = new ArrayList<VideoResults>();

            try {
                URL url = NetworkUtils.buildVideoDetailsUrl(ids, 1);
                String json = NetworkUtils.getResponseFromHttpUrl(url);
                try {
                    videosList.clear();
                    JSONObject object = new JSONObject(json);
                    JSONArray result = object.getJSONArray("results");
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject obj = result.getJSONObject(i);

                        VideoResults results = new VideoResults();
                        results.type = obj.getString("type");
                        results.key = obj.getString("key");
                        results.site = obj.getString("site");
                        results.name = obj.getString("name");
                        videosList.add(results);
//                            Log.i("Results", "video results updated");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (videosList.isEmpty()) {
                showVideoErrorMessage();
            } else {
                showData();
                vAdapter.setData(videosList);
                Log.i("Results", "Adapter data updated");
            }
        }
    }
}
