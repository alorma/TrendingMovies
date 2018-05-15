package com.alorma.myapplication.ui.movies

import android.arch.paging.PagedList
import com.alorma.myapplication.domain.model.Movie
import com.alorma.myapplication.ui.common.State
import javax.inject.Inject

class MoviesStates @Inject constructor(private val mapper: MoviesMapper) {
    sealed class MovieState : State() {
        data class Loading(val visible: Boolean) : MovieState()
        data class Success(val items: PagedList<Movie>) : MovieState()
        data class Error(val text: String) : MovieState()
    }

    infix fun loading(visible: Boolean): MovieState = MovieState.Loading(visible)

    infix fun success(it: PagedList<Movie>): MovieState = MovieState.Success(it)

    infix fun error(it: Throwable): MovieState.Error = MovieState.Error(mapper mapError it)
}