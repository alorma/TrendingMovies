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
	class Success : ShowsState()

	fun success(): ShowsState = Success()
}

open class ShowsRoute @Inject constructor() : Route() {

}