package com.alorma.myapplication.ui.shows

import com.alorma.myapplication.R
import com.alorma.myapplication.domain.model.TvShow
import com.alorma.myapplication.ui.common.ResourcesProvider
import javax.inject.Inject

class ShowsMapper @Inject constructor(private val resources: ResourcesProvider) {

    fun map(items: List<TvShow>): List<TvShowVM> = items.map {
        TvShowVM((it.id))
    }

    fun error(it: Throwable): String = resources.getString(R.string.generic_error)

}