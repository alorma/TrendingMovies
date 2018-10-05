package com.alorma.myapplication.infrastructure.di

import com.alorma.myapplication.domain.repository.ConfigurationRepository
import com.alorma.myapplication.domain.repository.MoviesRepository
import com.alorma.myapplication.domain.usecase.*
import com.alorma.myapplication.ui.detail.MovieDetailViewModel
import com.alorma.myapplication.ui.movies.MoviesViewModel
import com.alorma.myapplication.ui.search.SearchViewModel
import com.alorma.myapplication.ui.splash.SplashViewModel
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val domainModule = module {
    viewModel { SplashViewModel(get(), get()) }
    viewModel { MoviesViewModel(get(), get(), get(), get()) }
    viewModel { SearchViewModel(get(), get(), get(), get()) }
    viewModel { MovieDetailViewModel(get(), get(), get(), get(), get()) }

    factory { LoadConfigurationUseCase(get()) }
    factory { ObtainConfigurationUseCase(get()) }
    factory { ObtainMoviesUseCase(get()) }
    factory { SearchMoviesUseCase(get()) }
    factory { ObtainMovieUseCase(get()) }
    factory { ObtainMovieDetailUseCase(get()) }

    factory { ConfigurationRepository(get(), get()) }
    factory { MoviesRepository(get(), get()) }
}