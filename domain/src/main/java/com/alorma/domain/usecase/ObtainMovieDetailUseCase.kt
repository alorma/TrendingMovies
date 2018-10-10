package com.alorma.domain.usecase

import com.alorma.domain.model.Movie
import com.alorma.domain.repository.MoviesRepository
import io.reactivex.Single

class ObtainMovieDetailUseCase(private val moviesRepository: MoviesRepository) {
    fun execute(id: Int): Single<Movie> = moviesRepository.getMovie(id)
}