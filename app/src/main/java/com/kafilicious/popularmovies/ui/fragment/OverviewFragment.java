package com.kafilicious.popularmovies.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kafilicious.popularmovies.R;
import com.kafilicious.popularmovies.ui.activity.DetailActivity;

/**
 * Created by Abdulkarim on 5/14/2017.
 */

public class OverviewFragment extends Fragment {

    TextView overviewTextView;
    String movieOverview = DetailActivity.movieOverview;

    public static OverviewFragment newInstance() {
        OverviewFragment fragment = new OverviewFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_overview, container, false);

        overviewTextView = (TextView) view.findViewById(R.id.tv_movie_overview);
        getMovieOverview();
        return view;
    }

    public void getMovieOverview() {
        Intent getData = getActivity().getIntent();

        if (getData != null) {
            overviewTextView.setText(getData
                    .getStringExtra("overview"));
        }
    }
}
