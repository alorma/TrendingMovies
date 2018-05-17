package com.alorma.myapplication.ui.splash.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.alorma.myapplication.domain.usecase.LoadConfigurationUseCase
import com.alorma.myapplication.ui.splash.SplashNavigator
import com.alorma.myapplication.ui.splash.SplashRoutes
import com.alorma.myapplication.ui.splash.SplashViewModel
import javax.inject.Inject

class SplashViewModelFactory @Inject constructor(
        private val splashRoute: SplashRoutes,
        private val splashNavigator: SplashNavigator,
        private val loadConfigurationUseCase: LoadConfigurationUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            SplashViewModel(splashRoute, splashNavigator, loadConfigurationUseCase) as T
}