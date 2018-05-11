package com.alorma.myapplication.di.module

import com.alorma.myapplication.data.cache.ConfigDataSource
import com.alorma.myapplication.data.cache.ShowsDataSource
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule {

    @Provides
    @Singleton
    fun provideShowsCache(): ShowsDataSource = ShowsDataSource()

    @Provides
    @Singleton
    fun provideConfigCache(): ConfigDataSource = ConfigDataSource()
}