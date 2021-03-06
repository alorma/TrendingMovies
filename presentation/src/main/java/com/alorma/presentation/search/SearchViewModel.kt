package com.alorma.presentation.search

import com.alorma.domain.usecase.ObtainConfigurationUseCase
import com.alorma.domain.usecase.SearchMoviesUseCase
import com.alorma.presentation.common.BaseViewModel
import com.alorma.presentation.common.Event
import com.alorma.presentation.common.ViewModelDispatchers

class SearchViewModel(
        private val states: SearchStates,
        private val searchRoutes: SearchRoutes,
        private val obtainMoviesUseCase: SearchMoviesUseCase,
        private val obtainConfigurationUseCase: ObtainConfigurationUseCase,
        dispatchers: ViewModelDispatchers) :
        BaseViewModel<SearchStates.SearchState,
                SearchRoutes.SearchRoute,
                SearchActions.SearchAction,
                Event>(dispatchers) {

    private lateinit var query: String

    override infix fun reduce(action: SearchActions.SearchAction) {
        when (action) {
            is SearchActions.SearchAction.NewQuery -> {
                this.query = action.query
                search()
            }
            is SearchActions.SearchAction.LoadPage -> searchPage()
            SearchActions.SearchAction.CleanSearch -> {
                this.query = ""
            }
            SearchActions.SearchAction.Back -> navigate(searchRoutes.back())
            is SearchActions.SearchAction.OpenDetail -> openDetail(action)
            SearchActions.SearchAction.Retry -> search()
        }
    }

    private fun search() {
        clear()
        val error = object : ErrorHandler {
            override fun onError(exception: Throwable) {
                render(states.error(exception))
            }
        }
        launch(error) {
            try {
                render(states loading true)
                val configuration = obtainConfigurationUseCase.execute()
                val movies = obtainMoviesUseCase.execute(query)
                val success = states.success(configuration, movies)
                render(states loading false)
                render(success)
            } catch (e: Exception) {
                render(states error e)
            }
        }
    }

    private fun searchPage() {
        val error = object : ErrorHandler {
            override fun onError(exception: Throwable) {
                render(states.error(exception))
            }
        }
        launch(error) {
            try {
                render(states loading true)
                val configuration = obtainConfigurationUseCase.execute()
                val movies = obtainMoviesUseCase.executeNextPage(query)
                val success = states.success(configuration, movies, true)
                render(states loading false)
                render(success)
            } catch (e: Exception) {
                render(states.error(e))
            }
        }
    }

    private fun openDetail(action: SearchActions.SearchAction.OpenDetail) {
        navigate(searchRoutes.detail(action.movie))
    }
}