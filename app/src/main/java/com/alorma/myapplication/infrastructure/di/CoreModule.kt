package com.alorma.myapplication.infrastructure.di

import com.alorma.myapplication.ui.common.ResourcesProvider
import com.alorma.myapplication.ui.detail.DetailActions
import com.alorma.myapplication.ui.detail.DetailRoutes
import com.alorma.myapplication.ui.detail.DetailStates
import com.alorma.myapplication.ui.movies.MoviesActions
import com.alorma.myapplication.ui.movies.MoviesMapper
import com.alorma.myapplication.ui.movies.MoviesRoutes
import com.alorma.myapplication.ui.movies.MoviesStates
import com.alorma.myapplication.ui.search.SearchActions
import com.alorma.myapplication.ui.search.SearchRoutes
import com.alorma.myapplication.ui.search.SearchStates
import com.alorma.myapplication.ui.splash.SplashRoutes
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module

val coreModule = module {
    factory { SplashRoutes() }
    factory { MoviesRoutes() }
    factory { SearchRoutes() }
    factory { DetailRoutes() }

    factory { MoviesStates(get()) }
    factory { SearchStates(get()) }
    factory { DetailStates(get()) }

    factory { MoviesActions() }
    factory { SearchActions() }
    factory { DetailActions() }

    factory { MoviesMapper(get()) }

    factory { ResourcesProvider(androidContext()) }
}