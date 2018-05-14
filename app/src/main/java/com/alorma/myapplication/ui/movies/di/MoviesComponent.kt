package com.alorma.myapplication.ui.movies.di

import com.alorma.myapplication.ui.movies.MoviesActivity
import dagger.Subcomponent

@Subcomponent(modules = [MoviesModule::class])
interface MoviesComponent {
    infix fun inject(activity: MoviesActivity)
}