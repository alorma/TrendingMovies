package com.alorma.myapplication.ui.search

import assertk.assert
import assertk.assertions.isEqualTo
import com.alorma.myapplication.common.getResourcesProvider
import com.alorma.myapplication.domain.usecase.ObtainConfigurationUseCase
import com.alorma.myapplication.domain.usecase.SearchMoviesUseCase
import com.alorma.myapplication.ui.BaseViewModelTest
import com.alorma.myapplication.ui.common.*
import com.nhaarman.mockito_kotlin.KArgumentCaptor
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.mock
import io.reactivex.Single
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString

class SearchViewModelTest :
        BaseViewModelTest<SearchStates.SearchState, SearchRoutes.SearchRoute, SearchActions.SearchAction, Event>() {

    private lateinit var moviesUseCase: SearchMoviesUseCase
    private lateinit var configUseCase: ObtainConfigurationUseCase
    private lateinit var actions: SearchActions

    override fun createStateCaptor(): KArgumentCaptor<SearchStates.SearchState> = argumentCaptor()
    override fun createEventCaptor(): KArgumentCaptor<EventHandler<Event>> = argumentCaptor()
    override fun createRouteCaptor(): KArgumentCaptor<SearchRoutes.SearchRoute> = argumentCaptor()
    override fun createViewModel(navigator: Navigator<SearchRoutes.SearchRoute>): BaseViewModel<SearchStates.SearchState, SearchRoutes.SearchRoute, SearchActions.SearchAction, Event> {
        actions = SearchActions()

        moviesUseCase = mock()
        configUseCase = mock()

        val resources = getResourcesProvider()

        val states = SearchStates(SearchMapper(DateFormatter(), resources))
        return SearchViewModel(states, SearchRoutes(), navigator, moviesUseCase, configUseCase)
    }

    @Test
    fun onActionNewQuery_withNoResults_renderEmpty() {
        given(moviesUseCase.execute(anyString())).willReturn(Single.just(listOf()))
        given(configUseCase.execute()).willReturn(Single.just(mock()))

        captureState { actions.query("search test") }

        assert(stateCaptor.firstValue).isEqualTo(SearchStates.SearchState.Empty)
    }

    @Test
    fun onActionPageQuery_withNoResults_renderEmptyPage() {
        given(moviesUseCase.execute(anyString())).willReturn(Single.just(listOf()))
        given(moviesUseCase.executeNextPage(anyString())).willReturn(Single.just(listOf()))
        given(configUseCase.execute()).willReturn(Single.just(mock()))

        captureState { actions.query("search test") }
        captureState(2) { actions.page() }

        assert(stateCaptor.thirdValue).isEqualTo(SearchStates.SearchState.EmptyPage)
    }

    /*
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
*/
}