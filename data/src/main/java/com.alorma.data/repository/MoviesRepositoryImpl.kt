package com.alorma.data.repository

import com.alorma.data.cache.LocalMoviesDataSource
import com.alorma.data.net.NetworkMoviesDataSource
import com.alorma.domain.model.Movie
import com.alorma.domain.model.MovieList
import com.alorma.domain.repository.MoviesRepository

class MoviesRepositoryImpl(private val network: NetworkMoviesDataSource,
                           private val cache: LocalMoviesDataSource) : MoviesRepository {

    private var page: Int = 1
    private var searchPage: Int = 1
    private var similarPage: Int = 1

    override suspend fun listAll(): List<Movie> =
            cache.get().takeIf { it.isNotEmpty() }?.also {
                page = cache.page
            } ?: network.listAll().also {
                page = calculatePage(it)
            }.movies.also {
                cache.clear()
                cache.save(it)
            }

    override suspend fun listNextPage(): List<Movie> =
            network.listAll(page).also {
                page = calculatePage(it)
            }.movies.also {
                cache.save(it)
            }.let {
                cache.get()
            }

    override suspend fun search(query: String): List<Movie> =
            cache.getSearch().takeIf { it.isNotEmpty() }?.also {

                searchPage = cache.searchPage
            } ?: network.search(query).also {
                page = calculatePage(it)
            }.movies.also {
                cache.clearSearch()
                cache.saveSearch(it)
            }

    override suspend fun searchNextPage(query: String): List<Movie> =
            network.search(query, searchPage).also {
                page = calculatePage(it)
            }.movies.also {
                cache.saveSearch(it)
            }.let {
                cache.getSearch()
            }

    override suspend fun similar(id: Int): List<Movie> =
            cache.getSimilar(id).takeIf { it.isNotEmpty() }?.also {
                similarPage = cache.getSimilarMoviePage(id)
            } ?: network.similar(id).also {
                page = calculatePage(it)
            }.movies.also {
                cache.clearSimilar(id)
                cache.saveSimilar(id, it)
            }

    override suspend fun similarPage(id: Int): List<Movie> =
            network.similar(id, similarPage).also {
                page = calculatePage(it)
            }.movies.also {
                cache.saveSimilar(id, it)
            }.let {
                cache.getSimilar(id)
            }

    private fun calculatePage(it: MovieList): Int =
            it.page + if (it.page <= it.totalPage) {
                1
            } else {
                0
            }

    override suspend fun getMovie(id: Int): Movie =
            cache.get(id) ?: network.item(id)
}