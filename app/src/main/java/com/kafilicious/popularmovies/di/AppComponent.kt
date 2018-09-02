package com.kafilicious.popularmovies.di


import android.app.Application
import android.content.Context

import com.kafilicious.popularmovies.App
import com.kafilicious.popularmovies.data.remote.ApiService

import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    fun inject(app: App)

    fun context(): Context

    fun apiService(): ApiService

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

}
