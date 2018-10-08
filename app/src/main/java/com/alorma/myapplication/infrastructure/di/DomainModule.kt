package com.alorma.myapplication.infrastructure.di

import com.alorma.myapplication.data.repository.ConfigurationRepository
import com.alorma.myapplication.data.repository.MoviesRepository
import com.alorma.myapplication.domain.usecase.*
import org.koin.dsl.module.module

val domainModule = module {
    factory { LoadConfigurationUseCase(get()) }
    factory { ObtainConfigurationUseCase(get()) }
    factory { ObtainMoviesUseCase(get()) }
    factory { SearchMoviesUseCase(get()) }
    factory { ObtainMovieUseCase(get()) }
    factory { ObtainMovieDetailUseCase(get()) }

    factory { ConfigurationRepository(get(), get()) }
    factory { MoviesRepository(get(), get()) }
}