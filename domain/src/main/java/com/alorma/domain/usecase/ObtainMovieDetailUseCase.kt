package com.alorma.domain.usecase

import com.alorma.domain.model.Movie
import com.alorma.domain.repository.MoviesRepository

class ObtainMovieDetailUseCase(private val moviesRepository: MoviesRepository) {
    suspend fun execute(id: Int): Movie = moviesRepository.getMovie(id)
}