package com.kafilicious.popularmovies

import android.app.Activity
import android.app.Application
import com.kafilicious.popularmovies.di.AppModule

import javax.inject.Inject

import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector

class App : Application(), HasActivityInjector {

    @Inject
    internal var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>? = null

    override fun onCreate() {
        super.onCreate()

        DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
                .inject(this)

    }


    override fun activityInjector(): AndroidInjector<Activity>? {
        return dispatchingAndroidInjector
    }
}
