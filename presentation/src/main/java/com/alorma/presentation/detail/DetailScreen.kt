package com.alorma.presentation.detail

import com.alorma.domain.model.Configuration
import com.alorma.domain.model.Movie
import com.alorma.domain.model.MovieDetail
import com.alorma.presentation.common.Action
import com.alorma.presentation.common.Route
import com.alorma.presentation.common.State
import com.alorma.presentation.movies.MovieItemVM


class DetailStates(private val mapper: DetailMapper) {
    sealed class DetailState : State() {

        data class Success(val detail: MovieDetailVM,
                           val similarMovies: List<MovieItemVM>) : DetailState()

        data class SimilarMovies(val movies: List<MovieItemVM>) : DetailState()
        data class Error(val text: String) : DetailState()
        data class ErrorSimilarMovies(val text: String) : DetailState()
    }

    infix fun success(it: MovieDetail): DetailState =
            DetailState.Success(mapper.success(it.movie, it.config),
                    mapper.mapSimilar(it.similar, it.config))

    fun successSimilarMovies(config: Configuration, movies: List<Movie>): DetailState =
            DetailState.SimilarMovies(movies.map { mapper.mapSimilar(it, config) })

    infix fun error(it: Throwable): DetailState =
            DetailState.Error(mapper mapError it)

    infix fun errorSimilarMovies(it: Throwable): DetailState =
            DetailState.ErrorSimilarMovies(mapper mapError it)
}

class DetailActions {
    sealed class DetailAction : Action() {
        data class Load(val id: Int) : DetailAction()
        data class OpenMovie(val id: Int, val text: String) : DetailAction()
        object LoadSimilarPage : DetailAction()
        object Back : DetailAction()
    }

    fun load(id: Int): DetailAction = DetailAction.Load(id)
    fun loadSimilarPage(): DetailAction = DetailAction.LoadSimilarPage
    fun back(): DetailAction = DetailAction.Back
    fun openSimilarMovie(it: MovieItemVM): DetailAction = DetailAction.OpenMovie(it.id, it.title)
}

class DetailRoutes {
    sealed class DetailRoute : Route() {
        data class Detail(val id: Int, val title: String) : DetailRoute()
        object Back : DetailRoute()
    }

    fun back(): DetailRoute = DetailRoute.Back
    fun detail(id: Int, title: String): DetailRoute = DetailRoute.Detail(id, title)
}