package com.alorma.presentation.splash

import com.alorma.domain.usecase.ObtainConfigurationUseCase
import com.alorma.presentation.common.BaseViewModel
import com.alorma.presentation.common.Event
import com.alorma.presentation.common.State
import com.alorma.presentation.common.ViewModelDispatchers

class SplashViewModel(
        private val loadConfigurationUseCase: ObtainConfigurationUseCase,
        private val splashRoute: SplashRoutes,
        dispatchers: ViewModelDispatchers
) :
        BaseViewModel<State,
                SplashRoutes.SplashRoute,
                SplashActions.SplashAction,
                Event>(dispatchers) {

    override infix fun reduce(action: SplashActions.SplashAction) {
        when (action) {
            SplashActions.SplashAction.Load -> onLoad()
        }
    }

    private fun onLoad() {
        val error = object : ErrorHandler {
            override fun onError(exception: Throwable) {
                navigate(splashRoute.error())
            }
        }

        launch(error) {
            loadConfigurationUseCase.execute()
            navigate(splashRoute.main())
        }
    }
}