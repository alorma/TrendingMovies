package com.alorma.myapplication.data.repository

import com.alorma.myapplication.common.subscribeOnIO
import com.alorma.myapplication.data.cache.LocalMoviesDataSource
import com.alorma.myapplication.data.net.NetworkMoviesDataSource
import com.alorma.myapplication.domain.model.Movie
import com.alorma.myapplication.domain.repository.MoviesRepository
import io.reactivex.Single

class MoviesRepositoryImpl(private val network: NetworkMoviesDataSource,
                       private val cache: LocalMoviesDataSource): MoviesRepository {

    private var page: Int = 1
    private var searchPage: Int = 1
    private var similarPage: Int = 1

    override fun listAll(): Single<List<Movie>> = execute(Single.defer {
        val cacheItems = cache.get()
        if (cacheItems.isNotEmpty()) {
            Single.just(Triple(cache.page, cache.page + 1, cacheItems))
        } else {
            network.listAll()
        }
    }.doOnSubscribe { cache.clear() })

    override fun listNextPage(): Single<List<Movie>> = execute(network.listAll(page))

    private fun execute(operation: Single<Triple<Int, Int, List<Movie>>>):
            Single<List<Movie>> =
            operation.doOnSuccess { page = calculatePage(it) }
                    .doOnSuccess {
                        cache.save(it.third)
                        cache.page = page
                    }
                    .map { cache.get() }.subscribeOnIO()

    override fun search(query: String): Single<List<Movie>> =
            executeSearch(network.search(query).doOnSubscribe { cache.clearSearch() })

    override fun searchNextPage(query: String): Single<List<Movie>> =
            executeSearch(network.search(query, searchPage))

    private fun executeSearch(operation: Single<Triple<Int, Int, List<Movie>>>):
            Single<List<Movie>> =
            operation.doOnSuccess { searchPage = calculatePage(it) }
                    .doOnSuccess {
                        cache.saveSearch(it.third)
                        cache.searchPage = searchPage
                    }
                    .map { cache.getSearch() }.subscribeOnIO()

    override fun similar(id: Int): Single<List<Movie>> = executeSimilar(id, getSimilarMovies(id))

    private fun getSimilarMovies(id: Int): Single<Triple<Int, Int, List<Movie>>> = Single.defer {
        cache.getSimilar(id).takeIf { it.isNotEmpty() }?.let {
            val page = cache.getSimilarMoviePage(id)
            Single.just(Triple(page, page + 1, it))
        } ?: network.similar(id).doOnSubscribe { cache.clearSimilar(id) }
    }

    override fun similarPage(id: Int): Single<List<Movie>> = executeSimilar(id, network.similar(id, similarPage))

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

    override fun getMovie(id: Int): Single<Movie> = Single.defer {
        cache.get(id)?.let { Single.just(it) } ?: network.item(id)
    }.subscribeOnIO()
}