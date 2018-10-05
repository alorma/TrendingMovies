package com.alorma.myapplication.ui.detail

import com.alorma.myapplication.domain.model.Configuration
import com.alorma.myapplication.domain.model.Movie
import com.alorma.myapplication.ui.common.Action
import com.alorma.myapplication.ui.common.Route
import com.alorma.myapplication.ui.common.State
import com.alorma.myapplication.ui.movies.MovieItemVM


class DetailStates(private val mapper: DetailMapper) {
    sealed class DetailState : State() {

        data class Success(val detail: MovieDetailVM,
                           val similarMovies: List<MovieItemVM>) : DetailState()

        data class SimilarMovies(val movies: List<MovieItemVM>) : DetailState()
        data class Error(val text: String) : DetailState()
        data class ErrorSimilarMovies(val text: String) : DetailState()
    }

    infix fun success(it: Triple<Configuration, Movie, List<Movie>>): DetailState =
            DetailState.Success(mapper.success(it.second, it.first),
                    mapper.mapSimilar(it.third, it.first))

    infix fun successSimilarMovies(items: Pair<Configuration, List<Movie>>): DetailState =
            DetailState.SimilarMovies(items.second.map { mapper.mapSimilar(it, items.first) })

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