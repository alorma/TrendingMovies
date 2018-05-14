package com.alorma.myapplication.ui.detail

import com.alorma.myapplication.ui.common.Action
import com.alorma.myapplication.ui.movies.MovieItemVM
import javax.inject.Inject

class DetailActions @Inject constructor() {
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