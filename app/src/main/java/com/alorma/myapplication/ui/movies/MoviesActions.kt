package com.alorma.myapplication.ui.movies

import com.alorma.myapplication.ui.common.Action
import javax.inject.Inject

class MoviesActions @Inject constructor() {
    sealed class MovieAction : Action() {
        object Load : MovieAction()
        object LoadPage : MovieAction()
        data class OpenDetail(val id: Int, val title: String) : MovieAction()
    }

    fun load(): MovieAction = MovieAction.Load
    fun loadPage(): MovieAction = MovieAction.LoadPage
    fun detail(item: MoviewItemVM): MovieAction = MovieAction.OpenDetail(item.id, item.title)
}