package com.alorma.myapplication.ui.search

import com.alorma.myapplication.configureRxThreading
import com.alorma.myapplication.domain.usecase.ObtainConfigurationUseCase
import com.alorma.myapplication.domain.usecase.SearchMoviesUseCase
import com.alorma.myapplication.ui.common.BaseView
import com.alorma.myapplication.ui.common.DateFormatter
import com.alorma.myapplication.ui.common.ResourcesProvider
import com.nhaarman.mockito_kotlin.*
import io.reactivex.Single
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Captor
import org.mockito.MockitoAnnotations

class SearchPresenterTest {

    private lateinit var actions: SearchActions
    private lateinit var presenter: SearchPresenter
    private lateinit var view: BaseView<SearchStates.SearchState>
    private lateinit var navigator: SearchNavigator

    @Captor
    private lateinit var stateCaptor: ArgumentCaptor<SearchStates.SearchState>

    @Captor
    private lateinit var routeCaptor: ArgumentCaptor<SearchRoutes.SearchRoute>

    private lateinit var moviesUseCase: SearchMoviesUseCase
    private lateinit var configUseCase: ObtainConfigurationUseCase

    init {
        configureRxThreading()
    }

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        actions = SearchActions()
        view = mock()
        navigator = mock()

        moviesUseCase = mock()
        configUseCase = mock()

        val resources: ResourcesProvider = mock<ResourcesProvider>().apply {
            given(getString(anyInt())).willReturn("")
        }

        val states = SearchStates(SearchMapper(DateFormatter(), resources))
        presenter = SearchPresenter(states, SearchRoutes(), navigator, moviesUseCase, configUseCase)
        presenter init view
    }

    @Test
    fun onActionNewQuery_withNoResults_renderEmpty() {
        given(moviesUseCase.execute(anyString())).willReturn(Single.just(listOf()))
        given(configUseCase.execute()).willReturn(Single.just(mock()))

        presenter reduce actions.query("search test")

        verify(view) render capture(stateCaptor)

        assertTrue(stateCaptor.value === SearchStates.SearchState.Empty)
    }

    @Test
    fun onActionPageQuery_withNoResults_renderEmptyPage() {
        given(moviesUseCase.execute(anyString())).willReturn(Single.just(listOf()))
        given(moviesUseCase.executeNextPage(anyString())).willReturn(Single.just(listOf()))
        given(configUseCase.execute()).willReturn(Single.just(mock()))

        presenter reduce actions.query("search test")
        presenter reduce actions.page()

        verify(view, times(2)) render capture(stateCaptor)

        assertTrue(stateCaptor.allValues[1] === SearchStates.SearchState.EmptyPage)
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

    @Test
    fun onActionOpenDetail_navigateToDetail() {
        presenter reduce actions.detail(getMovieSearchVM(12))

        verify(navigator) navigate capture(routeCaptor)

        assertTrue(routeCaptor.value is SearchRoutes.SearchRoute.OpenDetail)
    }


    @Test
    fun onActionBack_navigateToBack() {
        presenter reduce actions.back()

        verify(navigator) navigate capture(routeCaptor)

        assertTrue(routeCaptor.value === SearchRoutes.SearchRoute.Back)
    }

    private fun getMovieSearchVM(id: Int = 0): MovieSearchItemVM =
            MovieSearchItemVM(id, "", "", "", "", "")
}