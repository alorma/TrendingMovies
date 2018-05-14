package com.alorma.myapplication.ui.movies.di

import android.app.Activity
import com.alorma.myapplication.ui.movies.MoviesNavigator
import com.alorma.myapplication.ui.movies.MoviesRoutes
import dagger.Module
import dagger.Provides

@Module
class MoviesModule(private val activity: Activity) {

    @Provides
    fun provideNavigator(moviesRoutes: MoviesRoutes): MoviesNavigator =
            MoviesNavigator(activity)
}