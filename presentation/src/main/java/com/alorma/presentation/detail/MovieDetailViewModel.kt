package com.alorma.presentation.detail

import com.alorma.domain.usecase.LoadMovieDetailUseCase
import com.alorma.domain.usecase.ObtainConfigurationUseCase
import com.alorma.domain.usecase.ObtainSimilarMoviesUseCase
import com.alorma.presentation.common.BaseViewModel
import com.alorma.presentation.common.Event

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
        launch {
            try {
                val movieDetail = loadMovieDetailUseCase.execute(id)
                render(detailStates success movieDetail)
            } catch (e: Exception) {
                render(detailStates error e)
            }
        }
    }

    private fun loadSimilarMovies(id: Int) {
        launch {
            try {
                val configuration = obtainConfigurationUseCase.execute()
                val similar = obtainSimilarMoviesUseCase.execute(id)
                render(detailStates.successSimilarMovies(configuration, similar))
            } catch (e: Exception) {
                render(detailStates errorSimilarMovies e)
            }

        }
    }
}
