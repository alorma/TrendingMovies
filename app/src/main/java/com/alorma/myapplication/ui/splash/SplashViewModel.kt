package com.alorma.myapplication.ui.splash

import com.alorma.domain.usecase.LoadConfigurationUseCase
import com.alorma.myapplication.common.observeOnUI
import com.alorma.myapplication.ui.common.BaseViewModel
import com.alorma.myapplication.ui.common.Event
import com.alorma.myapplication.ui.common.State

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