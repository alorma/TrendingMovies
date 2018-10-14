package com.alorma.myapplication.ui.splash

/*
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
        */