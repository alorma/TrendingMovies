package com.alorma.presentation.movies

import com.alorma.domain.model.Movie
import com.alorma.domain.usecase.ObtainConfigurationUseCase
import com.alorma.domain.usecase.ObtainMoviesUseCase
import com.alorma.presentation.common.BaseViewModel
import com.alorma.presentation.common.Event
import com.alorma.presentation.common.ViewModelDispatchers

class MoviesViewModel(private val states: MoviesStates,
                      private val routes: MoviesRoutes,
                      private val obtainMoviesUseCase: ObtainMoviesUseCase,
                      private val obtainConfigurationUseCase: ObtainConfigurationUseCase,
                      dispatchers: ViewModelDispatchers
) :
        BaseViewModel<MoviesStates.MovieState,
                MoviesRoutes.MovieRoute,
                MoviesActions.MovieAction,
                Event>(dispatchers) {

    override fun reduce(action: MoviesActions.MovieAction) {
        when (action) {
            MoviesActions.MovieAction.Load -> load(action)
            MoviesActions.MovieAction.LoadPage -> load(action)
            is MoviesActions.MovieAction.OpenDetail -> onOpenDetail(action)
            MoviesActions.MovieAction.Search -> navigate(routes.search())
        }
    }

    private fun load(action: MoviesActions.MovieAction) {
        val error = object : ErrorHandler {
            override fun onError(exception: Throwable) {
                render(states loading false)
                render(states.error(exception))
            }
        }
        launch(error) {
            render(states loading true)
            val configuration = obtainConfigurationUseCase.execute()
            val movies = obtainLoadUseCase(action)
            val success = states.success(configuration, movies)
            render(states loading false)
            render(success)
        }
    }

    private suspend fun obtainLoadUseCase(action: MoviesActions.MovieAction): List<Movie> =
            when (action) {
                is MoviesActions.MovieAction.Load -> obtainMoviesUseCase.execute()
                is MoviesActions.MovieAction.LoadPage -> obtainMoviesUseCase.executeNextPage()
                else -> listOf()
            }

    private fun onOpenDetail(action: MoviesActions.MovieAction.OpenDetail) =
            navigate(routes.detail(action.id, action.title))
}