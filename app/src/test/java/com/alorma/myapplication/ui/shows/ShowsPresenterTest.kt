package com.alorma.myapplication.ui.shows

import com.alorma.myapplication.configureRxThreading
import com.alorma.myapplication.data.net.ShowsApi
import com.alorma.myapplication.data.net.ShowsDataSource
import com.alorma.myapplication.domain.repository.ShowsRepository
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
import com.alorma.myapplication.data.net.ShowsMapper as NetworkMapper

class ShowsPresenterTest {

    private lateinit var showsApi: ShowsApi
    private lateinit var resources: ResourcesProvider

    private lateinit var mapper: ShowsMapper
    private lateinit var presenter: ShowsPresenter
    private lateinit var actions: ShowsAction
    private lateinit var states: ShowsState
    private lateinit var routes: ShowsRoute
    private lateinit var view: BaseView<ShowsRoute, ShowsState>

    @Captor
    private lateinit var stateCaptor: ArgumentCaptor<ShowsState>

    @Captor
    private lateinit var routeCaptor: ArgumentCaptor<ShowsRoute>

    init {
        configureRxThreading()
    }

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        showsApi = mock()
        view = mock()
        resources = mock<ResourcesProvider>().apply {
            given(getString(anyInt())).willReturn("")
        }

        mapper = ShowsMapper(resources)
        actions = ShowsAction()
        states = ShowsState()
        routes = ShowsRoute()

        val showsDs = ShowsDataSource(showsApi, NetworkMapper())
        val showsRepository = ShowsRepository(showsDs)
        val useCase = ObtainShowsUseCase(showsRepository)

        presenter = ShowsPresenter(states, routes, mapper, useCase)
        presenter init view
    }

    @Test
    fun onLoad_renderLoadings() {
        given(showsApi.listAll()).willReturn(Single.just(listOf(mock())))

        presenter reduce actions.load()

        verify(view, times(3)).render(capture(stateCaptor))

        with(stateCaptor.allValues) {
            assertTrue(get(0) is ShowsState.Loading)
            assertTrue((get(0) as ShowsState.Loading).visible)
            assertTrue(get(1) is ShowsState.Loading)
            assertFalse((get(1) as ShowsState.Loading).visible)
        }
    }

    @Test
    fun onLoad_renderSuccess() {
        given(showsApi.listAll()).willReturn(Single.just(listOf(mock())))

        presenter reduce actions.load()

        verify(view, times(3)).render(capture(stateCaptor))

        assertTrue(stateCaptor.allValues[2] is ShowsState.Success)
    }

    @Test
    fun onLoadSomeItems_renderSameNumberSuccess() {
        given(showsApi.listAll()).willReturn(Single.just(listOf(mock(), mock(), mock())))

        presenter reduce actions.load()

        verify(view, times(3)).render(capture(stateCaptor))

        val state = stateCaptor.allValues[2]
        assertTrue(state is ShowsState.Success)
        assertEquals(3, (state as ShowsState.Success).items.size)
    }

    @Test
    fun onLoadWithError_renderError() {
        given(showsApi.listAll()).willReturn(Single.error(Exception()))

        presenter reduce actions.load()

        verify(view, times(3)).render(capture(stateCaptor))

        assertTrue(stateCaptor.allValues[2] is ShowsState.Error)
    }

    @Test
    fun onOpenDetail_navigateToDetailRoute() {
        presenter reduce actions.detail(mock<TvShowVM>().apply { given(id).willReturn(12) })

        verify(view).navigate(capture(routeCaptor))

        assertTrue(routeCaptor.value is ShowsRoute.DetailRoute)
        assertEquals(12, (routeCaptor.value as ShowsRoute.DetailRoute).id)
    }

}