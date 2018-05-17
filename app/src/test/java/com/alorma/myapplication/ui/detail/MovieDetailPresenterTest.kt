package com.alorma.myapplication.ui.detail

import com.alorma.myapplication.configureRxThreading
import com.alorma.myapplication.data.net.DateParser
import com.alorma.myapplication.data.net.PagedResponse
import com.alorma.myapplication.data.net.MovieApi
import com.alorma.myapplication.data.net.MovieDto
import com.alorma.myapplication.domain.model.Images
import com.alorma.myapplication.domain.model.Movie
import com.alorma.myapplication.domain.repository.MoviesRepository
import com.alorma.myapplication.domain.usecase.ObtainConfigurationUseCase
import com.alorma.myapplication.domain.usecase.ObtainMovieDetailUseCase
import com.alorma.myapplication.domain.usecase.ObtainMovieUseCase
import com.alorma.myapplication.ui.common.BaseView
import com.alorma.myapplication.ui.common.DateFormatter
import com.alorma.myapplication.ui.common.ResourcesProvider
import com.alorma.myapplication.ui.movies.MovieItemVM
import com.nhaarman.mockito_kotlin.*
import io.reactivex.Single
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers
import org.mockito.Captor
import org.mockito.MockitoAnnotations
import java.util.*
import com.alorma.myapplication.data.cache.MoviesDataSource as Cache
import com.alorma.myapplication.data.net.MoviesDataSource as Network
import com.alorma.myapplication.data.net.MoviesMapper as NetworkMapper

class MovieDetailPresenterTest {

    init {
        configureRxThreading()
    }

    private lateinit var movieApi: MovieApi
    private lateinit var cacheDs: Cache
    private lateinit var navigator: DetailNavigator
    private lateinit var viewModel: MovieDetailViewModel
    private val actions: DetailActions = DetailActions()

    @Captor
    private lateinit var stateCaptor: ArgumentCaptor<DetailStates.DetailState>
    @Captor
    private lateinit var routeCaptor: ArgumentCaptor<DetailRoutes.DetailRoute>

    private lateinit var view: BaseView<DetailStates.DetailState>

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        navigator = mock()
        movieApi = mock()
        view = mock()

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
        viewModel init view
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

        viewModel reduce actions.load(12)

        verify(view, times(2)).render(capture(stateCaptor))

        assertTrue(stateCaptor.allValues[0] is DetailStates.DetailState.Success)
        assertTrue(stateCaptor.allValues[1] is DetailStates.DetailState.ErrorSimilarMovies)
    }

    @Test
    fun onActionLoad_serveFromApi_renderSuccess() {
        given(cacheDs.get(eq(12))).willReturn(null)
        given(movieApi.item(eq(12))).willReturn(Single.just(generateMovieDto(12)))

        viewModel reduce actions.load(12)

        verify(view, times(2)).render(capture(stateCaptor))

        assertTrue(stateCaptor.allValues[0] is DetailStates.DetailState.Success)
        assertTrue(stateCaptor.allValues[1] is DetailStates.DetailState.ErrorSimilarMovies)
    }

    @Test
    fun onActionLoadError_renderError() {
        given(cacheDs.get(eq(12))).willThrow(RuntimeException())

        viewModel reduce actions.load(12)

        verify(view, times((2))).render(capture(stateCaptor))

        assertTrue(stateCaptor.allValues[0] is DetailStates.DetailState.Error)
        assertTrue(stateCaptor.allValues[1] is DetailStates.DetailState.ErrorSimilarMovies)
    }

    @Test
    fun onActionOpenDetail_navigateToDetail() {
        viewModel reduce actions.openSimilarMovie(getMovieVM())

        verify(navigator).navigate(capture(routeCaptor))
        assertTrue(routeCaptor.value is DetailRoutes.DetailRoute.Detail)
    }

    @Test
    fun onLoad_renderSimilarMovies() {
        given(cacheDs.get(eq(12))).willReturn(getMovie(12))
        val response = PagedResponse(0, 0,
                listOf(generateMovieDto(), generateMovieDto(), generateMovieDto()))
        given(movieApi.similar(eq(12))).willReturn(Single.just(response))

        viewModel reduce actions.load(12)

        verify(view, times(2)).render(capture(stateCaptor))

        assertTrue(stateCaptor.allValues[0] is DetailStates.DetailState.Success)
        assertTrue(stateCaptor.allValues[1] is DetailStates.DetailState.SimilarMovies)
    }

    private fun generateMovieDto(id: Int = 0): MovieDto = MovieDto(id, "", "", "2017-04-10", "", "", 0f, listOf())
    private fun getMovie(id: Int = 0): Movie = Movie(id, "", "", Images("", ""), Date(), 0f, listOf())
    private fun getMovieVM(id: Int = 0): MovieItemVM = MovieItemVM(id, "", "", "5.4")
}