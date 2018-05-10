package com.alorma.myapplication.ui.shows

import com.alorma.myapplication.commons.observeOnUI
import com.alorma.myapplication.domain.usecase.ObtainShowsUseCase
import com.alorma.myapplication.ui.common.BasePresenter
import com.alorma.rac1.commons.plusAssign
import javax.inject.Inject

class ShowsPresenter @Inject constructor(private val states: ShowsState,
                                         private val routes: ShowsRoute,
                                         private val mapper: ShowsMapper,
                                         private val obtainShowsUseCase: ObtainShowsUseCase) :
        BasePresenter<ShowsAction, ShowsState, ShowsRoute>() {

    override fun reduce(action: ShowsAction) {
        when (action) {
            ShowsAction.Load -> onLoad()
            ShowsAction.LoadPage -> onLoadPage()
            is ShowsAction.OpenDetail -> onOpenDetail(action)
        }
    }

    private fun onLoad() {
        disposable += obtainShowsUseCase.execute()
                .observeOnUI()
                .doOnSubscribe { render(states loading true) }
                .doOnSuccess { render(states loading false) }
                .doOnError { render(states loading false) }
                .subscribe({
                    render(states success mapper.map(it))
                }, {
                    render(states error mapper.error(it))
                })
    }

    private fun onLoadPage() {
        disposable += obtainShowsUseCase.executeNextPage()
                .observeOnUI()
                .doOnSubscribe { render(states loading true) }
                .doOnSuccess { render(states loading false) }
                .doOnError { render(states loading false) }
                .subscribe({
                    render(states success mapper.map(it))
                }, {
                    render(states error mapper.error(it))
                })
    }

    private fun onOpenDetail(action: ShowsAction.OpenDetail) = navigate(routes.detail(action.id))
}