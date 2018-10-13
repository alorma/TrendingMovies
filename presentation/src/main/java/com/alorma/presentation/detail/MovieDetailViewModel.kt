package com.alorma.presentation.detail

import com.alorma.domain.model.Configuration
import com.alorma.domain.model.Movie
import com.alorma.domain.usecase.LoadMovieDetailUseCase
import com.alorma.domain.usecase.ObtainConfigurationUseCase
import com.alorma.domain.usecase.ObtainSimilarMoviesUseCase
import com.alorma.presentation.common.BaseViewModel
import com.alorma.presentation.common.Event
import com.alorma.presentation.common.observeOnUI
import io.reactivex.Single
import io.reactivex.functions.BiFunction

class MovieDetailViewModel(
        private val detailStates: DetailStates,
        private val detailRoutes: DetailRoutes,
        private val loadMovieDetailUseCase: LoadMovieDetailUseCase,
        private val obtainConfigurationUseCase: ObtainConfigurationUseCase,
        private val obtainSimilarMoviesUseCase: ObtainSimilarMoviesUseCase) :
        BaseViewModel<DetailStates.DetailState, DetailRoutes.DetailRoute,
                DetailActions.DetailAction, Event>() {

    private var id: Int = -1

    override infix fun reduce(action: DetailActions.DetailAction) {
        when (action) {
            is DetailActions.DetailAction.Load -> {
                this.id = action.id
                load()
            }
            DetailActions.DetailAction.LoadSimilarPage -> loadSimilarMovies(id)
            DetailActions.DetailAction.Back -> navigate(detailRoutes.back())
            is DetailActions.DetailAction.OpenMovie ->
                navigate(detailRoutes.detail(action.id, action.text))
        }
    }

    private fun load() {
        val disposable = loadMovieDetailUseCase.execute(id)
                .observeOnUI()
                .subscribe(
                        {
                            render(detailStates success it)
                        },
                        {
                            render(detailStates error it)
                        }
                )
        addDisposable(disposable)
    }

    private fun loadSimilarMovies(id: Int) {
        /*
        val disposable = Single.zip(
                obtainConfigurationUseCase.execute(),
                obtainSimilarMoviesUseCase.executeNextPage(id),
                BiFunction<Configuration, List<Movie>,
                        Pair<Configuration, List<Movie>>> { conf, movie ->
                    conf to movie
                })
                .observeOnUI()
                .subscribe(
                        { render(detailStates successSimilarMovies it) },
                        { render(detailStates errorSimilarMovies it) }
                )
        addDisposable(disposable)
        */
    }
}
