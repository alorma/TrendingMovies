package com.alorma.domain.usecase

import com.alorma.domain.model.Movie
import com.alorma.domain.repository.MoviesRepository

class ObtainSimilarMoviesUseCase(private val moviesRepository: MoviesRepository) {

    suspend fun execute(id: Int): List<Movie> = moviesRepository.similar(id)

    suspend fun executeNextPage(id: Int): List<Movie> = moviesRepository.similarPage(id)
}