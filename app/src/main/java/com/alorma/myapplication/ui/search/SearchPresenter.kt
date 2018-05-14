package com.alorma.myapplication.ui.search

import com.alorma.myapplication.ui.common.BasePresenter
import javax.inject.Inject

class SearchPresenter @Inject constructor(private val searchStates: SearchStates) :
        BasePresenter<SearchActions.SearchAction, SearchStates.SearchState>() {

    override fun reduce(action: SearchActions.SearchAction) {
        when (action) {
            is SearchActions.SearchAction.NewQuery -> onSearch(action)
        }
    }

    private fun onSearch(action: SearchActions.SearchAction.NewQuery) {
        val items = listOf<MovieSearchItemVM>()
        render(searchStates success items)
    }
}