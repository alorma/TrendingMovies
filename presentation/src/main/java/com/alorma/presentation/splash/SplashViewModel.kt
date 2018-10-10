package com.alorma.presentation.splash

import com.alorma.domain.usecase.LoadConfigurationUseCase
import com.alorma.presentation.common.BaseViewModel
import com.alorma.presentation.common.Event
import com.alorma.presentation.common.State
import com.alorma.presentation.common.observeOnUI

class SplashViewModel(
        private val loadConfigurationUseCase: LoadConfigurationUseCase,
        private val splashRoute: SplashRoutes
) :
        BaseViewModel<State, SplashRoutes.SplashRoute, SplashActions.SplashAction, Event>() {

    override infix fun reduce(action: SplashActions.SplashAction) {
        when (action) {
            SplashActions.SplashAction.Load -> onLoad()
        }
    }

    private fun onLoad() {
        val disposable = loadConfigurationUseCase.execute()
                .observeOnUI()
                .subscribe(
                        { navigate(splashRoute.main()) },
                        { navigate(splashRoute.error()) }
                )
        addDisposable(disposable)
    }
}