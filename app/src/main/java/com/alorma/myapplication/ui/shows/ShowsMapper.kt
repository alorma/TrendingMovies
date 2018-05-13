package com.alorma.myapplication.ui.shows

import com.alorma.myapplication.R
import com.alorma.myapplication.domain.model.Configuration
import com.alorma.myapplication.domain.model.TvShow
import com.alorma.myapplication.ui.common.ResourcesProvider
import javax.inject.Inject

class ShowsMapper @Inject constructor(private val resources: ResourcesProvider) {

    fun mapSuccess(tvShow: TvShow, conf: Configuration): TvShowVM = TvShowVM(tvShow.id, tvShow.title,
            getPosterImage(conf, tvShow),
            String.format("%.1f", tvShow.vote))

    private fun getPosterImage(conf: Configuration, tvShow: TvShow) =
            if (tvShow.images.poster.isNullOrBlank()) {
                null
            } else {
                "${conf.imagesUrl}${conf.imageSize}${tvShow.images.poster}"
            }

    infix fun mapError(it: Throwable): String = resources.getString(R.string.generic_error)

}