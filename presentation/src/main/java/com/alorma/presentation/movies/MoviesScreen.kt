package com.alorma.presentation.movies

import com.alorma.domain.model.Configuration
import com.alorma.domain.model.Movie
import com.alorma.presentation.common.Action
import com.alorma.presentation.common.Route
import com.alorma.presentation.common.State


class MoviesStates(private val mapper: MoviesMapper) {
    sealed class MovieState : State() {
        data class Loading(val visible: Boolean) : MovieState()
        data class Success(val items: List<MovieItemVM>) : MovieState()
        data class Error(val text: String) : MovieState()
    }

    infix fun loading(visible: Boolean): MovieState = MovieState.Loading(visible)

    fun success(config: Configuration, movies: List<Movie>): MovieState {
        val items = movies.map { mapper.mapSuccess(it, config) }
        return MovieState.Success(items)
    }

    infix fun error(it: Throwable): MovieState.Error = MovieState.Error(mapper mapError it)
}


class MoviesActions {
    sealed class MovieAction : Action() {
        object Load : MovieAction()
        object LoadPage : MovieAction()
        object Search : MovieAction()
        data class OpenDetail(val id: Int, val title: String) : MovieAction()
    }

    fun load(): MovieAction = MovieAction.Load
    fun loadPage(): MovieAction = MovieAction.LoadPage
    fun detail(item: MovieItemVM): MovieAction = MovieAction.OpenDetail(item.id, item.title)
    fun search(): MovieAction = MovieAction.Search
}

class MoviesRoutes {
    sealed class MovieRoute : Route() {
        data class DetailRoute(val id: Int, val title: String) : MovieRoute()
        object Search : MovieRoute()
    }

    fun detail(id: Int, title: String): MovieRoute = MovieRoute.DetailRoute(id, title)
    fun search(): MovieRoute = MovieRoute.Search
}