package com.alorma.domain.usecase

import com.alorma.domain.model.MovieDetail
import io.reactivex.Single

class LoadMovieDetailUseCase(
        private val obtainMovieDetailUseCase: ObtainMovieDetailUseCase,
        private val obtainConfigurationUseCase: ObtainConfigurationUseCase,
        private val obtainSimilarMoviesUseCase: ObtainSimilarMoviesUseCase
) {
    fun execute(id: Int): Single<MovieDetail> =
            Single.never()
    /*
            Single.zip(
            obtainConfigurationUseCase.execute(),
            obtainMovieDetailUseCase.execute(id),
            obtainSimilarMoviesUseCase.execute(id),
            Function3<Configuration, Movie, List<Movie>, MovieDetail> { config, movie, similar ->
                MovieDetail(config, movie, similar)
            })
            */
}