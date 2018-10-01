package com.alorma.myapplication.ui.movies

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import com.alorma.myapplication.configureRxThreading
import com.alorma.myapplication.data.net.DateParser
import com.alorma.myapplication.data.net.MovieApi
import com.alorma.myapplication.data.net.MovieDto
import com.alorma.myapplication.data.net.PagedResponse
import com.alorma.myapplication.domain.repository.MoviesRepository
import com.alorma.myapplication.domain.usecase.ObtainConfigurationUseCase
import com.alorma.myapplication.domain.usecase.ObtainMoviesUseCase
import com.alorma.myapplication.ui.common.ResourcesProvider
import com.nhaarman.mockito_kotlin.*
import io.reactivex.Single
import junit.framework.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import com.alorma.myapplication.data.cache.MoviesDataSource as Cache
import com.alorma.myapplication.data.net.MoviesDataSource as Network
import com.alorma.myapplication.data.net.MoviesMapper as NetworkMapper

class MoviesPresenterTest {
/*
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var movieApi: MovieApi

    private lateinit var presenter: MoviesViewModel
    private lateinit var actions: MoviesActions
    private lateinit var states: MoviesStates
    private lateinit var routes: MoviesRoutes

    private lateinit var liveData: LiveData<MoviesStates.MovieState>

    @Mock
    lateinit var observer: Observer<MoviesStates.MovieState>

    @Captor
    private lateinit var stateCaptor: ArgumentCaptor<MoviesStates.MovieState>

    @Captor
    private lateinit var routeCaptor: ArgumentCaptor<MoviesRoutes.MovieRoute>

    private lateinit var navigator: MoviesNavigator

    init {
        configureRxThreading()
    }

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        movieApi = mock()
        liveData = mock()
        navigator = mock()
        val resources = mock<ResourcesProvider>().apply {
            given(getString(anyInt())).willReturn("")
        }

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

        presenter = MoviesViewModel(states, routes, useCase, configUseCase, navigator)

        liveData = presenter.liveData
        liveData.observeForever(observer)
    }

    @Test
    fun onLoad_renderLoadings() {
        val response = PagedResponse(0, 0, listOf(generateMovieDto()))
        given(movieApi.listAll()).willReturn(Single.just(response))

        presenter reduce actions.load()

        verify(observer, times(3)).onChanged(capture(stateCaptor))

        with(stateCaptor.allValues) {
            assertTrue(get(0) is MoviesStates.MovieState.Loading)
            assertTrue((get(0) as MoviesStates.MovieState.Loading).visible)
            assertTrue(get(1) is MoviesStates.MovieState.Loading)
            assertFalse((get(1) as MoviesStates.MovieState.Loading).visible)
        }
    }

    @Test
    fun onLoadPage_renderLoadings() {
        val response = PagedResponse(0, 0, listOf(generateMovieDto()))
        given(movieApi.listAll()).willReturn(Single.just(response))

        presenter reduce actions.loadPage()

        verify(observer, times(3)).onChanged(capture(stateCaptor))

        with(stateCaptor.allValues) {
            assertTrue(get(0) is MoviesStates.MovieState.Loading)
            assertTrue((get(0) as MoviesStates.MovieState.Loading).visible)
            assertTrue(get(1) is MoviesStates.MovieState.Loading)
            assertFalse((get(1) as MoviesStates.MovieState.Loading).visible)
        }
    }

    @Test
    fun onLoad_renderSuccess() {
        val response = PagedResponse(0, 0, listOf(generateMovieDto()))
        given(movieApi.listAll()).willReturn(Single.just(response))

        presenter reduce actions.load()

        verify(observer, times(3)).onChanged(capture(stateCaptor))

        assertTrue(stateCaptor.allValues[2] is MoviesStates.MovieState.Success)
    }


    @Test
    fun onLoadPage_renderSuccess() {
        val response = PagedResponse(0, 0, listOf(generateMovieDto()))
        given(movieApi.listAll()).willReturn(Single.just(response))

        presenter reduce actions.loadPage()

        verify(observer, times(3)).onChanged(capture(stateCaptor))

        assertTrue(stateCaptor.allValues[2] is MoviesStates.MovieState.Success)
    }

    @Test
    fun onLoadSomeItems_renderSameNumberSuccess() {
        val response = PagedResponse(0, 0, listOf(generateMovieDto(), generateMovieDto(), generateMovieDto()))
        given(movieApi.listAll()).willReturn(Single.just(response))

        presenter reduce actions.load()

        verify(observer, times(3)).onChanged(capture(stateCaptor))

        val state = stateCaptor.allValues[2]
        assertTrue(state is MoviesStates.MovieState.Success)
        assertEquals(3, (state as MoviesStates.MovieState.Success).items.size)
    }

    @Test
    fun onLoadPageSomeItems_renderAllItems() {
        val response = PagedResponse(0, 0,
                listOf(generateMovieDto(), generateMovieDto(), generateMovieDto()))
        given(movieApi.listAll()).willReturn(Single.just(response))
        val responsePage = PagedResponse(0, 0, listOf(generateMovieDto()))
        given(movieApi.listPage(anyInt())).willReturn(Single.just(responsePage))

        presenter reduce actions.load()
        presenter reduce actions.loadPage()

        verify(observer, times(6)).onChanged(capture(stateCaptor))

        val firstState = stateCaptor.allValues[2]
        assertTrue(firstState is MoviesStates.MovieState.Success)
        assertEquals(3, (firstState as MoviesStates.MovieState.Success).items.size)
        val secondState = stateCaptor.allValues[5]
        assertTrue(secondState is MoviesStates.MovieState.Success)
        assertEquals(4, (secondState as MoviesStates.MovieState.Success).items.size)
    }

    @Test
    fun onLoadWithError_renderError() {
        given(movieApi.listAll()).willReturn(Single.error(Exception()))

        presenter reduce actions.load()

        verify(observer, times(3)).onChanged(capture(stateCaptor))

        assertTrue(stateCaptor.allValues[2] is MoviesStates.MovieState.Error)
    }

    @Test
    fun onOpenDetail_navigateToDetailRoute() {
        presenter reduce actions.detail(getMovie(12))

        verify(navigator) navigate capture(routeCaptor)

        assertTrue(routeCaptor.value is MoviesRoutes.MovieRoute.DetailRoute)
        assertEquals(12, (routeCaptor.value as MoviesRoutes.MovieRoute.DetailRoute).id)
    }

    private fun generateMovieDto(id: Int = 0): MovieDto = MovieDto(id, "", "", "2017-04-10", "", "", 0f, listOf())
    private fun getMovie(id: Int = 0): MovieItemVM = MovieItemVM(id, "", "", "5.4")
*/
}