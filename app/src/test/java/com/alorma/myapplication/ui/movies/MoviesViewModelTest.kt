package com.alorma.myapplication.ui.movies

import assertk.assert
import assertk.assertions.*
import com.alorma.domain.exception.DataOriginException
import com.alorma.domain.repository.ConfigurationRepository
import com.alorma.domain.repository.MoviesRepository
import com.alorma.domain.usecase.ObtainConfigurationUseCase
import com.alorma.domain.usecase.ObtainMoviesUseCase
import com.alorma.myapplication.common.getConfig
import com.alorma.myapplication.common.getMovieVM
import com.alorma.myapplication.common.getMovies
import com.alorma.myapplication.common.getResourcesProvider
import com.alorma.myapplication.ui.BaseViewModelTest
import com.alorma.presentation.common.Event
import com.alorma.presentation.common.EventHandler
import com.alorma.presentation.common.ViewModelDispatchers
import com.alorma.presentation.movies.*
import com.nhaarman.mockito_kotlin.*
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Test
import com.alorma.data.net.MoviesMapper as NetworkMapper

class MoviesViewModelTest : BaseViewModelTest<MoviesStates.MovieState,
        MoviesRoutes.MovieRoute,
        MoviesActions.MovieAction,
        Event>() {

    private var moviesRepository: MoviesRepository = mock()
    private var configRepository: ConfigurationRepository = mock()

    private lateinit var actions: MoviesActions
    private lateinit var states: MoviesStates
    private lateinit var routes: MoviesRoutes

    override fun createStateCaptor(): KArgumentCaptor<MoviesStates.MovieState> = argumentCaptor()
    override fun createEventCaptor(): KArgumentCaptor<EventHandler<Event>> = argumentCaptor()
    override fun createRouteCaptor(): KArgumentCaptor<MoviesRoutes.MovieRoute> = argumentCaptor()

    override fun createViewModel(dispatchers: ViewModelDispatchers): MoviesViewModel {
        val resources = getResourcesProvider()
        val mapper = MoviesMapper(resources)
        states = MoviesStates(mapper)
        actions = MoviesActions()
        routes = MoviesRoutes()

        val useCase = ObtainMoviesUseCase(moviesRepository)
        val configUseCase = ObtainConfigurationUseCase(configRepository)

        return MoviesViewModel(states, routes, useCase, configUseCase, dispatchers)
    }

    override fun setup() {
        super.setup()
        runBlocking {
            given(configRepository.getConfig()).willReturn(getConfig())
        }
    }

    @Test
    fun onLoad_renderLoadings() {
        runBlocking {
            given(moviesRepository.listAll()).willReturn(getMovies(1))
        }

        captureState(3) { actions.load() }

        with(stateCaptor.allValues) {
            with(get(0)) {
                assert(this).isInstanceOf(MoviesStates.MovieState.Loading::class.java)
                assert((this as MoviesStates.MovieState.Loading).visible).isTrue()
            }
            with(get(1)) {
                assert(this).isInstanceOf(MoviesStates.MovieState.Loading::class.java)
                assert((this as MoviesStates.MovieState.Loading).visible).isFalse()
            }
        }
    }

    @Test
    fun onLoadPage_renderLoadings() {
        runBlocking {
            given(moviesRepository.listNextPage()).willReturn(getMovies(1))
        }

        captureState(3) { actions.loadPage() }

        with(stateCaptor.allValues) {
            with(get(0)) {
                assert(this).isInstanceOf(MoviesStates.MovieState.Loading::class.java)
                assert((this as MoviesStates.MovieState.Loading).visible).isTrue()
            }
            with(get(1)) {
                assert(this).isInstanceOf(MoviesStates.MovieState.Loading::class.java)
                assert((this as MoviesStates.MovieState.Loading).visible).isFalse()
            }
        }
    }

    @Test
    fun onLoad_renderSuccess() {
        runBlocking {
            given(moviesRepository.listAll()).willReturn(getMovies(1))
        }

        captureState(3) { actions.load() }

        assert(stateCaptor.thirdValue).isInstanceOf(MoviesStates.MovieState.Success::class.java)
    }

    @Test
    fun onLoadPage_renderSuccess() {
        runBlocking {
            given(moviesRepository.listNextPage()).willReturn(getMovies(1))
        }

        captureState(3) { actions.loadPage() }

        assert(stateCaptor.thirdValue).isInstanceOf(MoviesStates.MovieState.Success::class.java)
    }

    @Test
    fun onLoadSomeItems_renderSameNumberSuccess() {
        runBlocking {
            given(moviesRepository.listAll()).willReturn(getMovies(3))
        }

        captureState(3) { actions.load() }

        val state = stateCaptor.thirdValue
        assert(state).isInstanceOf(MoviesStates.MovieState.Success::class.java)
        assert((state as MoviesStates.MovieState.Success).items).hasSize(3)
    }

    @Test
    fun onLoadPageSomeItems_renderAllItems() {
        runBlocking {
            given(moviesRepository.listAll()).willReturn(getMovies(3))
            given(moviesRepository.listNextPage()).willReturn(getMovies(4))
        }

        captureState(3) { actions.load() }

        val firstState = stateCaptor.thirdValue
        assert(firstState).isInstanceOf(MoviesStates.MovieState.Success::class.java)
        assert((firstState as MoviesStates.MovieState.Success).items).hasSize(3)

        captureState(4) { actions.loadPage() }

        val secondState = stateCaptor.allValues[3]
        assert(secondState).isInstanceOf(MoviesStates.MovieState.Success::class.java)
        assert((secondState as MoviesStates.MovieState.Success).items).hasSize(4)
    }

    @Test
    fun onLoadWithError_renderError() {
        runBlocking {
            doAnswer { throw DataOriginException() }.whenever(configRepository).getConfig()
        }

        captureState(3) { actions.load() }

        assert(stateCaptor.thirdValue).isInstanceOf(MoviesStates.MovieState.Error::class.java)
    }

    @Test
    fun onOpenDetail_navigateToDetailRoute() {
        captureRoute { actions.detail(getMovieVM(12)) }

        assert(routeCaptor.firstValue).isInstanceOf(MoviesRoutes.MovieRoute.DetailRoute::class.java)
        assert((routeCaptor.firstValue as MoviesRoutes.MovieRoute.DetailRoute).id).isEqualTo(12)
    }
}