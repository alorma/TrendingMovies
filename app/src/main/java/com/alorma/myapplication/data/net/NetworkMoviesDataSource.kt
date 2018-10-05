package com.alorma.myapplication.data.net

import com.alorma.myapplication.domain.model.Movie
import io.reactivex.Single
import javax.inject.Inject

class NetworkMoviesDataSource @Inject constructor(
        private val movieApi: MovieApi, private val moviesMapper: MoviesMapper) {

    fun listAll(page: Int? = null): Single<Triple<Int, Int, List<Movie>>> {
        val items: Single<PagedResponse<MovieDto>> = page?.let { movieApi.listPage(it) }
                ?: movieApi.listAll()

        return items.map {
            Triple(
                    it.page,
                    it.totalPages,
                    moviesMapper.map(it.results)
            )
        }
    }

    fun search(query: String, page: Int? = null): Single<Triple<Int, Int, List<Movie>>> {
        val items: Single<PagedResponse<MovieDto>> = page?.let { movieApi.searchPage(query, it) }
                ?: movieApi.search(query)

        return items.map {
            Triple(
                    it.page,
                    it.totalPages,
                    moviesMapper.map(it.results)
            )
        }
    }

    fun similar(id: Int, page: Int? = null): Single<Triple<Int, Int, List<Movie>>> {
        val items: Single<PagedResponse<MovieDto>> = page?.let { movieApi.similarPage(id, it) }
                ?: movieApi.similar(id)

        return items.map {
            Triple(
                    it.page,
                    it.totalPages,
                    moviesMapper.map(it.results)
            )
        }
    }

    fun item(id: Int): Single<Movie> = movieApi.item(id).map { moviesMapper.mapItem(it) }
}