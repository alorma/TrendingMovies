package com.alorma.domain.usecase

import com.alorma.domain.model.Movie
import com.alorma.domain.repository.MoviesRepository

class ObtainMoviesUseCase(private val moviesRepository: MoviesRepository) {

    suspend fun execute(): List<Movie> = moviesRepository.listAll()

    suspend fun executeNextPage(): List<Movie> = moviesRepository.listNextPage()
}