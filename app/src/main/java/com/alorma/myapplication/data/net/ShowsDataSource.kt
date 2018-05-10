package com.alorma.myapplication.data.net

import com.alorma.myapplication.domain.model.TvShow
import io.reactivex.Single
import javax.inject.Inject

class ShowsDataSource @Inject constructor(
    private val showsApi: ShowsApi, private val showsMapper: ShowsMapper) {

    fun listAll() : Single<List<TvShow>> = showsApi.listAll().map { showsMapper.map(it) }

}