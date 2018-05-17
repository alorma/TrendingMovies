package com.alorma.myapplication.ui.detail

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.LiveData
import com.alorma.myapplication.configureRxThreading
import com.alorma.myapplication.data.net.DateParser
import com.alorma.myapplication.data.net.MovieApi
import com.alorma.myapplication.data.net.MovieDto
import com.alorma.myapplication.domain.model.Images
import com.alorma.myapplication.domain.model.Movie
import com.alorma.myapplication.domain.repository.MoviesRepository
import com.alorma.myapplication.domain.usecase.ObtainConfigurationUseCase
import com.alorma.myapplication.domain.usecase.ObtainMovieDetailUseCase
import com.alorma.myapplication.domain.usecase.ObtainMovieUseCase
import com.alorma.myapplication.ui.common.DateFormatter
import com.alorma.myapplication.ui.common.ResourcesProvider
import com.alorma.myapplication.ui.movies.MovieItemVM
import com.nhaarman.mockito_kotlin.*
import io.reactivex.Single
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers
import org.mockito.Captor
import org.mockito.MockitoAnnotations
import java.util.*
import com.alorma.myapplication.data.cache.MoviesDataSource as Cache
import com.alorma.myapplication.data.net.MoviesDataSource as Network
import com.alorma.myapplication.data.net.MoviesMapper as NetworkMapper

class MovieDetailPresenterTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    init {
        configureRxThreading()
    }

    private lateinit var movieApi: MovieApi
    private lateinit var cacheDs: Cache
    private lateinit var navigator: DetailNavigator
    private lateinit var viewModel: MovieDetailViewModel
    private val actions: DetailActions = DetailActions()

    @Captor
    private lateinit var routeCaptor: ArgumentCaptor<DetailRoutes.DetailRoute>

    private lateinit var liveData: LiveData<DetailStates.DetailState>

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        navigator = mock()
        movieApi = mock()

        val networkDs = Network(movieApi, NetworkMapper(DateParser()))
        cacheDs = mock()

        val moviesRepository = MoviesRepository(networkDs, cacheDs)
        val movieDetailUseCase = ObtainMovieDetailUseCase(moviesRepository)
        val similarMoviesUseCase = ObtainMovieUseCase(moviesRepository)
        val configUseCase = mock<ObtainConfigurationUseCase>().apply {
            given(execute()).willReturn(Single.just(mock()))
        }

        val resources = mock<ResourcesProvider>().apply {
            given(getString(ArgumentMatchers.anyInt())).willReturn("")
        }

        val mapper = DetailMapper(resources, DateFormatter())

        viewModel = MovieDetailViewModel(DetailStates(mapper), DetailRoutes(), navigator,
                movieDetailUseCase,
                configUseCase,
                similarMoviesUseCase)

        liveData = viewModel.init()
    }

    @Test
    fun onActionBack_navigateBack() {
        viewModel reduce actions.back()

        verify(navigator) navigate capture(routeCaptor)
        assertTrue(routeCaptor.value === DetailRoutes.DetailRoute.Back)
    }

    @Test
    fun onActionLoad_serveFromCache_renderSuccess() {
        given(cacheDs.get(eq(12))).willReturn(getMovie(12))
        given(cacheDs.getSimilar(eq(12))).willReturn(getMovies(5))

        viewModel reduce actions.load(12)

        assertTrue(liveData.value is DetailStates.DetailState.Success)

        with(liveData.value as DetailStates.DetailState.Success) {
            assertEquals(5, similarMovies.size)
        }
    }

    @Test
    fun onActionLoad_serveFromApi_renderSuccess() {
        given(cacheDs.get(eq(12))).willReturn(null)
        given(movieApi.item(eq(12))).willReturn(Single.just(generateMovieDto(12)))
        given(cacheDs.getSimilar(eq(12))).willReturn(getMovies(5))

        viewModel reduce actions.load(12)

        assertTrue(liveData.value is DetailStates.DetailState.Success)
    }

    @Test
    fun onActionLoadError_renderError() {
        given(cacheDs.get(eq(12))).willThrow(RuntimeException())
        given(cacheDs.getSimilar(eq(12))).willReturn(getMovies(5))

        viewModel reduce actions.load(12)

        assertTrue(liveData.value is DetailStates.DetailState.Error)
    }

    @Test
    fun onActionOpenDetail_navigateToDetail() {
        viewModel reduce actions.openSimilarMovie(getMovieVM())

        verify(navigator).navigate(capture(routeCaptor))
        assertTrue(routeCaptor.value is DetailRoutes.DetailRoute.Detail)
    }

    private fun generateMovieDto(id: Int = 0): MovieDto = MovieDto(id, "", "", "2017-04-10", "", "", 0f, listOf())

    private fun getMovies(number: Int): List<Movie> = (1..number).map { getMovie(it) }
    private fun getMovie(id: Int = 0): Movie = Movie(id, "", "", Images("", ""), Date(), 0f, listOf())
    private fun getMovieVM(id: Int = 0): MovieItemVM = MovieItemVM(id, "", "", "5.4")
}