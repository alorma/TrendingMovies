package com.alorma.myapplication.ui.splash.di

import androidx.lifecycle.ViewModelProviders
import androidx.fragment.app.FragmentActivity
import com.alorma.myapplication.ui.splash.SplashNavigator
import com.alorma.myapplication.ui.splash.SplashViewModel
import dagger.Module
import dagger.Provides

@Module
class SplashModule(private val activity: androidx.fragment.app.FragmentActivity) {

    @Provides
    fun provideNavigator(): SplashNavigator = SplashNavigator(activity)

    @Provides
    fun provideSplashViewModel(factory: SplashViewModelFactory): SplashViewModel =
            ViewModelProviders.of(activity, factory).get(SplashViewModel::class.java)
}