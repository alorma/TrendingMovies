package com.alorma.myapplication.data.net

import com.alorma.myapplication.domain.model.TvShow
import javax.inject.Inject

class ShowsMapper @Inject constructor() {

    fun map(items: List<TvShowDto>): List<TvShow> = items.map {
        TvShow(it.id)
    }
}