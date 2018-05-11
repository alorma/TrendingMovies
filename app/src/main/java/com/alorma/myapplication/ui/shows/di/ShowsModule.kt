package com.alorma.myapplication.ui.shows.di

import android.app.Activity
import com.alorma.myapplication.ui.shows.ShowsNavigator
import com.alorma.myapplication.ui.shows.ShowsRoutes
import dagger.Module
import dagger.Provides

@Module
class ShowsModule(private val activity: Activity) {

    @Provides
    fun provideNavigator(showsRoutes: ShowsRoutes): ShowsNavigator =
            ShowsNavigator(activity, showsRoutes)
}