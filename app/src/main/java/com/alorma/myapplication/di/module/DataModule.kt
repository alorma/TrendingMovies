package com.alorma.myapplication.di.module

import com.alorma.myapplication.data.cache.ConfigDataSource
import com.alorma.myapplication.data.cache.MoviesDataSource
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule {

    @Provides
    @Singleton
    fun provideMoviesCache(): MoviesDataSource = MoviesDataSource()

    @Provides
    @Singleton
    fun provideConfigCache(): ConfigDataSource = ConfigDataSource()
}