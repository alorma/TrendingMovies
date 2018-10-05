package com.alorma.myapplication.domain.usecase

import com.alorma.myapplication.domain.model.Movie
import com.alorma.myapplication.domain.repository.MoviesRepository
import io.reactivex.Single

class ObtainMovieDetailUseCase(private val moviesRepository: MoviesRepository) {
    fun execute(id: Int): Single<Movie> = moviesRepository.getMovie(id)
}