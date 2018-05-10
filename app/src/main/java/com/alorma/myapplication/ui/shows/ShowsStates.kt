package com.alorma.myapplication.ui.shows

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
    infix fun success(items: List<TvShow>): ShowsState = ShowsState.Success(items.map { mapper mapSuccess it })
    infix fun error(it: Throwable): ShowsState.Error = ShowsState.Error(mapper mapError it)
}