package com.alorma.myapplication.domain.repository

import com.alorma.myapplication.commons.subscribeOnIO
import com.alorma.myapplication.domain.model.TvShow
import io.reactivex.Single
import com.alorma.myapplication.data.cache.ShowsDataSource as Cache
import com.alorma.myapplication.data.net.ShowsDataSource as Network

class ShowsRepository(private val network: Network,
                      private val cache: Cache) {

    private var page: Int = 1

    fun listAll(): Single<List<TvShow>> = execute(network.listAll()
            .doOnSubscribe { cache.clear() })

    fun listNextPage(): Single<List<TvShow>> = execute(network.listAll(page))

    private fun execute(operation: Single<Triple<Int, Int, List<TvShow>>>):
            Single<List<TvShow>> =
            operation.doOnSuccess { calculatePage(it) }
                    .doOnSuccess { cache.save(it.third) }
                    .map { cache.get() }.subscribeOnIO()

    private fun calculatePage(it: Triple<Int, Int, List<TvShow>>) {
        page = it.first + if (it.first <= it.second) {
            1
        } else {
            0
        }
    }

    fun getShow(id: Int): Single<TvShow> = Single.defer {
        cache.get(id)?.let { Single.just(it) } ?: network.item(id)
    }.subscribeOnIO()
}