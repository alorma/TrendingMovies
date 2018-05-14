package com.alorma.myapplication.ui.search

import com.alorma.myapplication.ui.common.BaseView
import com.nhaarman.mockito_kotlin.capture
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyZeroInteractions
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.MockitoAnnotations

class SearchPresenterTest {

    private lateinit var actions: SearchActions
    private lateinit var presenter: SearchPresenter
    private lateinit var view: BaseView<SearchStates.SearchState>

    @Captor
    private lateinit var stateCaptor: ArgumentCaptor<SearchStates.SearchState>

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        actions = SearchActions()
        view = mock()

        presenter = SearchPresenter(SearchStates())
        presenter init view
    }

    @Test
    fun onActionNewQuery_renderSearchResult() {
        presenter reduce actions.query("search test")

        verify(view) render capture(stateCaptor)

        assertTrue(stateCaptor.value is SearchStates.SearchState.SearchResult)
    }

    @Test
    fun onActionEmptyQuery_noRender() {
        presenter reduce actions.query("")

        verifyZeroInteractions(view)
    }

    @Test
    fun onActionNullQuery_noRender() {
        presenter reduce actions.query(null)

        verifyZeroInteractions(view)
    }

}