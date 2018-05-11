package com.alorma.myapplication.ui.shows

import com.alorma.myapplication.domain.model.Configuration
import com.alorma.myapplication.domain.model.TvShow
import com.alorma.myapplication.ui.common.State
import javax.inject.Inject

class ShowsStates @Inject constructor(private val mapper: ShowsMapper) {
    sealed class ShowsState : State() {
        data class Loading(val visible: Boolean) : ShowsState()
        data class Success(val items: List<TvShowVM>) : ShowsState()
        data class Error(val text: String) : ShowsState()
    }

    infix fun loading(visible: Boolean): ShowsState = ShowsState.Loading(visible)

    infix fun success(it: Pair<Configuration, List<TvShow>>): ShowsState {
        val configuration = it.first
        val items = it.second.map { mapper.mapSuccess(it, configuration) }
        return ShowsState.Success(items)
    }

    infix fun error(it: Throwable): ShowsState.Error = ShowsState.Error(mapper mapError it)
}