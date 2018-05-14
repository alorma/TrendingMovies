package com.alorma.myapplication.domain.usecase

import com.alorma.myapplication.domain.model.Movie
import com.alorma.myapplication.domain.repository.MoviesRepository
import io.reactivex.Single
import javax.inject.Inject

class SearchMoviesUseCase @Inject constructor(
        private val moviesRepository: MoviesRepository) {

    fun execute(query: String): Single<List<Movie>> = moviesRepository.listAll()

    fun executeNextPage(query: String): Single<List<Movie>> = moviesRepository.listNextPage()
}