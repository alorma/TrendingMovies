package com.alorma.myapplication.ui.search

import com.alorma.myapplication.domain.model.Configuration
import com.alorma.myapplication.domain.model.Movie
import com.alorma.myapplication.ui.common.State
import javax.inject.Inject

class SearchStates @Inject constructor(private val mapper: SearchMapper) {
    sealed class SearchState : State() {
        object Empty : SearchState()
        object EmptyPage : SearchState()
        data class SearchResult(val items: List<MovieSearchItemVM>, val page: Boolean) : SearchState()
        data class Error(val text: String) : SearchState()
    }

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

    infix fun error(it: Throwable): SearchState = SearchState.Error(mapper.mapError(it))
}