package com.alorma.myapplication.domain.repository

import com.alorma.myapplication.common.subscribeOnIO
import com.alorma.myapplication.domain.model.Movie
import io.reactivex.Single
import com.alorma.myapplication.data.cache.MoviesDataSource as Cache
import com.alorma.myapplication.data.net.MoviesDataSource as Network

class MoviesRepository(private val network: Network,
                       private val cache: Cache) {

    private var page: Int = 1
    private var searchPage: Int = 1
    private var similarPage: Int = 1

    fun listAll(): Single<List<Movie>> = execute(Single.defer {
        val cacheItems = cache.get()
        if (cacheItems.isNotEmpty()) {
            Single.just(Triple(cache.page, cache.page + 1, cacheItems))
        } else {
            network.listAll()
        }
    }.doOnSubscribe { cache.clear() })

    fun listNextPage(): Single<List<Movie>> = execute(network.listAll(page))

    private fun execute(operation: Single<Triple<Int, Int, List<Movie>>>):
            Single<List<Movie>> =
            operation.doOnSuccess { page = calculatePage(it) }
                    .doOnSuccess {
                        cache.save(it.third)
                        cache.page = page
                    }
                    .map { cache.get() }.subscribeOnIO()

    fun search(query: String): Single<List<Movie>> =
            executeSearch(network.search(query).doOnSubscribe { cache.clearSearch() })

    fun searchNextPage(query: String): Single<List<Movie>> =
            executeSearch(network.search(query, searchPage))

    private fun executeSearch(operation: Single<Triple<Int, Int, List<Movie>>>):
            Single<List<Movie>> =
            operation.doOnSuccess { searchPage = calculatePage(it) }
                    .doOnSuccess {
                        cache.saveSearch(it.third)
                        cache.searchPage = searchPage
                    }
                    .map { cache.getSearch() }.subscribeOnIO()

    fun similar(id: Int): Single<List<Movie>> = executeSimilar(id, getSimilarMovies(id))

    private fun getSimilarMovies(id: Int): Single<Triple<Int, Int, List<Movie>>> = Single.defer {
        cache.getSimilar(id).takeIf { it.isNotEmpty() }?.let {
            val page = cache.getSimilarMoviePage(id)
            Single.just(Triple(page, page + 1, it))
        } ?: network.similar(id).doOnSubscribe { cache.clearSimilar(id) }
    }

    fun similarPage(id: Int): Single<List<Movie>> = executeSimilar(id, network.similar(id, similarPage))

    private fun executeSimilar(id: Int, operation: Single<Triple<Int, Int, List<Movie>>>):
            Single<List<Movie>> =
            operation.doOnSuccess {
                similarPage = calculatePage(it)
                cache.setPageSimilarMovies(id, similarPage)
            }
                    .doOnSuccess { cache.saveSimilar(id, it.third) }
                    .map { cache.getSimilar(id) }.subscribeOnIO()

    private fun calculatePage(it: Triple<Int, Int, List<Movie>>): Int =
            it.first + if (it.first <= it.second) {
                1
            } else {
                0
            }

    fun getMovie(id: Int): Single<Movie> = Single.defer {
        cache.get(id)?.let { Single.just(it) } ?: network.item(id)
    }.subscribeOnIO()
}