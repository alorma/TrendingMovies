package com.alorma.presentation.splash

import com.alorma.domain.usecase.ObtainConfigurationUseCase
import com.alorma.presentation.common.BaseViewModel
import com.alorma.presentation.common.Event
import com.alorma.presentation.common.State
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.launch

class SplashViewModel(
        private val loadConfigurationUseCase: ObtainConfigurationUseCase,
        private val splashRoute: SplashRoutes
) :
        BaseViewModel<State, SplashRoutes.SplashRoute, SplashActions.SplashAction, Event>() {

    override infix fun reduce(action: SplashActions.SplashAction) {
        when (action) {
            SplashActions.SplashAction.Load -> onLoad()
        }
    }

    private fun onLoad() {
        val job = GlobalScope.launch {
            try {
                loadConfigurationUseCase.execute()
                navigate(splashRoute.main())
            } catch (e: Exception) {
                navigate(splashRoute.error())
            }
        }
        addJob(job)
    }
}