package com.alorma.myapplication.ui.shows

import com.alorma.myapplication.configureRxThreading
import com.alorma.myapplication.data.net.DateParser
import com.alorma.myapplication.data.net.PagedResponse
import com.alorma.myapplication.data.net.ShowsApi
import com.alorma.myapplication.data.net.TvShowDto
import com.alorma.myapplication.domain.model.Configuration
import com.alorma.myapplication.domain.repository.ShowsRepository
import com.alorma.myapplication.domain.usecase.ObtainConfigurationUseCase
import com.alorma.myapplication.domain.usecase.ObtainShowsUseCase
import com.alorma.myapplication.ui.common.BaseView
import com.alorma.myapplication.ui.common.ResourcesProvider
import com.nhaarman.mockito_kotlin.*
import io.reactivex.Single
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Captor
import org.mockito.MockitoAnnotations
import com.alorma.myapplication.data.cache.ShowsDataSource as Cache
import com.alorma.myapplication.data.net.ShowsDataSource as Network
import com.alorma.myapplication.data.net.ShowsMapper as NetworkMapper

class ShowsPresenterTest {

    private lateinit var showsApi: ShowsApi

    private lateinit var presenter: ShowsPresenter
    private lateinit var actions: ShowsActions
    private lateinit var states: ShowsStates
    private lateinit var routes: ShowsRoutes
    private lateinit var view: BaseView<ShowsStates.ShowsState>

    @Captor
    private lateinit var stateCaptor: ArgumentCaptor<ShowsStates.ShowsState>

    @Captor
    private lateinit var routeCaptor: ArgumentCaptor<ShowsRoutes.ShowsRoute>

    private lateinit var navigator: ShowsNavigator

    init {
        configureRxThreading()
    }

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        showsApi = mock()
        view = mock()
        navigator = mock()
        val resources = mock<ResourcesProvider>().apply {
            given(getString(anyInt())).willReturn("")
        }

        val mapper = ShowsMapper(resources)
        states = ShowsStates(mapper)
        actions = ShowsActions()
        routes = ShowsRoutes()

        val networkDs = Network(showsApi, NetworkMapper(DateParser()))
        val cacheDs = Cache()

        val showsRepository = ShowsRepository(networkDs, cacheDs)
        val useCase = ObtainShowsUseCase(showsRepository)
        val configUseCase = mock<ObtainConfigurationUseCase>().apply {
            given(execute()).willReturn(Single.just(mock()))
        }

        presenter = ShowsPresenter(states, routes, useCase, configUseCase, navigator)
        presenter init view
    }

    @Test
    fun onLoad_renderLoadings() {
        val response = PagedResponse(0, 0, listOf(generateTvShowDto()))
        given(showsApi.listAll()).willReturn(Single.just(response))

        presenter reduce actions.load()

        verify(view, times(3)).render(capture(stateCaptor))

        with(stateCaptor.allValues) {
            assertTrue(get(0) is ShowsStates.ShowsState.Loading)
            assertTrue((get(0) as ShowsStates.ShowsState.Loading).visible)
            assertTrue(get(1) is ShowsStates.ShowsState.Loading)
            assertFalse((get(1) as ShowsStates.ShowsState.Loading).visible)
        }
    }

    @Test
    fun onLoadPage_renderLoadings() {
        val response = PagedResponse(0, 0, listOf(generateTvShowDto()))
        given(showsApi.listAll()).willReturn(Single.just(response))

        presenter reduce actions.loadPage()

        verify(view, times(3)).render(capture(stateCaptor))

        with(stateCaptor.allValues) {
            assertTrue(get(0) is ShowsStates.ShowsState.Loading)
            assertTrue((get(0) as ShowsStates.ShowsState.Loading).visible)
            assertTrue(get(1) is ShowsStates.ShowsState.Loading)
            assertFalse((get(1) as ShowsStates.ShowsState.Loading).visible)
        }
    }

    @Test
    fun onLoad_renderSuccess() {
        val response = PagedResponse(0, 0, listOf(generateTvShowDto()))
        given(showsApi.listAll()).willReturn(Single.just(response))

        presenter reduce actions.load()

        verify(view, times(3)).render(capture(stateCaptor))

        assertTrue(stateCaptor.allValues[2] is ShowsStates.ShowsState.Success)
    }


    @Test
    fun onLoadPage_renderSuccess() {
        val response = PagedResponse(0, 0, listOf(generateTvShowDto()))
        given(showsApi.listAll()).willReturn(Single.just(response))

        presenter reduce actions.loadPage()

        verify(view, times(3)).render(capture(stateCaptor))

        assertTrue(stateCaptor.allValues[2] is ShowsStates.ShowsState.Success)
    }

    @Test
    fun onLoadSomeItems_renderSameNumberSuccess() {
        val response = PagedResponse(0, 0, listOf(generateTvShowDto(), generateTvShowDto(), generateTvShowDto()))
        given(showsApi.listAll()).willReturn(Single.just(response))

        presenter reduce actions.load()

        verify(view, times(3)).render(capture(stateCaptor))

        val state = stateCaptor.allValues[2]
        assertTrue(state is ShowsStates.ShowsState.Success)
        assertEquals(3, (state as ShowsStates.ShowsState.Success).items.size)
    }

    @Test
    fun onLoadPageSomeItems_renderAllItems() {
        val response = PagedResponse(0, 0,
                listOf(generateTvShowDto(), generateTvShowDto(), generateTvShowDto()))
        given(showsApi.listAll()).willReturn(Single.just(response))
        val responsePage = PagedResponse(0, 0, listOf(generateTvShowDto()))
        given(showsApi.listPage(anyInt())).willReturn(Single.just(responsePage))

        presenter reduce actions.load()
        presenter reduce actions.loadPage()

        verify(view, times(6)).render(capture(stateCaptor))

        val firstState = stateCaptor.allValues[2]
        assertTrue(firstState is ShowsStates.ShowsState.Success)
        assertEquals(3, (firstState as ShowsStates.ShowsState.Success).items.size)
        val secondState = stateCaptor.allValues[5]
        assertTrue(secondState is ShowsStates.ShowsState.Success)
        assertEquals(4, (secondState as ShowsStates.ShowsState.Success).items.size)
    }

    @Test
    fun onLoadWithError_renderError() {
        given(showsApi.listAll()).willReturn(Single.error(Exception()))

        presenter reduce actions.load()

        verify(view, times(3)).render(capture(stateCaptor))

        assertTrue(stateCaptor.allValues[2] is ShowsStates.ShowsState.Error)
    }

    @Test
    fun onOpenDetail_navigateToDetailRoute() {
        presenter reduce actions.detail(getTvShow(12))

        verify(navigator) navigate capture(routeCaptor)

        assertTrue(routeCaptor.value is ShowsRoutes.ShowsRoute.DetailRoute)
        assertEquals(12, (routeCaptor.value as ShowsRoutes.ShowsRoute.DetailRoute).id)
    }

    private fun generateTvShowDto(id: Int = 0): TvShowDto = TvShowDto(id, "", "", "2017-04-10", "", "", 0f, listOf())
    private fun getTvShow(id: Int = 0): TvShowVM = TvShowVM(id, "", "")
}