package com.alorma.myapplication.ui.shows

import com.alorma.myapplication.ui.common.BasePresenter
import javax.inject.Inject

class ShowsPresenter @Inject constructor(private val states: ShowsState): 
		BasePresenter<ShowsAction, ShowsState, ShowsRoute>() {

	override fun reduce(a: ShowsAction) {
		when (a) {
			ShowsAction.Load -> onLoad()
		}
	}

	fun onLoad() {
		render(states.success())
	}
}