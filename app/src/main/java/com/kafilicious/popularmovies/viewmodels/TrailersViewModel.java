package com.kafilicious.popularmovies.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.kafilicious.popularmovies.BuildConfig;
import com.kafilicious.popularmovies.callbacks.TrailerListener;
import com.kafilicious.popularmovies.data.remote.ApiService;
import com.kafilicious.popularmovies.data.models.remote.TrailerResponse;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrailersViewModel extends AndroidViewModel {

    ApiService apiService;
    TrailerListener listener;

    private static final String TAG = TrailersViewModel.class.getName();
    private static final MutableLiveData ABSENT = new MutableLiveData();
    {
        //noinspection unchecked
        ABSENT.setValue(null);
    }

    public void getTrailers(String type, String id) {
        apiService.getTrailers(type, id, BuildConfig.API_KEY, "en-US")
                .enqueue(new Callback<TrailerResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<TrailerResponse> call,
                                           @NonNull Response<TrailerResponse> response) {
                        if (response.code() == 200 && response.body() != null) {
                            // TODO: 29/08/2018 Add code to user response from the API
                        }

                    }

                    @Override
                    public void onFailure(@NonNull Call<TrailerResponse> call,
                                          @NonNull Throwable t) {

                    }
                });
    }

    @Inject
    public TrailersViewModel(@NonNull Application application) {
        super(application);
    }
}
