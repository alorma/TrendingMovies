package com.alorma.myapplication.ui.detail

import assertk.assert
import assertk.assertions.isInstanceOf
import com.alorma.domain.exception.DataOriginException
import com.alorma.domain.model.Configuration
import com.alorma.domain.model.Images
import com.alorma.domain.model.Movie
import com.alorma.domain.repository.ConfigurationRepository
import com.alorma.domain.repository.MoviesRepository
import com.alorma.domain.usecase.LoadMovieDetailUseCase
import com.alorma.domain.usecase.ObtainConfigurationUseCase
import com.alorma.domain.usecase.ObtainMovieDetailUseCase
import com.alorma.domain.usecase.ObtainSimilarMoviesUseCase
import com.alorma.myapplication.common.TestViewModelDispatchers
import com.alorma.myapplication.common.getResourcesProvider
import com.alorma.myapplication.ui.BaseViewModelTest
import com.alorma.presentation.common.DateFormatter
import com.alorma.presentation.common.Event
import com.alorma.presentation.common.EventHandler
import com.alorma.presentation.detail.*
import com.alorma.presentation.movies.MovieItemVM
import com.nhaarman.mockito_kotlin.*
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Test
import java.util.*
import com.alorma.data.net.MoviesMapper as NetworkMapper

class MovieDetailViewModelTest : BaseViewModelTest<DetailStates.DetailState,
        DetailRoutes.DetailRoute, DetailActions.DetailAction, Event>() {

    private var moviesRepository: MoviesRepository = mock()
    private var configRepository: ConfigurationRepository = mock()
    private val actions: DetailActions = DetailActions()

    override fun createStateCaptor(): KArgumentCaptor<DetailStates.DetailState> = argumentCaptor()
    override fun createRouteCaptor(): KArgumentCaptor<DetailRoutes.DetailRoute> = argumentCaptor()
    override fun createEventCaptor(): KArgumentCaptor<EventHandler<Event>> = argumentCaptor()


    override fun createViewModel(dispatchers: TestViewModelDispatchers): MovieDetailViewModel {
        val movieDetailUseCase = ObtainMovieDetailUseCase(moviesRepository)
        val similarMoviesUseCase = ObtainSimilarMoviesUseCase(moviesRepository)
        val obtainConfigurationUseCase = ObtainConfigurationUseCase(configRepository)

        val loadDetailUseCase = LoadMovieDetailUseCase(
                movieDetailUseCase,
                obtainConfigurationUseCase, similarMoviesUseCase
        )

        val resources = getResourcesProvider()
        val mapper = DetailMapper(resources, DateFormatter())

        return MovieDetailViewModel(
                DetailStates(mapper),
                DetailRoutes(),
                loadDetailUseCase,
                obtainConfigurationUseCase,
                similarMoviesUseCase,
                dispatchers
        )
    }

    @Test
    fun onActionBack_navigateBack() {
        captureRoute { actions.back() }

        assert(routeCaptor.firstValue).isInstanceOf(DetailRoutes.DetailRoute.Back::class)
    }

    @Test
    fun onActionLoad_repositorySuccess_renderSuccess() {
        runBlocking {
            given(configRepository.getConfig()).willReturn(getConfig())
            given(moviesRepository.getMovie(eq(12))).willReturn(getMovie(12))
            given(moviesRepository.similar(eq(12))).willReturn(getMovies(2))
        }

        captureState { actions.load(12) }

        assert(stateCaptor.firstValue).isInstanceOf(DetailStates.DetailState.Success::class.java)
    }

    @Test
    fun onActionLoadError_renderError() {
        runBlocking {
            doAnswer { throw DataOriginException() }.whenever(configRepository).getConfig()
        }

        captureState { actions.load(12) }

        assert(stateCaptor.firstValue).isInstanceOf(DetailStates.DetailState.Error::class.java)
    }

    @Test
    fun onActionOpenDetail_navigateToDetail() {
        captureRoute { actions.openSimilarMovie(getMovieVM()) }

        assert(routeCaptor.firstValue).isInstanceOf(DetailRoutes.DetailRoute.Detail::class)
    }

    private fun getConfig(): Configuration = Configuration("", "", "", listOf())
    private fun getMovies(number: Int): List<Movie> = (1..number).map { getMovie(it) }
    private fun getMovie(id: Int = 0): Movie = Movie(id, "", "", Images("", ""), Date(), 0f, listOf())
    private fun getMovieVM(id: Int = 0): MovieItemVM = MovieItemVM(id, "", "", "5.4")
}