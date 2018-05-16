package com.alorma.myapplication.di.module

import android.arch.lifecycle.MutableLiveData
import com.alorma.myapplication.data.cache.ConfigDataSource
import com.alorma.myapplication.data.cache.MoviesDataSource
import com.alorma.myapplication.ui.detail.DetailStates
import com.alorma.myapplication.ui.movies.MoviesStates
import com.alorma.myapplication.ui.search.SearchStates
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule {

    @Provides
    @Singleton
    fun providesMoviesLiveData(): MutableLiveData<MoviesStates.MovieState> = MutableLiveData()

    @Provides
    @Singleton
    fun providesMovieDetailLiveData(): MutableLiveData<DetailStates.DetailState> = MutableLiveData()

    @Provides
    @Singleton
    fun providesSearchLiveData(): MutableLiveData<SearchStates.SearchState> = MutableLiveData()

    @Provides
    @Singleton
    fun provideMoviesCache(): MoviesDataSource = MoviesDataSource()

    @Provides
    @Singleton
    fun provideConfigCache(): ConfigDataSource = ConfigDataSource()
}