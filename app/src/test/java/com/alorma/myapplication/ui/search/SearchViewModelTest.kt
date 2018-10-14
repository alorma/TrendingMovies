package com.alorma.myapplication.ui.search

import assertk.assert
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import com.alorma.domain.repository.ConfigurationRepository
import com.alorma.domain.repository.MoviesRepository
import com.alorma.domain.usecase.ObtainConfigurationUseCase
import com.alorma.domain.usecase.SearchMoviesUseCase
import com.alorma.myapplication.common.getConfig
import com.alorma.myapplication.common.getMovieSearchVM
import com.alorma.myapplication.common.getResourcesProvider
import com.alorma.myapplication.ui.BaseViewModelTest
import com.alorma.presentation.common.*
import com.alorma.presentation.search.*
import com.nhaarman.mockito_kotlin.*
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Test

class SearchViewModelTest :
        BaseViewModelTest<SearchStates.SearchState,
                SearchRoutes.SearchRoute,
                SearchActions.SearchAction,
                Event>() {

    private var moviesRepository: MoviesRepository = mock()
    private var configRepository: ConfigurationRepository = mock()

    private lateinit var actions: SearchActions

    override fun createStateCaptor(): KArgumentCaptor<SearchStates.SearchState> = argumentCaptor()
    override fun createEventCaptor(): KArgumentCaptor<EventHandler<Event>> = argumentCaptor()
    override fun createRouteCaptor(): KArgumentCaptor<SearchRoutes.SearchRoute> = argumentCaptor()

    override fun createViewModel(dispatchers: ViewModelDispatchers): BaseViewModel<SearchStates.SearchState, SearchRoutes.SearchRoute, SearchActions.SearchAction, Event> {
        actions = SearchActions()

        val moviesUseCase = SearchMoviesUseCase(moviesRepository)
        val configUseCase = ObtainConfigurationUseCase(configRepository)

        val resources = getResourcesProvider()

        val states = SearchStates(SearchMapper(DateFormatter(), resources))
        return SearchViewModel(states, SearchRoutes(), moviesUseCase, configUseCase, dispatchers)
    }

    override fun setup() {
        super.setup()
        runBlocking {
            given(configRepository.getConfig()).willReturn(getConfig())
        }
    }

    @Test
    fun onActionNewQuery_withNoResults_renderEmpty() {
        runBlocking {
            given(moviesRepository.search(any())).willReturn(listOf())
        }

        captureState(3) { actions.query("search test") }

        assert(stateCaptor.thirdValue).isEqualTo(SearchStates.SearchState.Empty)
    }

    @Test
    fun onActionPageQuery_withNoResults_renderEmptyPage() {
        runBlocking {
            given(moviesRepository.search(any())).willReturn(listOf())
            given(moviesRepository.searchNextPage(any())).willReturn(listOf())
        }

        runAction(actions.query("search test"))
        captureState(4) { actions.page() }

        stateCaptor.allValues.forEach { System.out.println(it) }

        assert(stateCaptor.allValues[3]).isEqualTo(SearchStates.SearchState.EmptyPage)
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
}