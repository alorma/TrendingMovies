package com.alorma.data.net

import com.alorma.domain.exception.DataOriginException
import com.alorma.domain.model.Movie
import com.alorma.domain.model.MovieList
import kotlinx.coroutines.experimental.Deferred

class NetworkMoviesDataSource(private val movieApi: MovieApi,
                              private val moviesMapper: MoviesMapper) {

    suspend fun listAll(page: Int? = null): MovieList {
        val deferred = page?.let {
            movieApi.listPage(it)
        } ?: movieApi.listAll()

        return loadMovies(deferred)
    }

    suspend fun search(query: String, page: Int? = null): MovieList {
        val deferred = page?.let {
            movieApi.searchPage(query, it)
        } ?: movieApi.search(query)

        return loadMovies(deferred)
    }

    suspend fun similar(id: Int, page: Int? = null): MovieList {
        val deferred = page?.let {
            movieApi.similarPage(id, it)
        } ?: movieApi.similar(id)

        return loadMovies(deferred)
    }

    private suspend fun loadMovies(deferred: Deferred<PagedResponse<MovieDto>>): MovieList {
        try {
            val moviesDto = deferred.await()

            return MovieList(
                    moviesDto.page,
                    moviesDto.totalPages,
                    moviesMapper.map(moviesDto.results)
            )
        } catch (e: Exception) {
            throw DataOriginException()
        }
    }

    suspend fun item(id: Int): Movie =
            try {
                movieApi.item(id)
                        .await()
                        .let { moviesMapper.mapItem(it) }
            } catch (e: Exception) {
                throw DataOriginException()
            }
}