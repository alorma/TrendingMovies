package com.alorma.myapplication.ui.movies

import assertk.assert
import assertk.assertions.*
import com.alorma.myapplication.common.getResourcesProvider
import com.alorma.myapplication.data.net.DateParser
import com.alorma.myapplication.data.net.MovieApi
import com.alorma.myapplication.data.net.MovieDto
import com.alorma.myapplication.data.net.PagedResponse
import com.alorma.myapplication.domain.repository.MoviesRepository
import com.alorma.myapplication.domain.usecase.ObtainConfigurationUseCase
import com.alorma.myapplication.domain.usecase.ObtainMoviesUseCase
import com.alorma.myapplication.ui.BaseViewModelTest
import com.alorma.myapplication.ui.common.BaseViewModel
import com.alorma.myapplication.ui.common.Event
import com.alorma.myapplication.ui.common.EventHandler
import com.alorma.myapplication.ui.common.Navigator
import com.nhaarman.mockito_kotlin.KArgumentCaptor
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.mock
import io.reactivex.Single
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import com.alorma.myapplication.data.cache.MoviesDataSource as Cache
import com.alorma.myapplication.data.net.MoviesDataSource as Network
import com.alorma.myapplication.data.net.MoviesMapper as NetworkMapper

class MoviesViewModelTest : BaseViewModelTest<MoviesStates.MovieState,
        MoviesRoutes.MovieRoute,
        MoviesActions.MovieAction,
        Event>() {

    private lateinit var movieApi: MovieApi

    private lateinit var actions: MoviesActions
    private lateinit var states: MoviesStates
    private lateinit var routes: MoviesRoutes

    override fun createStateCaptor(): KArgumentCaptor<MoviesStates.MovieState> = argumentCaptor()
    override fun createEventCaptor(): KArgumentCaptor<EventHandler<Event>> = argumentCaptor()
    override fun createRouteCaptor(): KArgumentCaptor<MoviesRoutes.MovieRoute> = argumentCaptor()

    override fun createViewModel(navigator: Navigator<MoviesRoutes.MovieRoute>):
            BaseViewModel<MoviesStates.MovieState, MoviesRoutes.MovieRoute, MoviesActions.MovieAction, Event> {
        movieApi = mock()

        val resources = getResourcesProvider()

        val mapper = MoviesMapper(resources)
        states = MoviesStates(mapper)
        actions = MoviesActions()
        routes = MoviesRoutes()

        val networkDs = Network(movieApi, NetworkMapper(DateParser()))
        val cacheDs = Cache()

        val moviesRepository = MoviesRepository(networkDs, cacheDs)
        val useCase = ObtainMoviesUseCase(moviesRepository)
        val configUseCase = mock<ObtainConfigurationUseCase>().apply {
            given(execute()).willReturn(Single.just(mock()))
        }

        return MoviesViewModel(states, routes, useCase, configUseCase, navigator)
    }

    @Test
    fun onLoad_renderLoadings() {
        val response = PagedResponse(0, 0, listOf(generateMovieDto()))
        given(movieApi.listAll()).willReturn(Single.just(response))

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
        val response = PagedResponse(0, 0, listOf(generateMovieDto()))
        given(movieApi.listAll()).willReturn(Single.just(response))

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
        val response = PagedResponse(0, 0, listOf(generateMovieDto()))
        given(movieApi.listAll()).willReturn(Single.just(response))

        captureState(3) { actions.load() }

        assert(stateCaptor.allValues[2]).isInstanceOf(MoviesStates.MovieState.Success::class.java)
    }

    @Test
    fun onLoadPage_renderSuccess() {
        val response = PagedResponse(0, 0, listOf(generateMovieDto()))
        given(movieApi.listAll()).willReturn(Single.just(response))

        captureState(3) { actions.loadPage() }

        assert(stateCaptor.allValues[2]).isInstanceOf(MoviesStates.MovieState.Success::class.java)
    }

    @Test
    fun onLoadSomeItems_renderSameNumberSuccess() {
        val response = PagedResponse(0, 0, listOf(generateMovieDto(), generateMovieDto(), generateMovieDto()))
        given(movieApi.listAll()).willReturn(Single.just(response))

        captureState(3) { actions.load() }

        val state = stateCaptor.allValues[2]
        assert(state).isInstanceOf(MoviesStates.MovieState.Success::class.java)
        assert((state as MoviesStates.MovieState.Success).items).hasSize(3)
    }

    @Test
    fun onLoadPageSomeItems_renderAllItems() {
        val response = PagedResponse(0, 0,
                listOf(generateMovieDto(), generateMovieDto(), generateMovieDto()))
        given(movieApi.listAll()).willReturn(Single.just(response))
        val responsePage = PagedResponse(0, 0, listOf(generateMovieDto()))
        given(movieApi.listPage(anyInt())).willReturn(Single.just(responsePage))

        captureState(3) { actions.load() }

        val firstState = stateCaptor.allValues[2]
        assert(firstState).isInstanceOf(MoviesStates.MovieState.Success::class.java)
        assert((firstState as MoviesStates.MovieState.Success).items).hasSize(3)

        captureState(6) { actions.loadPage() }

        val secondState = stateCaptor.allValues[5]
        assert(secondState).isInstanceOf(MoviesStates.MovieState.Success::class.java)
        assert((secondState as MoviesStates.MovieState.Success).items).hasSize(3)
    }

    @Test
    fun onLoadWithError_renderError() {
        given(movieApi.listAll()).willReturn(Single.error(Exception()))

        captureState(3) { actions.load() }

        assert(stateCaptor.allValues[2]).isInstanceOf(MoviesStates.MovieState.Error::class.java)
    }

    @Test
    fun onOpenDetail_navigateToDetailRoute() {
        captureRoute { actions.detail(getMovie(12)) }

        assert(routeCaptor.firstValue).isInstanceOf(MoviesRoutes.MovieRoute.DetailRoute::class.java)
        assert((routeCaptor.firstValue as MoviesRoutes.MovieRoute.DetailRoute).id).isEqualTo(12)
    }

    private fun generateMovieDto(id: Int = 0): MovieDto = MovieDto(id, "", "", "2017-04-10", "", "", 0f, listOf())
    private fun getMovie(id: Int = 0): MovieItemVM = MovieItemVM(id, "", "", "5.4")
}