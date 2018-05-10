package com.alorma.myapplication.ui.shows

import com.alorma.myapplication.ui.common.Action
import com.alorma.myapplication.ui.common.Route
import com.alorma.myapplication.ui.common.State
import javax.inject.Inject

open class ShowsAction @Inject constructor() : Action() {
    object Load : ShowsAction()

    fun load(): ShowsAction = Load
}

open class ShowsState @Inject constructor() : State() {
    data class Loading(val visible: Boolean) : ShowsState()
    data class Success(val items: List<TvShowVM>) : ShowsState()
    data class Error(val text: String): ShowsState()

    infix fun loading(visible: Boolean): ShowsState = Loading(visible)
    infix fun success(items: List<TvShowVM>): ShowsState = Success(items)
    infix fun error(text: String): Error = ShowsState.Error(text)
}

open class ShowsRoute @Inject constructor() : Route() {

}