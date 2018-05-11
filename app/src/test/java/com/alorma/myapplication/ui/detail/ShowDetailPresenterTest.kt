package com.alorma.myapplication.ui.detail

import com.alorma.myapplication.configureRxThreading
import com.alorma.myapplication.data.net.ShowsApi
import com.alorma.myapplication.data.net.TvShowDto
import com.alorma.myapplication.domain.model.Configuration
import com.alorma.myapplication.domain.model.Images
import com.alorma.myapplication.domain.model.TvShow
import com.alorma.myapplication.domain.repository.ShowsRepository
import com.alorma.myapplication.domain.usecase.ObtainConfigurationUseCase
import com.alorma.myapplication.domain.usecase.ObtainShowDetailUseCase
import com.alorma.myapplication.ui.common.BaseView
import com.alorma.myapplication.ui.common.ResourcesProvider
import com.nhaarman.mockito_kotlin.*
import io.reactivex.Single
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers
import org.mockito.Captor
import org.mockito.MockitoAnnotations
import java.util.*
import com.alorma.myapplication.data.cache.ShowsDataSource as Cache
import com.alorma.myapplication.data.net.ShowsDataSource as Network
import com.alorma.myapplication.data.net.ShowsMapper as NetworkMapper

class ShowDetailPresenterTest {

    init {
        configureRxThreading()
    }

    private lateinit var showsApi: ShowsApi
    private lateinit var cacheDs: Cache
    private lateinit var navigator: DetailNavigator
    private lateinit var presenter: ShowDetailPresenter
    private val actions: DetailActions = DetailActions()

    @Captor
    private lateinit var stateCaptor: ArgumentCaptor<DetailStates.DetailState>
    @Captor
    private lateinit var routeCaptor: ArgumentCaptor<DetailRoutes.DetailRoute>

    private lateinit var view: BaseView<DetailStates.DetailState>

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        navigator = mock()
        showsApi = mock()
        view = mock()

        val networkDs = Network(showsApi, NetworkMapper())
        cacheDs = mock()

        val showsRepository = ShowsRepository(networkDs, cacheDs)
        val useCase = ObtainShowDetailUseCase(showsRepository)
        val configUseCase = mock<ObtainConfigurationUseCase>().apply {
            given(execute()).willReturn(Single.just(mock()))
        }

        val resources = mock<ResourcesProvider>().apply {
            given(getString(ArgumentMatchers.anyInt())).willReturn("")
        }

        val mapper = DetailMapper(resources)

        presenter = ShowDetailPresenter(DetailStates(mapper), DetailRoutes(), navigator, useCase, configUseCase)
        presenter init view
    }

    @Test
    fun onActionBack_navigateBack() {
        presenter reduce actions.back()

        verify(navigator) navigate capture(routeCaptor)
        assertTrue(routeCaptor.value === DetailRoutes.DetailRoute.Back)
    }

    @Test
    fun onActionLoad_serveFromCache_renderSuccess() {
        given(cacheDs.get(eq(12))).willReturn(getTvShow(12))

        presenter reduce actions.load(12)

        verify(view).render(capture(stateCaptor))

        assertTrue(stateCaptor.value is DetailStates.DetailState.Success)
    }

    @Test
    fun onActionLoad_serveFromApi_renderSuccess() {
        given(cacheDs.get(eq(12))).willReturn(null)
        given(showsApi.item(eq(12))).willReturn(Single.just(generateTvShowDto(12)))

        presenter reduce actions.load(12)

        verify(view).render(capture(stateCaptor))

        assertTrue(stateCaptor.value is DetailStates.DetailState.Success)
    }

    @Test
    fun onActionLoadError_renderError() {
        given(cacheDs.get(eq(12))).willThrow(RuntimeException())

        presenter reduce actions.load(12)

        verify(view).render(capture(stateCaptor))

        assertTrue(stateCaptor.value is DetailStates.DetailState.Error)
    }

    private fun generateTvShowDto(id: Int = 0): TvShowDto = TvShowDto(id, "", "", "", "", "", 0f)
    private fun getTvShow(id: Int = 0): TvShow = TvShow(id, "", "", Images("", ""), Date())
}