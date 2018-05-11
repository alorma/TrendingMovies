package com.alorma.myapplication.ui.splash

import com.alorma.myapplication.ui.common.BasePresenter
import com.alorma.myapplication.ui.common.State
import javax.inject.Inject

class SplashPresenter @Inject constructor(
        private val splashRoute: SplashRoutes,
        private val splashNavigator: SplashNavigator) :
        BasePresenter<SplashActions.SplashAction, State>() {

    override fun reduce(action: SplashActions.SplashAction) {
        when (action) {
            SplashActions.SplashAction.Load -> onLoad()
        }
    }

    private fun onLoad() {
        splashNavigator navigate splashRoute.main()
    }
}