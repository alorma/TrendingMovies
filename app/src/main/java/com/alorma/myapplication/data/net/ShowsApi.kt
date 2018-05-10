package com.alorma.myapplication.data.net

import io.reactivex.Single

interface ShowsApi {
    fun listAll(): Single<List<TvShowDto>>
    fun listPage(page: Int): Single<List<TvShowDto>>
}