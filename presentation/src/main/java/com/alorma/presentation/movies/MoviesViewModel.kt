package com.alorma.presentation.movies

import com.alorma.domain.model.Configuration
import com.alorma.domain.model.Movie
import com.alorma.domain.usecase.ObtainConfigurationUseCase
import com.alorma.domain.usecase.ObtainMoviesUseCase
import com.alorma.presentation.common.BaseViewModel
import com.alorma.presentation.common.Event
import com.alorma.presentation.common.observeOnUI
import io.reactivex.Single
import io.reactivex.functions.BiFunction

class MoviesViewModel(private val states: MoviesStates,
                      private val routes: MoviesRoutes,
                      private val obtainMoviesUseCase: ObtainMoviesUseCase,
                      private val obtainConfigurationUseCase: ObtainConfigurationUseCase) :
        BaseViewModel<MoviesStates.MovieState, MoviesRoutes.MovieRoute, MoviesActions.MovieAction, Event>() {

    override fun reduce(action: MoviesActions.MovieAction) {
        when (action) {
            MoviesActions.MovieAction.Load -> load(action)
            MoviesActions.MovieAction.LoadPage -> load(action)
            is MoviesActions.MovieAction.OpenDetail -> onOpenDetail(action)
            MoviesActions.MovieAction.Search -> navigate(routes.search())
        }
    }

    private fun load(action: MoviesActions.MovieAction) {
        /*
        val disposable = Single.zip(obtainConfigurationUseCase.execute(), obtainLoadUseCase(action),
                BiFunction<Configuration, List<Movie>, Pair<Configuration, List<Movie>>> { conf, list ->
                    conf to list
                })
                .observeOnUI()
                .doOnSubscribe { render(states loading true) }
                .doOnSuccess { render(states loading false) }
                .doOnError { render(states loading false) }
                .subscribe(
                        { render(states success it) },
                        { render(states error it) }
                )
        addDisposable(disposable)
        */
    }

    private fun obtainLoadUseCase(action: MoviesActions.MovieAction): Single<List<Movie>> =
            when (action) {
                is MoviesActions.MovieAction.Load -> obtainMoviesUseCase.execute()
                is MoviesActions.MovieAction.LoadPage -> obtainMoviesUseCase.executeNextPage()
                else -> Single.never()
            }

    private fun onOpenDetail(action: MoviesActions.MovieAction.OpenDetail) =
            navigate(routes.detail(action.id, action.title))
}