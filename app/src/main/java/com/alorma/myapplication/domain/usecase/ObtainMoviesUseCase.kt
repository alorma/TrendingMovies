package com.alorma.myapplication.domain.usecase

import com.alorma.myapplication.domain.model.Movie
import com.alorma.myapplication.domain.repository.MoviesRepository
import io.reactivex.Single
import javax.inject.Inject

class ObtainMoviesUseCase @Inject constructor(
        private val moviesRepository: MoviesRepository) {

    fun execute(): Single<List<Movie>> = moviesRepository.listAll()

    fun executeNextPage(): Single<List<Movie>> = moviesRepository.listNextPage()
}