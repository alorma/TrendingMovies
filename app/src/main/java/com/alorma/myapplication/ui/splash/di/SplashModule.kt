package com.alorma.myapplication.ui.splash.di

import android.app.Activity
import com.alorma.myapplication.ui.splash.SplashNavigator
import dagger.Module
import dagger.Provides

@Module
class SplashModule(private val activity: Activity) {

    @Provides
    fun provideNavigator(): SplashNavigator = SplashNavigator(activity)
}