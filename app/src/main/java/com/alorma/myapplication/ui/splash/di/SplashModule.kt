package com.alorma.myapplication.ui.splash.di

import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.FragmentActivity
import com.alorma.myapplication.ui.splash.SplashNavigator
import com.alorma.myapplication.ui.splash.SplashViewModel
import dagger.Module
import dagger.Provides

@Module
class SplashModule(private val activity: FragmentActivity) {

    @Provides
    fun provideNavigator(): SplashNavigator = SplashNavigator(activity)

    @Provides
    fun provideSplashViewModel(factory: SplashViewModelFactory): SplashViewModel =
            ViewModelProviders.of(activity, factory).get(SplashViewModel::class.java)
}