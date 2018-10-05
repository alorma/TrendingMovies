package com.alorma.myapplication.infrastructure.di

import com.alorma.myapplication.ui.detail.*
import com.alorma.myapplication.ui.movies.*
import com.alorma.myapplication.ui.search.*
import com.alorma.myapplication.ui.splash.SplashActions
import com.alorma.myapplication.ui.splash.SplashRoutes
import com.alorma.myapplication.ui.splash.SplashViewModel
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val uiModule = module {
    viewModel { SplashViewModel(get(), get()) }
    viewModel { MoviesViewModel(get(), get(), get(), get()) }
    viewModel { SearchViewModel(get(), get(), get(), get()) }
    viewModel { MovieDetailViewModel(get(), get(), get(), get(), get()) }

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