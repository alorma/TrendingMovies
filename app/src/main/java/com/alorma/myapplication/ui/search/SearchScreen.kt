package com.alorma.myapplication.ui.search

import com.alorma.domain.model.Configuration
import com.alorma.domain.model.Movie
import com.alorma.myapplication.ui.common.Action
import com.alorma.myapplication.ui.common.Route
import com.alorma.myapplication.ui.common.State


class SearchStates(private val mapper: SearchMapper) {
    sealed class SearchState : State() {
        object Empty : SearchState()
        object EmptyPage : SearchState()
        data class Loading(val visible: Boolean) : SearchState()
        data class SearchResult(val items: List<MovieSearchItemVM>, val page: Boolean) : SearchState()
        data class Error(val text: String) : SearchState()
    }

    infix fun loading(loading: Boolean): SearchState = SearchState.Loading(loading)

    fun success(it: Pair<Configuration, List<Movie>>, page: Boolean = false): SearchState =
            when {
                it.second.isEmpty() && !page -> SearchState.Empty
                it.second.isEmpty() && page -> SearchState.EmptyPage
                else -> {
                    val configuration = it.first
                    val items = it.second.map { mapper.map(it, configuration) }
                    SearchStates.SearchState.SearchResult(items, page)
                }
            }

    infix fun error(it: Throwable): SearchState = SearchState.Error(mapper mapError it)
}

class SearchActions {
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

class SearchRoutes {
    sealed class SearchRoute : Route() {
        data class OpenDetail(val id: Int, val title: String) : SearchRoute()
        object Back : SearchRoute()
    }

    infix fun detail(movie: MovieSearchItemVM): SearchRoute =
            SearchRoute.OpenDetail(movie.id, movie.title)

    fun back(): SearchRoute = SearchRoute.Back

}