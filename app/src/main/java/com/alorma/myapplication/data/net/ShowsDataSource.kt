package com.alorma.myapplication.data.net

import com.alorma.myapplication.domain.model.TvShow
import io.reactivex.Single
import javax.inject.Inject

class ShowsDataSource @Inject constructor(
        private val showsApi: ShowsApi, private val showsMapper: ShowsMapper) {

    fun listAll(page: Int? = null): Single<List<TvShow>> {
        val items: Single<List<TvShowDto>> = page?.let { showsApi.listPage(it) }
                ?: showsApi.listAll()

        return items.map { showsMapper.map(it) }
    }

}