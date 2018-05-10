package com.alorma.myapplication.ui.shows

import com.alorma.myapplication.commons.observeOnUI
import com.alorma.myapplication.domain.usecase.ObtainShowsUseCase
import com.alorma.myapplication.ui.common.BasePresenter
import com.alorma.rac1.commons.plusAssign
import javax.inject.Inject

class ShowsPresenter @Inject constructor(private val states: ShowsState,
                                         private val mapper: ShowsMapper,
                                         private val obtainShowsUseCase: ObtainShowsUseCase) :
        BasePresenter<ShowsAction, ShowsState, ShowsRoute>() {

    override fun reduce(a: ShowsAction) {
        when (a) {
            ShowsAction.Load -> onLoad()
        }
    }

    private fun onLoad() {
        disposable += obtainShowsUseCase.execute()
                .observeOnUI()
                .subscribe({
                    render(states.success(mapper.map(it)))
                }, {

                })
    }
}