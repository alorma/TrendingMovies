package com.alorma.domain.usecase

import com.alorma.domain.model.Configuration
import com.alorma.domain.model.Movie
import com.alorma.domain.model.MovieDetail
import io.reactivex.Single
import io.reactivex.functions.Function3

class LoadMovieDetailUseCase(
        private val obtainMovieDetailUseCase: ObtainMovieDetailUseCase,
        private val obtainConfigurationUseCase: ObtainConfigurationUseCase,
        private val obtainSimilarMoviesUseCase: ObtainSimilarMoviesUseCase
) {
    fun execute(id: Int): Single<MovieDetail> = Single.zip(
            obtainConfigurationUseCase.execute(),
            obtainMovieDetailUseCase.execute(id),
            obtainSimilarMoviesUseCase.execute(id),
            Function3<Configuration, Movie, List<Movie>, MovieDetail> { config, movie, similar ->
                MovieDetail(config, movie, similar)
            })
}