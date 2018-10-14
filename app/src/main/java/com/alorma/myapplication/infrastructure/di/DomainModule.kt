package com.alorma.myapplication.infrastructure.di

import com.alorma.data.repository.ConfigurationRepositoryImpl
import com.alorma.data.repository.MoviesRepositoryImpl
import com.alorma.domain.repository.ConfigurationRepository
import com.alorma.domain.repository.MoviesRepository
import com.alorma.domain.usecase.*
import org.koin.dsl.module.module

val domainModule = module {
    factory { ObtainConfigurationUseCase(get()) }
    factory { ObtainMoviesUseCase(get()) }
    factory { SearchMoviesUseCase(get()) }
    factory { ObtainSimilarMoviesUseCase(get()) }
    factory { ObtainMovieDetailUseCase(get()) }
    factory { LoadMovieDetailUseCase(get(), get(), get()) }

    factory<ConfigurationRepository> { ConfigurationRepositoryImpl(get(), get()) }
    factory<MoviesRepository> { MoviesRepositoryImpl(get(), get()) }
}