package com.alorma.domain.usecase

import com.alorma.domain.model.Movie
import com.alorma.domain.repository.MoviesRepository

class SearchMoviesUseCase(private val moviesRepository: MoviesRepository) {

    suspend fun execute(query: String): List<Movie> = moviesRepository.search(query)

    suspend fun executeNextPage(query: String): List<Movie> = moviesRepository.searchNextPage(query)
}