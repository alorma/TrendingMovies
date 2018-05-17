package com.alorma.myapplication.ui.movies.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.alorma.myapplication.domain.usecase.ObtainConfigurationUseCase
import com.alorma.myapplication.domain.usecase.ObtainMoviesUseCase
import com.alorma.myapplication.ui.movies.MoviesNavigator
import com.alorma.myapplication.ui.movies.MoviesRoutes
import com.alorma.myapplication.ui.movies.MoviesStates
import com.alorma.myapplication.ui.movies.MoviesViewModel
import javax.inject.Inject

class MovieViewModelFactory @Inject constructor(
        private val states: MoviesStates,
        private val routes: MoviesRoutes,
        private val obtainMoviesUseCase: ObtainMoviesUseCase,
        private val obtainConfigurationUseCase: ObtainConfigurationUseCase,
        private val moviesNavigator: MoviesNavigator) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
            MoviesViewModel(
                    states,
                    routes,
                    obtainMoviesUseCase,
                    obtainConfigurationUseCase,
                    moviesNavigator
            ) as T
}