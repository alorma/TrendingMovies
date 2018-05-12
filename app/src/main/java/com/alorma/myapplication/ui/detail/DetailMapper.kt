package com.alorma.myapplication.ui.detail

import com.alorma.myapplication.R
import com.alorma.myapplication.domain.model.Configuration
import com.alorma.myapplication.domain.model.TvShow
import com.alorma.myapplication.ui.common.DateFormatter
import com.alorma.myapplication.ui.common.ResourcesProvider
import com.alorma.myapplication.ui.shows.TvShowVM
import java.util.*
import javax.inject.Inject

class DetailMapper @Inject constructor(private val resources: ResourcesProvider,
                                       private val dateFormatter: DateFormatter) {

    fun success(tvShow: TvShow, conf: Configuration): TvShowDetailVm =
            TvShowDetailVm(tvShow.id, tvShow.title, tvShow.overview,
                    "${conf.imagesUrl}${conf.imageSize}${tvShow.images.backdrop}",
                    "${conf.imagesUrl}${conf.posterSize}${tvShow.images.backdrop}",
                    mapDate(tvShow.date), String.format("%.1f", tvShow.vote),
                    addGenres(tvShow.genres, conf.genres))

    fun mapSimilar(tvShow: TvShow, conf: Configuration): TvShowVM = TvShowVM(tvShow.id, tvShow.title,
            "${conf.imagesUrl}${conf.imageSize}${tvShow.images.poster}")

    private fun addGenres(ids: List<Int>, config: List<Pair<Int, String>>): List<String> =
            config.toMap().filterKeys { ids.contains(it) }.values.toList()

    private fun mapDate(date: Date?): String = date?.let { dateFormatter format it }
            ?: resources.getString(R.string.no_air_date)

    infix fun mapError(it: Throwable): String = resources.getString(R.string.generic_error)
}