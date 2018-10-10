package com.alorma.domain.usecase

import com.alorma.domain.model.Movie
import com.alorma.domain.repository.MoviesRepository
import io.reactivex.Single

class ObtainMovieUseCase(private val moviesRepository: MoviesRepository) {

    fun execute(id: Int): Single<List<Movie>> = moviesRepository.similar(id)

    fun executeNextPage(id: Int): Single<List<Movie>> = moviesRepository.similarPage(id)
}