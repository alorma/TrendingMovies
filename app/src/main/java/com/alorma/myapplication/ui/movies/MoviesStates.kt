package com.alorma.myapplication.ui.movies

import com.alorma.myapplication.domain.model.Configuration
import com.alorma.myapplication.domain.model.Movie
import com.alorma.myapplication.ui.common.State
import javax.inject.Inject

class MoviesStates @Inject constructor(private val mapper: MoviesMapper) {
    sealed class MovieState : State() {
        data class Loading(val visible: Boolean) : MovieState()
        data class Success(val items: List<MoviewItemVM>) : MovieState()
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