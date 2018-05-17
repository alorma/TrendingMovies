package com.alorma.myapplication.ui.movies.di

import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.FragmentActivity
import com.alorma.myapplication.ui.movies.MoviesNavigator
import com.alorma.myapplication.ui.movies.MoviesViewModel
import dagger.Module
import dagger.Provides

@Module
class MoviesModule(private val activity: FragmentActivity) {

    @Provides
    fun provideNavigator(): MoviesNavigator = MoviesNavigator(activity)

    @Provides
    fun providesViewModel(factory: MovieViewModelFactory): MoviesViewModel =
            ViewModelProviders.of(activity, factory).get(MoviesViewModel::class.java)
}