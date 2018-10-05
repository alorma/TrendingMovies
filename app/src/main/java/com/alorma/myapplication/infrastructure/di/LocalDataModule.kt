package com.alorma.myapplication.infrastructure.di

import com.alorma.myapplication.data.cache.LocalConfigDataSource
import com.alorma.myapplication.data.cache.LocalMoviesDataSource
import org.koin.dsl.module.module

val localDataModule = module {
    single { LocalMoviesDataSource() }
    single { LocalConfigDataSource() }
}