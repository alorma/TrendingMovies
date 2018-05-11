package com.alorma.myapplication.ui.splash

import com.alorma.myapplication.ui.common.Action
import javax.inject.Inject

class SplashActions @Inject constructor() {
    sealed class SplashAction : Action() {
        object Load : SplashAction()
    }

    fun load(): SplashAction = SplashAction.Load
}