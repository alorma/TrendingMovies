package com.alorma.myapplication.infrastructure.di

import com.alorma.presentation.common.ViewModelDispatchers
import com.alorma.presentation.splash.SplashActions
import com.alorma.presentation.splash.SplashRoutes
import com.alorma.presentation.splash.SplashViewModel
import com.alorma.presentation.detail.*
import com.alorma.presentation.movies.*
import com.alorma.presentation.search.*
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val uiModule = module {
    viewModel { SplashViewModel(get(), get(), get()) }
    viewModel { MoviesViewModel(get(), get(), get(), get(), get()) }
    viewModel { SearchViewModel(get(), get(), get(), get(), get()) }
    viewModel { MovieDetailViewModel(get(), get(), get(), get(), get(), get()) }

    factory { ViewModelDispatchers() }

    single { SplashRoutes() }
    single { MoviesRoutes() }
    single { SearchRoutes() }
    single { DetailRoutes() }

    single { MoviesStates(get()) }
    single { SearchStates(get()) }
    single { DetailStates(get()) }

    single { MoviesActions() }
    single { SearchActions() }
    single { DetailActions() }
    single { SplashActions() }

    factory { MoviesMapper(get()) }
    factory { SearchMapper(get(), get()) }
    factory { DetailMapper(get(), get()) }
}