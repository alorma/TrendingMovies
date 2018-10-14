package com.alorma.myapplication.ui.splash

import assertk.assert
import assertk.assertions.isEqualTo
import com.alorma.domain.exception.DataOriginException
import com.alorma.domain.repository.ConfigurationRepository
import com.alorma.domain.usecase.ObtainConfigurationUseCase
import com.alorma.myapplication.common.getConfig
import com.alorma.myapplication.ui.BaseViewModelTest
import com.alorma.presentation.common.*
import com.alorma.presentation.splash.SplashActions
import com.alorma.presentation.splash.SplashRoutes
import com.alorma.presentation.splash.SplashViewModel
import com.nhaarman.mockito_kotlin.*
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Test

class SplashViewModelTest : BaseViewModelTest<
        State,
        SplashRoutes.SplashRoute,
        SplashActions.SplashAction,
        Event>() {

    private lateinit var actions: SplashActions
    private val configRepository: ConfigurationRepository = mock()

    override fun createStateCaptor(): KArgumentCaptor<State> = argumentCaptor()
    override fun createEventCaptor(): KArgumentCaptor<EventHandler<Event>> = argumentCaptor()
    override fun createRouteCaptor(): KArgumentCaptor<SplashRoutes.SplashRoute> = argumentCaptor()

    override fun createViewModel(dispatchers: ViewModelDispatchers): BaseViewModel<State, SplashRoutes.SplashRoute, SplashActions.SplashAction, Event> {
        actions = SplashActions()
        val configurationUseCase = ObtainConfigurationUseCase(configRepository)
        return SplashViewModel(configurationUseCase, SplashRoutes(), dispatchers)
    }

    @Test
    fun onLoadSplash_with_valid_configuration_route_main() {
        runBlocking {
            given(configRepository.getConfig()).willReturn(getConfig())
        }

        captureRoute { actions.load() }

        assert(routeCaptor.firstValue).isEqualTo(SplashRoutes.SplashRoute.Main)
    }

    @Test
    fun onLoadSplash_with_error_configuration_route_main() {
        runBlocking {
            doAnswer { throw DataOriginException() }.whenever(configRepository).getConfig()
        }

        captureRoute { actions.load() }

        assert(routeCaptor.firstValue).isEqualTo(SplashRoutes.SplashRoute.Error)
    }

}