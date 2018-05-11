package com.alorma.myapplication.ui.detail

import com.alorma.myapplication.configureRxThreading
import com.nhaarman.mockito_kotlin.capture
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.MockitoAnnotations

class ShowDetailPresenterTest {

    init {
        configureRxThreading()
    }

    private lateinit var navigator: DetailNavigator

    private lateinit var presenter: ShowDetailPresenter

    private val actions: DetailActions = DetailActions()

    @Captor
    private lateinit var routeCaptor: ArgumentCaptor<DetailRoutes.DetailRoute>

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        navigator = mock()
        presenter = ShowDetailPresenter(DetailStates(), DetailRoutes(), navigator)
    }

    @Test
    fun onActionBack_navigateBack() {
        presenter reduce actions.back()

        verify(navigator) navigate capture(routeCaptor)
        assertTrue(routeCaptor.value is DetailRoutes.DetailRoute.Back)
    }
}