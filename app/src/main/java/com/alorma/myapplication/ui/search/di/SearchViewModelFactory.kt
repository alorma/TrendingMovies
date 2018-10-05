package com.alorma.myapplication.ui.search.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alorma.myapplication.domain.usecase.ObtainConfigurationUseCase
import com.alorma.myapplication.domain.usecase.SearchMoviesUseCase
import com.alorma.myapplication.ui.search.SearchNavigator
import com.alorma.myapplication.ui.search.SearchRoutes
import com.alorma.myapplication.ui.search.SearchStates
import com.alorma.myapplication.ui.search.SearchViewModel
import javax.inject.Inject

class SearchViewModelFactory @Inject constructor(
        private val states: SearchStates,
        private val searchRoutes: SearchRoutes,
        private val navigator: SearchNavigator,
        private val obtainMoviesUseCase: SearchMoviesUseCase,
        private val obtainConfigurationUseCase: ObtainConfigurationUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            SearchViewModel(states,
                    searchRoutes,
                    navigator,
                    obtainMoviesUseCase,
                    obtainConfigurationUseCase
            ) as T

}