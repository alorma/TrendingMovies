package com.alorma.myapplication.ui.splash

import com.alorma.myapplication.commons.observeOnUI
import com.alorma.myapplication.domain.usecase.LoadConfigurationUseCase
import com.alorma.myapplication.ui.common.BasePresenter
import com.alorma.myapplication.ui.common.State
import com.alorma.rac1.commons.plusAssign
import javax.inject.Inject

class SplashPresenter @Inject constructor(
        private val splashRoute: SplashRoutes,
        private val splashNavigator: SplashNavigator,
        private val loadConfigurationUseCase: LoadConfigurationUseCase) :
        BasePresenter<SplashActions.SplashAction, State>() {

    override fun reduce(action: SplashActions.SplashAction) {
        when (action) {
            SplashActions.SplashAction.Load -> onLoad()
        }
    }

    private fun onLoad() {
        disposable += loadConfigurationUseCase.execute()
                .observeOnUI()
                .subscribe(
                        { splashNavigator navigate splashRoute.main() },
                        { splashNavigator navigate splashRoute.error() }
                )
    }
}