package com.alorma.myapplication.domain.usecase

import android.arch.paging.PagedList
import com.alorma.myapplication.domain.model.Movie
import com.alorma.myapplication.domain.repository.MoviesRepository
import io.reactivex.Observable
import javax.inject.Inject

class ObtainMoviesUseCase @Inject constructor(
        private val moviesRepository: MoviesRepository) {

    fun execute(): Observable<PagedList<Movie>> = moviesRepository.listAll()
}