package com.alorma.myapplication.di.module

import android.content.Context
import com.alorma.myapplication.data.cache.AppDatabase
import com.alorma.myapplication.data.cache.ConfigDataSource
import com.alorma.myapplication.data.cache.MoviesDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule(private val context: Context) {


    @Provides
    fun providesDatabase(): AppDatabase = AppDatabase.getInstance(context)

    @Provides
    @Singleton
    fun provideMoviesDao(appDatabase: AppDatabase): MoviesDao = appDatabase.movieDao()

    @Provides
    @Singleton
    fun provideConfigCache(): ConfigDataSource = ConfigDataSource()
}