package com.alorma.myapplication.ui.splash

import com.alorma.myapplication.ui.common.Action
import com.alorma.myapplication.ui.common.Route

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