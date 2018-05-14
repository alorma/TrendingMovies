package com.alorma.myapplication.ui.movies

import com.alorma.myapplication.domain.model.Movie
import com.alorma.myapplication.ui.common.Action
import javax.inject.Inject

class MoviesActions @Inject constructor() {
    sealed class MovieAction : Action() {
        object Load : MovieAction()
        object LoadPage : MovieAction()
        object Search : MovieAction()
        data class OpenDetail(val id: Int, val title: String) : MovieAction()
    }

    fun load(): MovieAction = MovieAction.Load
    fun loadPage(): MovieAction = MovieAction.LoadPage
    fun detail(item: Movie): MovieAction = MovieAction.OpenDetail(item.id, item.title)
    fun search(): MovieAction = MovieAction.Search
}