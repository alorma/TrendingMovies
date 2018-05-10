package com.alorma.myapplication.ui.shows

import com.alorma.myapplication.domain.model.TvShow
import javax.inject.Inject

class ShowsMapper @Inject constructor() {

    fun map(items: List<TvShow>): List<TvShowVM> = items.map {
        TvShowVM((it.id))
    }

}