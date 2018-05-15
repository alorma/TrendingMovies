package com.alorma.myapplication.domain.usecase

import com.alorma.myapplication.domain.model.Movie
import com.alorma.myapplication.domain.repository.MoviesRepository
import io.reactivex.Maybe
import javax.inject.Inject

class ObtainMovieDetailUseCase @Inject constructor(private val moviesRepository: MoviesRepository) {
    fun execute(id: Int): Maybe<Movie> = moviesRepository.getMovie(id)
}