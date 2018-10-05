package com.alorma.myapplication.ui.splash

import assertk.assert
import assertk.assertions.isEqualTo
import com.alorma.myapplication.domain.usecase.LoadConfigurationUseCase
import com.alorma.myapplication.ui.BaseViewModelTest
import com.alorma.myapplication.ui.common.BaseViewModel
import com.alorma.myapplication.ui.common.Event
import com.alorma.myapplication.ui.common.EventHandler
import com.alorma.myapplication.ui.common.State
import com.nhaarman.mockito_kotlin.KArgumentCaptor
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.mock
import io.reactivex.Completable
import org.junit.Test

class SplashViewModelTest : BaseViewModelTest<State, SplashRoutes.SplashRoute, SplashActions.SplashAction, Event>() {

    private lateinit var actions: SplashActions
    private lateinit var loadConfigurationUseCase: LoadConfigurationUseCase

    override fun createStateCaptor(): KArgumentCaptor<State> = argumentCaptor()
    override fun createEventCaptor(): KArgumentCaptor<EventHandler<Event>> = argumentCaptor()
    override fun createRouteCaptor(): KArgumentCaptor<SplashRoutes.SplashRoute> = argumentCaptor()

    override fun createViewModel(): BaseViewModel<State, SplashRoutes.SplashRoute, SplashActions.SplashAction, Event> {
        actions = SplashActions()
        loadConfigurationUseCase = mock()
        return SplashViewModel(loadConfigurationUseCase, SplashRoutes())
    }

    @Test
    fun onLoadSplash_with_valid_configuration_route_main() {
        given(loadConfigurationUseCase.execute()).willReturn(Completable.complete())

        captureRoute { actions.load() }

        assert(routeCaptor.firstValue).isEqualTo(SplashRoutes.SplashRoute.Main)
    }

    @Test
    fun onLoadSplash_with_error_configuration_route_main() {
        given(loadConfigurationUseCase.execute()).willReturn(Completable.error(Exception()))

        captureRoute { actions.load() }

        assert(routeCaptor.firstValue).isEqualTo(SplashRoutes.SplashRoute.Error)
    }

}