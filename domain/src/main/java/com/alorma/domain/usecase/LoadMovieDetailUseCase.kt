package com.alorma.domain.usecase

import com.alorma.domain.model.MovieDetail

class LoadMovieDetailUseCase(
        private val obtainMovieDetailUseCase: ObtainMovieDetailUseCase,
        private val obtainConfigurationUseCase: ObtainConfigurationUseCase,
        private val obtainSimilarMoviesUseCase: ObtainSimilarMoviesUseCase
) {
    suspend fun execute(id: Int): MovieDetail =
            MovieDetail(
                    obtainConfigurationUseCase.execute(),
                    obtainMovieDetailUseCase.execute(id),
                    obtainSimilarMoviesUseCase.execute(id)
            )
}