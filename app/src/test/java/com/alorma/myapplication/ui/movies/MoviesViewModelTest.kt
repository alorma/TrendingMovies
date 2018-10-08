package com.alorma.myapplication.ui.movies

import assertk.assert
import assertk.assertions.*
import com.alorma.myapplication.common.getResourcesProvider
import com.alorma.data.cache.LocalMoviesDataSource
import com.alorma.data.net.*
import com.alorma.data.repository.MoviesRepositoryImpl
import com.alorma.domain.usecase.ObtainConfigurationUseCase
import com.alorma.domain.usecase.ObtainMoviesUseCase
import com.alorma.myapplication.ui.BaseViewModelTest
import com.alorma.presentation.common.BaseViewModel
import com.alorma.presentation.common.Event
import com.alorma.presentation.common.EventHandler
import com.alorma.presentation.movies.*
import com.nhaarman.mockito_kotlin.KArgumentCaptor
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.mock
import io.reactivex.Single
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import com.alorma.data.net.MoviesMapper as NetworkMapper

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

    override fun createViewModel():
            BaseViewModel<MoviesStates.MovieState, MoviesRoutes.MovieRoute, MoviesActions.MovieAction, Event> {
        movieApi = mock()

        val resources = getResourcesProvider()

        val mapper = MoviesMapper(resources)
        states = MoviesStates(mapper)
        actions = MoviesActions()
        routes = MoviesRoutes()

        val networkDs = NetworkMoviesDataSource(movieApi, NetworkMapper(DateParser()))
        val cacheDs = LocalMoviesDataSource()

        val moviesRepository = MoviesRepositoryImpl(networkDs, cacheDs)
        val useCase = ObtainMoviesUseCase(moviesRepository)
        val configUseCase = mock<ObtainConfigurationUseCase>().apply {
            given(execute()).willReturn(Single.just(mock()))
        }

        return MoviesViewModel(states, routes, useCase, configUseCase)
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

        assert(stateCaptor.thirdValue).isInstanceOf(MoviesStates.MovieState.Success::class.java)
    }

    @Test
    fun onLoadPage_renderSuccess() {
        val response = PagedResponse(0, 0, listOf(generateMovieDto()))
        given(movieApi.listAll()).willReturn(Single.just(response))

        captureState(3) { actions.loadPage() }

        assert(stateCaptor.thirdValue).isInstanceOf(MoviesStates.MovieState.Success::class.java)
    }

    @Test
    fun onLoadSomeItems_renderSameNumberSuccess() {
        val response = PagedResponse(0, 0, listOf(generateMovieDto(), generateMovieDto(), generateMovieDto()))
        given(movieApi.listAll()).willReturn(Single.just(response))

        captureState(3) { actions.load() }

        val state = stateCaptor.thirdValue
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
        given(movieApi.listAll()).willReturn(Single.error(Exception()))

        captureState(3) { actions.load() }

        assert(stateCaptor.thirdValue).isInstanceOf(MoviesStates.MovieState.Error::class.java)
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