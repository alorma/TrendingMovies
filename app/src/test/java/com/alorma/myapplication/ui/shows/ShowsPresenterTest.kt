package com.alorma.myapplication.ui.shows

import com.alorma.myapplication.configureRxThreading
import com.alorma.myapplication.data.net.ShowsApi
import com.alorma.myapplication.data.net.ShowsDataSource
import com.alorma.myapplication.domain.repository.ShowsRepository
import com.alorma.myapplication.domain.usecase.ObtainShowsUseCase
import com.alorma.myapplication.ui.common.BaseView
import com.nhaarman.mockito_kotlin.capture
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import io.reactivex.Single
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.MockitoAnnotations
import com.alorma.myapplication.data.net.ShowsMapper as NetworkMapper

class ShowsPresenterTest {

    private lateinit var showsApi: ShowsApi

    private lateinit var mapper: ShowsMapper
    private lateinit var presenter: ShowsPresenter
    private lateinit var actions: ShowsAction
    private lateinit var states: ShowsState
    private lateinit var view: BaseView<ShowsRoute, ShowsState>

    @Captor
    private lateinit var captor: ArgumentCaptor<ShowsState>

    init {
        configureRxThreading()
    }

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        showsApi = mock()
        view = mock()

        mapper = ShowsMapper()
        actions = ShowsAction()
        states = ShowsState()

        val showsDs = ShowsDataSource(showsApi, NetworkMapper())
        val showsRepository = ShowsRepository(showsDs)
        val useCase = ObtainShowsUseCase(showsRepository)

        presenter = ShowsPresenter(states, mapper, useCase)
        presenter init view
    }

    @Test
    fun onLoad_renderSuccess() {
        given(showsApi.listAll()).willReturn(Single.just(listOf(mock())))
        presenter reduce actions.load()

        verify(view).render(capture(captor))

        assertTrue(captor.value is ShowsState.Success)
    }

}