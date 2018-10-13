package com.alorma.presentation.search

import com.alorma.domain.usecase.ObtainConfigurationUseCase
import com.alorma.domain.usecase.SearchMoviesUseCase
import com.alorma.presentation.common.BaseViewModel
import com.alorma.presentation.common.Event
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.launch

class SearchViewModel(
        private val states: SearchStates,
        private val searchRoutes: SearchRoutes,
        private val obtainMoviesUseCase: SearchMoviesUseCase,
        private val obtainConfigurationUseCase: ObtainConfigurationUseCase) :
        BaseViewModel<SearchStates.SearchState, SearchRoutes.SearchRoute, SearchActions.SearchAction, Event>() {

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
        val job = GlobalScope.launch {
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
        addJob(job)
    }

    private fun searchPage() {
        val job = GlobalScope.launch {
            try {
                render(states loading true)
                val configuration = obtainConfigurationUseCase.execute()
                val movies = obtainMoviesUseCase.executeNextPage(query)
                val success = states.success(configuration, movies)
                render(states loading false)
                render(success)
            } catch (e: Exception) {
                render(states.error(e))
            }
        }
        addJob(job)
    }

    private fun openDetail(action: SearchActions.SearchAction.OpenDetail) {
        navigate(searchRoutes.detail(action.movie))
    }
}