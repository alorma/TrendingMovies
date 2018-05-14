package com.alorma.myapplication.ui.movies

import com.alorma.myapplication.commons.observeOnUI
import com.alorma.myapplication.domain.usecase.ObtainMoviesUseCase
import com.alorma.myapplication.ui.common.BasePresenter
import com.alorma.rac1.commons.plusAssign
import javax.inject.Inject

class MoviesPresenter @Inject constructor(private val states: MoviesStates,
                                          private val routes: MoviesRoutes,
                                          private val obtainMoviesUseCase: ObtainMoviesUseCase,
                                          private val moviesNavigator: MoviesNavigator) :
        BasePresenter<MoviesActions.MovieAction, MoviesStates.MovieState>() {

    override fun reduce(action: MoviesActions.MovieAction) {
        when (action) {
            MoviesActions.MovieAction.Load -> load()
            MoviesActions.MovieAction.LoadPage -> load()
            is MoviesActions.MovieAction.OpenDetail -> onOpenDetail(action)
            MoviesActions.MovieAction.Search -> moviesNavigator navigate routes.search()
        }
    }

    private fun load() {
        disposable += obtainMoviesUseCase.execute()
                .observeOnUI()
                .doOnSubscribe { render(states loading true) }
                .doOnNext { render(states loading false) }
                .doOnError { render(states loading false) }
                .subscribe(
                        { render(states success it) },
                        {
                            render(states error it)
                            it.printStackTrace()
                        }
                )
    }

    private fun onOpenDetail(action: MoviesActions.MovieAction.OpenDetail) =
            moviesNavigator.navigate(routes.detail(action.id, action.title))
}