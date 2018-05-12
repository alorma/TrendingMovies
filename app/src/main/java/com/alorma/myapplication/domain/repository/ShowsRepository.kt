package com.alorma.myapplication.domain.repository

import com.alorma.myapplication.commons.subscribeOnIO
import com.alorma.myapplication.domain.model.TvShow
import io.reactivex.Single
import com.alorma.myapplication.data.cache.ShowsDataSource as Cache
import com.alorma.myapplication.data.net.ShowsDataSource as Network

class ShowsRepository(private val network: Network,
                      private val cache: Cache) {

    private var page: Int = 1
    private var similarPage: Int = 1

    fun listAll(): Single<List<TvShow>> = execute(network.listAll()
            .doOnSubscribe { cache.clear() })

    fun listNextPage(): Single<List<TvShow>> = execute(network.listAll(page))

    private fun execute(operation: Single<Triple<Int, Int, List<TvShow>>>):
            Single<List<TvShow>> =
            operation.doOnSuccess { page = calculatePage(it) }
                    .doOnSuccess { cache.save(it.third) }
                    .map { cache.get() }.subscribeOnIO()

    fun similar(id: Int): Single<List<TvShow>> = executeSimilar(id, getSimilarShows(id))

    private fun getSimilarShows(id: Int): Single<Triple<Int, Int, List<TvShow>>> = Single.defer {
        cache.getSimilar(id).takeIf { it.isNotEmpty() }?.let {
            val page = cache.getSimilarShowPage(id)
            Single.just(Triple(page, page + 1, it))
        } ?: network.similar(id).doOnSubscribe { cache.clearSimilar(id) }
    }

    fun similarPage(id: Int): Single<List<TvShow>> = executeSimilar(id, network.similar(id, similarPage))

    private fun executeSimilar(id: Int, operation: Single<Triple<Int, Int, List<TvShow>>>):
            Single<List<TvShow>> =
            operation.doOnSuccess {
                similarPage = calculatePage(it)
                cache.setPageSimilarShows(id, similarPage)
            }
                    .doOnSuccess { cache.saveSimilar(id, it.third) }
                    .map { cache.getSimilar(id) }.subscribeOnIO()

    private fun calculatePage(it: Triple<Int, Int, List<TvShow>>): Int =
            it.first + if (it.first <= it.second) {
                1
            } else {
                0
            }

    fun getShow(id: Int): Single<TvShow> = Single.defer {
        cache.get(id)?.let { Single.just(it) } ?: network.item(id)
    }.subscribeOnIO()
}