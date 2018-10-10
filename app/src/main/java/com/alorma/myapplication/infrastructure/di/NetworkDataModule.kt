package com.alorma.myapplication.infrastructure.di

import com.alorma.data.net.MoviesMapper
import com.alorma.data.net.NetworkMoviesDataSource
import com.alorma.data.net.config.ConfigurationMapper
import com.alorma.data.net.config.NetworkConfigDataSource
import org.koin.dsl.module.module

val networkDataModule = module {
    factory { NetworkConfigDataSource(get(), get()) }
    factory { NetworkMoviesDataSource(get(), get()) }

    factory { ConfigurationMapper() }
    factory { MoviesMapper(get()) }
}