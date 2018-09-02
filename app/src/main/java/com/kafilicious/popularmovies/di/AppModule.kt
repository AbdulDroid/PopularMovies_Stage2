package com.kafilicious.popularmovies.di

import android.content.Context
import com.google.gson.Gson
import com.kafilicious.popularmovies.BuildConfig
import com.kafilicious.popularmovies.data.remote.ApiService

import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
class AppModule(val context: Context) {

    @Provides
    @Singleton
    fun providesContext(): Context {
        return context
    }

    @Singleton
    @Provides
    internal fun providesApiService(gsonConverterFactory: GsonConverterFactory,
                                    okHttpClient: OkHttpClient): ApiService {
        return Retrofit.Builder()
                .baseUrl(ApiService.BASE_URL)
                .addConverterFactory(gsonConverterFactory)
                .client(okHttpClient)
                .build()
                .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun providesOkHttpClient(cache: Cache): OkHttpClient {
        val client = OkHttpClient.Builder()
                .cache(cache)
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)

        return client.build()
    }

    @Provides
    @Singleton
    fun providesOkhttpCache(context: Context): Cache {
        val cacheSize = 10 * 1024 * 1024 // 10 MB
        return Cache(context.cacheDir, cacheSize.toLong())
    }

    @Provides
    @Singleton
    fun providesGson(): Gson {
        return Gson()
    }

    @Provides
    @Singleton
    fun providesGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }
}
