package com.alorma.myapplication.ui.detail.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.alorma.myapplication.domain.usecase.ObtainConfigurationUseCase
import com.alorma.myapplication.domain.usecase.ObtainMovieDetailUseCase
import com.alorma.myapplication.domain.usecase.ObtainMovieUseCase
import com.alorma.myapplication.ui.detail.DetailNavigator
import com.alorma.myapplication.ui.detail.DetailRoutes
import com.alorma.myapplication.ui.detail.DetailStates
import com.alorma.myapplication.ui.detail.MovieDetailViewModel
import javax.inject.Inject

class DetailViewModelFactory @Inject constructor(
        private val detailStates: DetailStates,
        private val detailRoutes: DetailRoutes,
        private val detailNavigator: DetailNavigator,
        private val obtainMovieDetailUseCase: ObtainMovieDetailUseCase,
        private val obtainConfigurationUseCase: ObtainConfigurationUseCase,
        private val obtainMovieUseCase: ObtainMovieUseCase) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
            MovieDetailViewModel(
                    detailStates,
                    detailRoutes,
                    detailNavigator,
                    obtainMovieDetailUseCase,
                    obtainConfigurationUseCase,
                    obtainMovieUseCase
            ) as T
}