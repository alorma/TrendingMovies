package com.alorma.myapplication.ui.shows

import com.alorma.myapplication.commons.observeOnUI
import com.alorma.myapplication.domain.usecase.ObtainShowsUseCase
import com.alorma.myapplication.ui.common.BasePresenter
import com.alorma.rac1.commons.plusAssign
import javax.inject.Inject

class ShowsPresenter @Inject constructor(private val states: ShowsStates,
                                         private val routes: ShowsRoutes,
                                         private val obtainShowsUseCase: ObtainShowsUseCase) :
        BasePresenter<ShowsActions.ShowsAction, ShowsStates.ShowsState, ShowsRoutes.ShowsRoute>() {

    override fun reduce(action: ShowsActions.ShowsAction) {
        when (action) {
            ShowsActions.ShowsAction.Load -> onLoad()
            ShowsActions.ShowsAction.LoadPage -> onLoadPage()
            is ShowsActions.ShowsAction.OpenDetail -> onOpenDetail(action)
        }
    }

    private fun onLoad() {
        disposable += obtainShowsUseCase.execute()
                .observeOnUI()
                .doOnSubscribe { render(states loading true) }
                .doOnSuccess { render(states loading false) }
                .doOnError { render(states loading false) }
                .subscribe({
                    render(states success it)
                }, {
                    render(states error it)
                })
    }

    private fun onLoadPage() {
        disposable += obtainShowsUseCase.executeNextPage()
                .observeOnUI()
                .doOnSubscribe { render(states loading true) }
                .doOnSuccess { render(states loading false) }
                .doOnError { render(states loading false) }
                .subscribe({
                    render(states success it)
                }, {
                    render(states error it)
                })
    }

    private fun onOpenDetail(action: ShowsActions.ShowsAction.OpenDetail) =
            navigate(routes detail action.id)
}