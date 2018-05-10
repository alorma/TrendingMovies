package com.alorma.myapplication.ui.shows

import com.alorma.myapplication.ui.common.Action
import com.alorma.myapplication.ui.common.Route
import com.alorma.myapplication.ui.common.State
import javax.inject.Inject

open class ShowsAction @Inject constructor() : Action() {
    object Load : ShowsAction()
    object LoadPage : ShowsAction()
    data class OpenDetail(val id: Int): ShowsAction()

    fun load(): ShowsAction = Load
    fun loadPage(): ShowsAction = LoadPage
    fun detail(item: TvShowVM): ShowsAction = OpenDetail(item.id)
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
    data class DetailRoute(val id: Int): ShowsRoute()

    fun detail(id: Int): ShowsRoute = DetailRoute(id)
}