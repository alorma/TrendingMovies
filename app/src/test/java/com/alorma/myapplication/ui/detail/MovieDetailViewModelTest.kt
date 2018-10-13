package com.alorma.myapplication.ui.detail

import assertk.all
import assertk.assert
import assertk.assertions.hasSize
import assertk.assertions.isInstanceOf
import com.alorma.data.cache.LocalMoviesDataSource
import com.alorma.data.net.DateParser
import com.alorma.data.net.MovieApi
import com.alorma.data.net.MovieDto
import com.alorma.data.net.NetworkMoviesDataSource
import com.alorma.data.repository.MoviesRepositoryImpl
import com.alorma.domain.model.Images
import com.alorma.domain.model.Movie
import com.alorma.domain.usecase.LoadMovieDetailUseCase
import com.alorma.domain.usecase.ObtainConfigurationUseCase
import com.alorma.domain.usecase.ObtainMovieDetailUseCase
import com.alorma.domain.usecase.ObtainSimilarMoviesUseCase
import com.alorma.myapplication.common.getResourcesProvider
import com.alorma.myapplication.ui.BaseViewModelTest
import com.alorma.presentation.common.DateFormatter
import com.alorma.presentation.common.Event
import com.alorma.presentation.common.EventHandler
import com.alorma.presentation.detail.*
import com.alorma.presentation.movies.MovieItemVM
import com.nhaarman.mockito_kotlin.*
import io.reactivex.Single
import org.junit.Test
import java.util.*
import com.alorma.data.net.MoviesMapper as NetworkMapper

class MovieDetailViewModelTest : BaseViewModelTest<DetailStates.DetailState,
        DetailRoutes.DetailRoute, DetailActions.DetailAction, Event>() {

    private lateinit var movieApi: MovieApi
    private lateinit var cacheDs: LocalMoviesDataSource
    private val actions: DetailActions = DetailActions()

    override fun createStateCaptor(): KArgumentCaptor<DetailStates.DetailState> = argumentCaptor()
    override fun createRouteCaptor(): KArgumentCaptor<DetailRoutes.DetailRoute> = argumentCaptor()
    override fun createEventCaptor(): KArgumentCaptor<EventHandler<Event>> = argumentCaptor()

    override fun createViewModel(): MovieDetailViewModel {
        movieApi = mock()

        val networkDs = NetworkMoviesDataSource(movieApi, NetworkMapper(DateParser()))
        cacheDs = mock()

        val moviesRepository = MoviesRepositoryImpl(networkDs, cacheDs)
        val movieDetailUseCase = ObtainMovieDetailUseCase(moviesRepository)
        val similarMoviesUseCase = ObtainSimilarMoviesUseCase(moviesRepository)

        val configUseCase = mock<ObtainConfigurationUseCase>().apply {
            //given(execute()).willReturn(createC)
        }

        /*
        val configUseCase = mock<ObtainConfigurationUseCase>().apply {
            given(execute()).willReturn(Single.just(mock()))
        }
        */

        val loadDetailUseCase = LoadMovieDetailUseCase(movieDetailUseCase, configUseCase, similarMoviesUseCase)

        val resources = getResourcesProvider()
        val mapper = DetailMapper(resources, DateFormatter())

        return MovieDetailViewModel(DetailStates(mapper), DetailRoutes(),
                loadDetailUseCase,
                configUseCase,
                similarMoviesUseCase)
    }

    @Test
    fun onActionBack_navigateBack() {
        captureRoute { actions.back() }

        assert(routeCaptor.firstValue).isInstanceOf(DetailRoutes.DetailRoute.Back::class)
    }

    @Test
    fun onActionLoad_serveFromCache_renderSuccess() {
        given(cacheDs.get(eq(12))).willReturn(getMovie(12))
        given(cacheDs.getSimilar(eq(12))).willReturn(getMovies(5))

        captureState { actions.load(12) }

        assert(stateCaptor.firstValue).all {
            assert(actual).isInstanceOf(DetailStates.DetailState.Success::class.java)
            (actual as? DetailStates.DetailState.Success)?.let {
                assert(it.similarMovies).hasSize(5)
            }
        }
    }

    @Test
    fun onActionLoad_serveFromApi_renderSuccess() {
        given(cacheDs.get(eq(12))).willReturn(null)
        given(movieApi.item(eq(12))).willReturn(Single.just(generateMovieDto(12)))
        given(cacheDs.getSimilar(eq(12))).willReturn(getMovies(5))

        captureState { actions.load(12) }

        assert(stateCaptor.firstValue).isInstanceOf(DetailStates.DetailState.Success::class.java)
    }

    @Test
    fun onActionLoadError_renderError() {
        given(cacheDs.get(eq(12))).willThrow(RuntimeException())
        given(cacheDs.getSimilar(eq(12))).willReturn(getMovies(5))

        captureState { actions.load(12) }

        assert(stateCaptor.firstValue).isInstanceOf(DetailStates.DetailState.Error::class.java)
    }

    @Test
    fun onActionOpenDetail_navigateToDetail() {
        captureRoute { actions.openSimilarMovie(getMovieVM()) }

        assert(routeCaptor.firstValue).isInstanceOf(DetailRoutes.DetailRoute.Detail::class)
    }

    private fun generateMovieDto(id: Int = 0): MovieDto = MovieDto(id, "", "", "2017-04-10", "", "", 0f, listOf())

    private fun getMovies(number: Int): List<Movie> = (1..number).map { getMovie(it) }
    private fun getMovie(id: Int = 0): Movie = Movie(id, "", "", Images("", ""), Date(), 0f, listOf())
    private fun getMovieVM(id: Int = 0): MovieItemVM = MovieItemVM(id, "", "", "5.4")
}