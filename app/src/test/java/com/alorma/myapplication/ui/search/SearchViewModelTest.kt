package com.alorma.myapplication.ui.search

import assertk.assert
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import com.alorma.myapplication.common.getResourcesProvider
import com.alorma.domain.usecase.ObtainConfigurationUseCase
import com.alorma.domain.usecase.SearchMoviesUseCase
import com.alorma.myapplication.ui.BaseViewModelTest
import com.alorma.myapplication.ui.common.BaseViewModel
import com.alorma.myapplication.ui.common.DateFormatter
import com.alorma.myapplication.ui.common.Event
import com.alorma.myapplication.ui.common.EventHandler
import com.nhaarman.mockito_kotlin.*
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
    override fun createViewModel(): BaseViewModel<SearchStates.SearchState, SearchRoutes.SearchRoute, SearchActions.SearchAction, Event> {
        actions = SearchActions()

        moviesUseCase = mock()
        configUseCase = mock()

        val resources = getResourcesProvider()

        val states = SearchStates(SearchMapper(DateFormatter(), resources))
        return SearchViewModel(states, SearchRoutes(), moviesUseCase, configUseCase)
    }

    @Test
    fun onActionNewQuery_withNoResults_renderEmpty() {
        given(moviesUseCase.execute(anyString())).willReturn(Single.just(listOf()))
        given(configUseCase.execute()).willReturn(Single.just(mock()))

        captureState(3) { actions.query("search test") }

        assert(stateCaptor.thirdValue).isEqualTo(SearchStates.SearchState.Empty)
    }

    @Test
    fun onActionPageQuery_withNoResults_renderEmptyPage() {
        given(moviesUseCase.execute(anyString())).willReturn(Single.just(listOf()))
        given(moviesUseCase.executeNextPage(anyString())).willReturn(Single.just(listOf()))
        given(configUseCase.execute()).willReturn(Single.just(mock()))

        runAction(actions.query("search test"))
        captureState(2) { actions.page() }

        assert(stateCaptor.secondValue).isEqualTo(SearchStates.SearchState.EmptyPage)
    }

    @Test
    fun onActionEmptyQuery_noRender() {
        runAction(actions.query(""))

        verifyZeroInteractions(stateObserver)
    }

    @Test
    fun onActionNullQuery_noRender() {
        runAction(actions.query(null))

        verifyZeroInteractions(stateObserver)
    }

    @Test
    fun onActionOpenDetail_navigateToDetail() {
        captureRoute { actions.detail(getMovieSearchVM(12)) }

        assert(routeCaptor.firstValue).isInstanceOf(SearchRoutes.SearchRoute.OpenDetail::class.java)
    }

    @Test
    fun onActionBack_navigateToBack() {
        captureRoute { actions.back() }

        assert(routeCaptor.firstValue).isEqualTo(SearchRoutes.SearchRoute.Back)
    }

    private fun getMovieSearchVM(id: Int = 0): MovieSearchItemVM =
            MovieSearchItemVM(id, "", "", "", "", "")
}