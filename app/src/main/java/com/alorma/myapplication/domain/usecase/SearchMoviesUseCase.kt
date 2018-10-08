package com.alorma.myapplication.domain.usecase

import com.alorma.myapplication.domain.model.Movie
import com.alorma.myapplication.data.repository.MoviesRepository
import io.reactivex.Single

class SearchMoviesUseCase(private val moviesRepository: MoviesRepository) {

    fun execute(query: String): Single<List<Movie>> = moviesRepository.search(query)

    fun executeNextPage(query: String): Single<List<Movie>> = moviesRepository.searchNextPage(query)
}