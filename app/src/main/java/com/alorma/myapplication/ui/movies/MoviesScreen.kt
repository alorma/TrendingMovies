package com.alorma.myapplication.ui.movies

import com.alorma.myapplication.domain.model.Configuration
import com.alorma.myapplication.domain.model.Movie
import com.alorma.myapplication.ui.common.Action
import com.alorma.myapplication.ui.common.Event
import com.alorma.myapplication.ui.common.Route
import com.alorma.myapplication.ui.common.State
import javax.inject.Inject


class MoviesStates @Inject constructor(private val mapper: MoviesMapper) {
    sealed class MovieState : State() {
        data class Loading(val visible: Boolean) : MovieState()
        data class Success(val items: List<MovieItemVM>) : MovieState()
        data class Error(val text: String) : MovieState()
    }

    infix fun loading(visible: Boolean): MovieState = MovieState.Loading(visible)

    infix fun success(it: Pair<Configuration, List<Movie>>): MovieState {
        val configuration = it.first
        val items = it.second.map { mapper.mapSuccess(it, configuration) }
        return MovieState.Success(items)
    }

    infix fun error(it: Throwable): MovieState.Error = MovieState.Error(mapper mapError it)
}


class MoviesActions @Inject constructor() {
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

class MoviesRoutes @Inject constructor() {
    sealed class MovieRoute : Route() {
        data class DetailRoute(val id: Int, val title: String) : MovieRoute()
        object Search : MovieRoute()
    }

    fun detail(id: Int, title: String): MovieRoute = MovieRoute.DetailRoute(id, title)
    fun search(): MovieRoute = MovieRoute.Search
}

class MoviesEvent @Inject constructor() {
    sealed class MovieEvent : Event()
}