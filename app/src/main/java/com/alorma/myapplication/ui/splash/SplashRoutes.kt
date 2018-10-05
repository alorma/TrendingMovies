package com.alorma.myapplication.ui.splash

import com.alorma.myapplication.ui.common.Route

class SplashRoutes {
    sealed class SplashRoute : Route() {
        object Main : SplashRoute()
        object Error : SplashRoute()
    }

    fun main(): SplashRoute = SplashRoute.Main
    fun error(): SplashRoute = SplashRoute.Error
}