package com.alorma.myapplication.ui.detail

import com.alorma.myapplication.commons.observeOnUI
import com.alorma.myapplication.domain.usecase.ObtainShowDetailUseCase
import com.alorma.myapplication.ui.common.BasePresenter
import com.alorma.rac1.commons.plusAssign
import javax.inject.Inject

class ShowDetailPresenter @Inject constructor(
        private val detailStates: DetailStates,
        private val detailRoutes: DetailRoutes,
        private val detailNavigator: DetailNavigator,
        private val useCase: ObtainShowDetailUseCase
) :
        BasePresenter<DetailActions.DetailAction, DetailStates.DetailState>() {

    override fun reduce(action: DetailActions.DetailAction) {
        when (action) {
            is DetailActions.DetailAction.Load -> load(action)
            DetailActions.DetailAction.Back -> detailNavigator navigate detailRoutes.back()
        }
    }

    private fun load(action: DetailActions.DetailAction.Load) {
        disposable += useCase.execute(action.id)
                .observeOnUI()
                .subscribe(
                        { render(detailStates success it) },
                        { render(detailStates error it) }
                )
    }
}