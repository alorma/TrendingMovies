package com.alorma.myapplication.data.net

import com.alorma.myapplication.domain.model.TvShow
import io.reactivex.Single
import javax.inject.Inject

class ShowsDataSource @Inject constructor(
        private val showsApi: ShowsApi, private val showsMapper: ShowsMapper) {

    fun listAll(page: Int? = null): Single<Triple<Int, Int, List<TvShow>>> {
        val items: Single<PagedResponse<TvShowDto>> = page?.let { showsApi.listPage(it) }
                ?: showsApi.listAll()

        return items.map {
            Triple(
                    it.page,
                    it.totalPages,
                    showsMapper.map(it.results)
            )
        }
    }

}