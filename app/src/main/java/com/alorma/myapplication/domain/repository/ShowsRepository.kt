package com.alorma.myapplication.domain.repository

import com.alorma.myapplication.commons.subscribeOnIO
import com.alorma.myapplication.data.net.ShowsDataSource
import com.alorma.myapplication.domain.model.TvShow
import io.reactivex.Single

class ShowsRepository(private val showsDataSource: ShowsDataSource) {
    private var page: Int = 0

    fun listAll(): Single<List<TvShow>> = showsDataSource.listAll().doOnSuccess {
        page = page++
    }.subscribeOnIO()

    fun listNextPage(): Single<List<TvShow>> = showsDataSource.listAll(page).subscribeOnIO()

}