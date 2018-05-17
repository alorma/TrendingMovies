package com.alorma.myapplication.ui.search

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import com.alorma.myapplication.configureRxThreading
import com.alorma.myapplication.domain.usecase.ObtainConfigurationUseCase
import com.alorma.myapplication.domain.usecase.SearchMoviesUseCase
import com.alorma.myapplication.ui.common.DateFormatter
import com.alorma.myapplication.ui.common.ResourcesProvider
import com.nhaarman.mockito_kotlin.*
import io.reactivex.Single
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class SearchViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var actions: SearchActions
    private lateinit var viewModel: SearchViewModel
    private lateinit var liveData: LiveData<SearchStates.SearchState>
    private lateinit var navigator: SearchNavigator

    @Mock
    lateinit var observer: Observer<SearchStates.SearchState>

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

        navigator = mock()

        moviesUseCase = mock()
        configUseCase = mock()

        val resources: ResourcesProvider = mock<ResourcesProvider>().apply {
            given(getString(anyInt())).willReturn("")
        }

        val states = SearchStates(SearchMapper(DateFormatter(), resources))
        viewModel = SearchViewModel(states, SearchRoutes(), navigator, moviesUseCase, configUseCase)

        liveData = viewModel.liveData
        liveData.observeForever(observer)
    }

    @Test
    fun onActionNewQuery_withNoResults_renderEmpty() {
        given(moviesUseCase.execute(anyString())).willReturn(Single.just(listOf()))
        given(configUseCase.execute()).willReturn(Single.just(mock()))

        viewModel reduce actions.query("search test")

        verify(observer).onChanged(capture(stateCaptor))

        assertTrue(stateCaptor.value === SearchStates.SearchState.Empty)
    }

    @Test
    fun onActionPageQuery_withNoResults_renderEmptyPage() {
        given(moviesUseCase.execute(anyString())).willReturn(Single.just(listOf()))
        given(moviesUseCase.executeNextPage(anyString())).willReturn(Single.just(listOf()))
        given(configUseCase.execute()).willReturn(Single.just(mock()))

        viewModel reduce actions.query("search test")
        viewModel reduce actions.page()

        verify(observer, times(2)).onChanged(capture(stateCaptor))

        assertTrue(stateCaptor.allValues[1] === SearchStates.SearchState.EmptyPage)
    }

    @Test
    fun onActionEmptyQuery_noRender() {
        viewModel reduce actions.query("")

        verifyZeroInteractions(observer)
    }

    @Test
    fun onActionNullQuery_noRender() {
        viewModel reduce actions.query(null)

        verifyZeroInteractions(observer)
    }

    @Test
    fun onActionOpenDetail_navigateToDetail() {
        viewModel reduce actions.detail(getMovieSearchVM(12))

        verify(navigator) navigate capture(routeCaptor)

        assertTrue(routeCaptor.value is SearchRoutes.SearchRoute.OpenDetail)
    }


    @Test
    fun onActionBack_navigateToBack() {
        viewModel reduce actions.back()

        verify(navigator) navigate capture(routeCaptor)

        assertTrue(routeCaptor.value === SearchRoutes.SearchRoute.Back)
    }

    private fun getMovieSearchVM(id: Int = 0): MovieSearchItemVM =
            MovieSearchItemVM(id, "", "", "", "", "")
}