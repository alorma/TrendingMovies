package com.alorma.domain.usecase

import com.alorma.domain.model.Movie
import com.alorma.domain.repository.MoviesRepository
import io.reactivex.Single

class ObtainMoviesUseCase(private val moviesRepository: MoviesRepository) {

    fun execute(): Single<List<Movie>> = moviesRepository.listAll()

    fun executeNextPage(): Single<List<Movie>> = moviesRepository.listNextPage()
}