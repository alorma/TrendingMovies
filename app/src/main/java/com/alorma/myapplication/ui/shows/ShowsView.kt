package com.alorma.myapplication.ui.shows

import com.alorma.myapplication.ui.common.Action
import com.alorma.myapplication.ui.common.Route
import com.alorma.myapplication.ui.common.State
import javax.inject.Inject

open class ShowsAction @Inject constructor() : Action() {
	object Load: ShowsAction()

    fun load(): ShowsAction = Load
}

open class ShowsState @Inject constructor() : State() {
	data class Loading(val visible: Boolean) : ShowsState()
	data class Success(val items: List<TvShowVM>) : ShowsState()

	fun success(items: List<TvShowVM>): ShowsState = Success(items)
}

open class ShowsRoute @Inject constructor() : Route() {

}