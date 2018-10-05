package com.alorma.myapplication.ui.detail

import com.alorma.myapplication.common.observeOnUI
import com.alorma.myapplication.domain.model.Configuration
import com.alorma.myapplication.domain.model.Movie
import com.alorma.myapplication.domain.usecase.ObtainConfigurationUseCase
import com.alorma.myapplication.domain.usecase.ObtainMovieDetailUseCase
import com.alorma.myapplication.domain.usecase.ObtainMovieUseCase
import com.alorma.myapplication.ui.common.BaseViewModel
import com.alorma.myapplication.ui.common.Event
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function3
import javax.inject.Inject

class MovieDetailViewModel @Inject constructor(
        private val detailStates: DetailStates,
        private val detailRoutes: DetailRoutes,
        private val obtainMovieDetailUseCase: ObtainMovieDetailUseCase,
        private val obtainConfigurationUseCase: ObtainConfigurationUseCase,
        private val obtainMovieUseCase: ObtainMovieUseCase) :
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
        val disposable = Single.zip(obtainConfigurationUseCase.execute(),
                obtainMovieDetailUseCase.execute(id),
                obtainMovieUseCase.execute(id),
                Function3<Configuration, Movie, List<Movie>,
                        Triple<Configuration, Movie, List<Movie>>> { c, m, l ->
                    Triple(c, m, l)
                })
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
        val disposable = Single.zip(
                obtainConfigurationUseCase.execute(),
                obtainMovieUseCase.executeNextPage(id),
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
    }
}
