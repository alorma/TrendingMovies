package com.alorma.myapplication.ui.search

import com.alorma.myapplication.commons.observeOnUI
import com.alorma.myapplication.domain.model.Configuration
import com.alorma.myapplication.domain.model.Movie
import com.alorma.myapplication.domain.usecase.ObtainConfigurationUseCase
import com.alorma.myapplication.domain.usecase.SearchMoviesUseCase
import com.alorma.myapplication.ui.common.BasePresenter
import com.alorma.rac1.commons.plusAssign
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class SearchPresenter @Inject constructor(
        private val states: SearchStates,
        private val searchRoutes: SearchRoutes,
        private val navigator: SearchNavigator,
        private val obtainMoviesUseCase: SearchMoviesUseCase,
        private val obtainConfigurationUseCase: ObtainConfigurationUseCase) :
        BasePresenter<SearchActions.SearchAction, SearchStates.SearchState>() {

    private lateinit var query: String

    override fun reduce(action: SearchActions.SearchAction) {
        when (action) {
            is SearchActions.SearchAction.NewQuery -> {
                this.query = action.query
                onSearch(action)
            }
            is SearchActions.SearchAction.LoadPage -> onSearch(action)
            SearchActions.SearchAction.CleanSearch -> {
                this.query = ""
            }
            SearchActions.SearchAction.Back -> navigator navigate searchRoutes.back()
            is SearchActions.SearchAction.OpenDetail -> openDetail(action)
        }
    }

    private fun onSearch(action: SearchActions.SearchAction) {
        disposable += Single.zip(obtainConfigurationUseCase.execute(), obtainLoadUseCase(action),
                BiFunction<Configuration, List<Movie>, Pair<Configuration, List<Movie>>> { conf, list ->
                    conf to list
                })
                .observeOnUI()
                .subscribe(
                        { render(states.success(it, action === SearchActions.SearchAction.LoadPage)) },
                        { render(states error it) }
                )
    }

    private fun obtainLoadUseCase(action: SearchActions.SearchAction): Single<List<Movie>> =
            when (action) {
                is SearchActions.SearchAction.NewQuery -> obtainMoviesUseCase.execute(action.query)
                is SearchActions.SearchAction.LoadPage -> obtainMoviesUseCase.executeNextPage(query)
                else -> Single.never()
            }

    private fun openDetail(action: SearchActions.SearchAction.OpenDetail) {
        navigator navigate searchRoutes.detail(action.movie)
    }
}