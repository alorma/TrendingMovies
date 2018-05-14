package com.alorma.myapplication.ui.movies.di

import android.app.Activity
import com.alorma.myapplication.ui.movies.MoviesNavigator
import dagger.Module
import dagger.Provides

@Module
class MoviesModule(private val activity: Activity) {

    @Provides
    fun provideNavigator(): MoviesNavigator = MoviesNavigator(activity)
}