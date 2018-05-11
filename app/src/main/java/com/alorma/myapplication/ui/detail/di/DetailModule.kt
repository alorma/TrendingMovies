package com.alorma.myapplication.ui.detail.di

import android.app.Activity
import com.alorma.myapplication.ui.detail.DetailNavigator
import dagger.Module
import dagger.Provides

@Module
class DetailModule(private val activity: Activity) {

    @Provides
    fun provideNavigator(): DetailNavigator = DetailNavigator(activity)

}