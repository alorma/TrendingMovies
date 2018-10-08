package com.alorma.myapplication.infrastructure.di

import com.alorma.data.cache.LocalConfigDataSource
import com.alorma.data.cache.LocalMoviesDataSource
import org.koin.dsl.module.module

val localDataModule = module {
    single { LocalMoviesDataSource() }
    single { LocalConfigDataSource() }
}