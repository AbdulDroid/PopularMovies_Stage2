package com.kafilicious.popularmovies.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import javax.inject.Inject;

public class MovieListViewModel extends AndroidViewModel{

    private static final String TAG = MovieListViewModel.class.getName();
    private static final MutableLiveData ABSENT = new MutableLiveData();
    {
        //noinspection unchecked
        ABSENT.setValue(null);
    }

    @Inject
    public MovieListViewModel(@NonNull Application application) {
        super(application);
    }
}
