package com.alorma.presentation.splash

import com.alorma.presentation.common.Action
import com.alorma.presentation.common.Route

class SplashActions {
    sealed class SplashAction : Action() {
        object Load : SplashAction()
    }

    fun load(): SplashAction = SplashAction.Load
}

class SplashRoutes {
    sealed class SplashRoute : Route() {
        object Main : SplashRoute()
        object Error : SplashRoute()
    }

    fun main(): SplashRoute = SplashRoute.Main
    fun error(): SplashRoute = SplashRoute.Error
}