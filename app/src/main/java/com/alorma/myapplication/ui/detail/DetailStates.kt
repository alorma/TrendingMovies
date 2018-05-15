package com.alorma.myapplication.ui.detail

import com.alorma.myapplication.domain.model.Configuration
import com.alorma.myapplication.domain.model.Movie
import com.alorma.myapplication.ui.common.State
import com.alorma.myapplication.ui.movies.MovieItemVM
import javax.inject.Inject

class DetailStates @Inject constructor(private val mapper: DetailMapper) {
    sealed class DetailState : State() {
        data class Success(val detail: MovieDetailVM) : DetailState()
        data class SimilarMovies(val movies: List<MovieItemVM>) : DetailState()
        data class Error(val text: String) : DetailState()
        data class ErrorSimilarMovies(val text: String) : DetailState()
    }

    infix fun success(it: Movie): DetailState =
            DetailState.Success(mapper.success(it))

    infix fun successSimilarMovies(items: Pair<Configuration, List<Movie>>): DetailState =
            DetailState.SimilarMovies(items.second.map { mapper.mapSimilar(it, items.first) })

    infix fun error(it: Throwable): DetailState =
            DetailState.Error(mapper mapError it)

    infix fun errorSimilarMovies(it: Throwable): DetailState =
            DetailState.ErrorSimilarMovies(mapper mapError it)
}