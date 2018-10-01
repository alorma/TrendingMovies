package com.alorma.myapplication.ui.search

import com.alorma.myapplication.ui.common.Action
import javax.inject.Inject

class SearchActions @Inject constructor() {
    sealed class SearchAction : Action() {
        data class NewQuery(val query: String) : SearchAction()
        object Retry : SearchAction()
        data class OpenDetail(val movie: MovieSearchItemVM) : SearchAction()
        object LoadPage : SearchAction()
        object Back : SearchAction()
        object CleanSearch : SearchAction()
    }

    fun back(): SearchAction = SearchAction.Back

    fun query(text: String?): SearchAction = text?.takeIf { it.isNotBlank() }?.let {
        SearchAction.NewQuery(it)
    } ?: SearchAction.CleanSearch

    fun retry(): SearchAction = SearchAction.Retry

    fun page(): SearchAction = SearchAction.LoadPage

    fun detail(it: MovieSearchItemVM): SearchAction = SearchAction.OpenDetail(it)
}