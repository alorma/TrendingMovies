package com.alorma.myapplication.ui.shows.di

import android.app.Activity
import com.alorma.myapplication.ui.shows.ShowsNavigator
import dagger.Module
import dagger.Provides

@Module
class ShowsModule(private val activity: Activity) {

    @Provides
    fun provideNavigator(): ShowsNavigator = ShowsNavigator(activity)

}