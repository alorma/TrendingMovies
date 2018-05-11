package com.alorma.myapplication.ui.detail

import com.alorma.myapplication.R
import com.alorma.myapplication.domain.model.Configuration
import com.alorma.myapplication.domain.model.TvShow
import com.alorma.myapplication.ui.common.DateFormatter
import com.alorma.myapplication.ui.common.ResourcesProvider
import java.util.*
import javax.inject.Inject

class DetailMapper @Inject constructor(private val resources: ResourcesProvider,
                                       private val dateFormatter: DateFormatter) {

    fun success(tvShow: TvShow, conf: Configuration): TvShowDetailVm =
            TvShowDetailVm(tvShow.id, tvShow.title, tvShow.overview,
                    "${conf.imagesUrl}${conf.imageSize}${tvShow.images.backdrop}",
                    mapDate(tvShow.date), String.format("%.2f", tvShow.vote))

    private fun mapDate(date: Date?): String = date?.let { dateFormatter format it }
            ?: resources.getString(R.string.no_air_date)

    infix fun mapError(it: Throwable): String = resources.getString(R.string.generic_error)
}