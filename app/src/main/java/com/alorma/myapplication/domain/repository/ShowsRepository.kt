package com.alorma.myapplication.domain.repository

import com.alorma.myapplication.commons.subscribeOnIO
import com.alorma.myapplication.data.net.ShowsDataSource
import com.alorma.myapplication.domain.model.TvShow
import io.reactivex.Single

class ShowsRepository(private val showsDataSource: ShowsDataSource) {
    private var page: Int = 1

    fun listAll(): Single<List<TvShow>> = listNextPage()

    fun listNextPage(): Single<List<TvShow>> = showsDataSource.listAll(page).doOnSuccess {
        calculatePage(it)
    }.map { it.third }.subscribeOnIO()

    private fun calculatePage(it: Triple<Int, Int, List<TvShow>>) {
        page = it.first + if (it.first <= it.second) {
            1
        } else {
            0
        }
    }

}