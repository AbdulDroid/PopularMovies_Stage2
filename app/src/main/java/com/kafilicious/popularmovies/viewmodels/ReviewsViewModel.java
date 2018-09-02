package com.kafilicious.popularmovies.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.kafilicious.popularmovies.BuildConfig;
import com.kafilicious.popularmovies.data.remote.ApiService;
import com.kafilicious.popularmovies.data.models.remote.ReviewResponse;
import com.kafilicious.popularmovies.data.models.db.Review;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewsViewModel extends AndroidViewModel {

    @Inject
    ApiService apiService;

    private static final String TAG = ReviewsViewModel.class.getName();
    private static final MutableLiveData ABSENT = new MutableLiveData();
    {
        //noinspection unchecked
        ABSENT.setValue(null);
    }

    List<Review> getReviews(String type, String id, int page) {
        final List<Review>[] results = new List[1];
        apiService.getReviews(type, id, BuildConfig.API_KEY, "en-US", page)
                .enqueue(new Callback<ReviewResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ReviewResponse> call,
                                           @NonNull Response<ReviewResponse> response) {
                        if (response.code() == 200)
                            results[0] = response.body().getResults();
                        else
                            results[0] = null;
                    }

                    @Override
                    public void onFailure(@NonNull Call<ReviewResponse> call,
                                          @NonNull Throwable t) {
                        results[0] = null;
                    }
                });

        return results[0];
    }

    @Inject
    public ReviewsViewModel(@NonNull Application application) {
        super(application);
    }
}
