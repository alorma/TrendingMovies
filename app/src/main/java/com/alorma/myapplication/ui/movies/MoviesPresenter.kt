package com.alorma.myapplication.ui.movies

import com.alorma.myapplication.commons.observeOnUI
import com.alorma.myapplication.domain.model.Configuration
import com.alorma.myapplication.domain.model.Movie
import com.alorma.myapplication.domain.usecase.ObtainConfigurationUseCase
import com.alorma.myapplication.domain.usecase.ObtainMoviesUseCase
import com.alorma.myapplication.ui.common.BasePresenter
import com.alorma.rac1.commons.plusAssign
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class MoviesPresenter @Inject constructor(private val states: MoviesStates,
                                          private val routes: MoviesRoutes,
                                          private val obtainMoviesUseCase: ObtainMoviesUseCase,
                                          private val obtainConfigurationUseCase: ObtainConfigurationUseCase,
                                          private val moviesNavigator: MoviesNavigator) :
        BasePresenter<MoviesActions.MovieAction, MoviesStates.MovieState>() {

    override fun reduce(action: MoviesActions.MovieAction) {
        when (action) {
            MoviesActions.MovieAction.Load -> load(action)
            MoviesActions.MovieAction.LoadPage -> load(action)
            is MoviesActions.MovieAction.OpenDetail -> onOpenDetail(action)
        }
    }

    private fun load(action: MoviesActions.MovieAction) {
        disposable += Single.zip(obtainConfigurationUseCase.execute(), obtainLoadUseCase(action),
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
    }

    private fun obtainLoadUseCase(action: MoviesActions.MovieAction): Single<List<Movie>> =
            when (action) {
                is MoviesActions.MovieAction.Load -> obtainMoviesUseCase.execute()
                is MoviesActions.MovieAction.LoadPage -> obtainMoviesUseCase.executeNextPage()
                else -> Single.never()
            }

    private fun onOpenDetail(action: MoviesActions.MovieAction.OpenDetail) =
            moviesNavigator.navigate(routes.detail(action.id, action.title))
}