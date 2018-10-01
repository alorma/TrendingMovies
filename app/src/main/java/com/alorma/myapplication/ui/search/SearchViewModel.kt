package com.alorma.myapplication.ui.search

import com.alorma.myapplication.commons.observeOnUI
import com.alorma.myapplication.domain.model.Configuration
import com.alorma.myapplication.domain.model.Movie
import com.alorma.myapplication.domain.usecase.ObtainConfigurationUseCase
import com.alorma.myapplication.domain.usecase.SearchMoviesUseCase
import com.alorma.myapplication.ui.common.BaseViewModel
import com.alorma.myapplication.ui.common.Event
import com.alorma.myapplication.ui.common.Navigator
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class SearchViewModel @Inject constructor(
        private val states: SearchStates,
        private val searchRoutes: SearchRoutes,
        navigator: Navigator<SearchRoutes.SearchRoute>,
        private val obtainMoviesUseCase: SearchMoviesUseCase,
        private val obtainConfigurationUseCase: ObtainConfigurationUseCase) :
        BaseViewModel<SearchStates.SearchState, SearchRoutes.SearchRoute, SearchActions.SearchAction, Event>(navigator) {

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
        val disposable = Single.zip(
                obtainConfigurationUseCase.execute(),
                obtainMoviesUseCase.execute(query),
                BiFunction<Configuration, List<Movie>, Pair<Configuration, List<Movie>>> { conf, list ->
                    conf to list
                })
                .doOnSubscribe { render(states loading true) }
                .doOnSuccess { render(states loading false) }
                .observeOnUI()
                .subscribe(
                        { render(states.success(it)) },
                        { render(states error it) }
                )
        addDisposable(disposable)
    }

    private fun searchPage() {
        val disposable = Single.zip(
                obtainConfigurationUseCase.execute(),
                obtainMoviesUseCase.executeNextPage(query),
                BiFunction<Configuration, List<Movie>, Pair<Configuration, List<Movie>>> { conf, list ->
                    conf to list
                })
                .observeOnUI()
                .subscribe(
                        { render(states.success(it, true)) },
                        { render(states error it) }
                )
        addDisposable(disposable)
    }

    private fun openDetail(action: SearchActions.SearchAction.OpenDetail) {
        navigate(searchRoutes.detail(action.movie))
    }
}