package com.alorma.myapplication.ui.search

import com.alorma.myapplication.ui.common.BasePresenter
import javax.inject.Inject

class SearchPresenter @Inject constructor(
        private val searchStates: SearchStates,
        private val searchRoutes: SearchRoutes,
        private val navigator: SearchNavigator) :

        BasePresenter<SearchActions.SearchAction, SearchStates.SearchState>() {

    override fun reduce(action: SearchActions.SearchAction) {
        when (action) {
            is SearchActions.SearchAction.NewQuery -> onSearch(action)
            is SearchActions.SearchAction.OpenDetail -> openDetail(action)
        }
    }

    private fun onSearch(action: SearchActions.SearchAction.NewQuery) {
        val items = listOf<MovieSearchItemVM>()
        render(searchStates success items)
    }

    private fun openDetail(action: SearchActions.SearchAction.OpenDetail) {
        navigator navigate searchRoutes.detail(action.movie)
    }
}