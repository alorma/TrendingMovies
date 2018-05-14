package com.alorma.myapplication.ui.search

import com.alorma.myapplication.ui.common.State
import javax.inject.Inject

class SearchStates @Inject constructor() {
    sealed class SearchState : State() {
        data class SearchResult(val itemVMS: List<MovieSearchItemVM>) : SearchState()
    }

    infix fun success(items: List<MovieSearchItemVM>) = SearchState.SearchResult(items)
}