package com.alorma.myapplication.ui.splash

import com.alorma.myapplication.commons.observeOnUI
import com.alorma.myapplication.domain.usecase.LoadConfigurationUseCase
import com.alorma.myapplication.ui.common.BaseViewModel
import com.alorma.myapplication.ui.common.Event
import com.alorma.myapplication.ui.common.Route
import com.alorma.myapplication.ui.common.State
import javax.inject.Inject

class SplashViewModel @Inject constructor(
        private val splashRoute: SplashRoutes,
        private val splashNavigator: SplashNavigator,
        private val loadConfigurationUseCase: LoadConfigurationUseCase) :
        BaseViewModel<State, SplashActions.SplashAction, Event>() {

    override infix fun reduce(action: SplashActions.SplashAction) {
        when (action) {
            SplashActions.SplashAction.Load -> onLoad()
        }
    }

    private fun onLoad() {
        val disposable = loadConfigurationUseCase.execute()
                .observeOnUI()
                .subscribe(
                        { splashNavigator navigate splashRoute.main() },
                        { splashNavigator navigate splashRoute.error() }
                )
        addDisposable(disposable)
    }
}