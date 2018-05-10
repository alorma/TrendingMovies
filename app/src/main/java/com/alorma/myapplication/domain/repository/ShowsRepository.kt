package com.alorma.myapplication.domain.repository

import com.alorma.myapplication.commons.subscribeOnIO
import com.alorma.myapplication.data.net.ShowsDataSource
import com.alorma.myapplication.domain.model.TvShow
import io.reactivex.Single

class ShowsRepository(private val showsDataSource: ShowsDataSource) {
    fun listAll(): Single<List<TvShow>> = showsDataSource.listAll().subscribeOnIO()

}