package com.alorma.myapplication.data.cache

import android.arch.paging.DataSource
import com.alorma.myapplication.domain.model.Movie
import javax.inject.Inject

class MoviesDataSource @Inject constructor(
        private val dao: MoviesDao,
        private val mapper: MoviesMapper) {

    fun save(items: List<Movie>) {
        dao.insert(items.map { mapper.map(it) })
    }

    fun get(): DataSource.Factory<Int, Movie> = dao.getMovies().map { mapper.mapEntity(it) }
}
