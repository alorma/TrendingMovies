package com.alorma.myapplication.ui.shows

import com.alorma.myapplication.ui.common.BaseView
import com.alorma.myapplication.ui.shows.ShowsPresenter
import com.alorma.myapplication.ui.shows.ShowsAction
import com.alorma.myapplication.ui.shows.ShowsState
import com.alorma.myapplication.ui.shows.ShowsRoute
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.MockitoAnnotations
import com.nhaarman.mockito_kotlin.*

class ShowsPresenterTest {

	private lateinit var presenter: ShowsPresenter
	private lateinit var actions: ShowsAction
	private lateinit var states: ShowsState
	private lateinit var view: BaseView<ShowsRoute, ShowsState>

    @Captor
	private lateinit var captor: ArgumentCaptor<ShowsState>

	@Before
	fun setup() {
		MockitoAnnotations.initMocks(this)
		view = mock()

		actions = ShowsAction()
		states = ShowsState()

		presenter = ShowsPresenter(states)
		presenter init view
	}

	@Test
	fun onLoad_renderSuccess() {
		presenter reduce actions.load()

		verify(view).render(capture(captor))

		assertTrue(captor.value is ShowsState.Success)
	}

}