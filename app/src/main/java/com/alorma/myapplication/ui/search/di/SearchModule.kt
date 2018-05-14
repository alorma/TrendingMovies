package com.alorma.myapplication.ui.search.di

import android.app.Activity
import com.alorma.myapplication.ui.search.SearchNavigator
import dagger.Module
import dagger.Provides

@Module
class SearchModule(val activity: Activity) {

    @Provides
    fun provideSearchNavigator(): SearchNavigator = SearchNavigator(activity)
}