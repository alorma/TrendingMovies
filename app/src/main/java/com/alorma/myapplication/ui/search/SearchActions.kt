package com.alorma.myapplication.ui.search

import com.alorma.myapplication.ui.common.Action
import javax.inject.Inject

class SearchActions @Inject constructor() {
    sealed class SearchAction : Action() {
        data class NewQuery(val query: String) : SearchAction()
        object CleanSearch : SearchAction()
    }

    fun query(text: String?): SearchAction = text?.takeIf { it.isNotBlank() }?.let {
        SearchAction.NewQuery(it)
    } ?: SearchAction.CleanSearch
}