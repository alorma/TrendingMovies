package com.alorma.presentation.search

import com.alorma.domain.common.ResourceProvider
import com.alorma.domain.exception.DataOriginException
import com.alorma.domain.model.Configuration
import com.alorma.domain.model.Movie
import com.alorma.presentation.R
import com.alorma.presentation.common.DateFormatter
import java.util.*

class SearchMapper(private val dateFormatter: DateFormatter,
                   private val resources: ResourceProvider) {

    fun map(movie: Movie, conf: Configuration): MovieSearchItemVM = MovieSearchItemVM(movie.id, movie.title,
            movie.overview,
            getPosterImage(conf, movie), mapDate(movie.date),
            String.format("%.1f", movie.vote))

    private fun getPosterImage(conf: Configuration, movie: Movie) =
            if (movie.images.poster.isNullOrBlank()) {
                null
            } else {
                "${conf.imagesUrl}${conf.imageSize}${movie.images.poster}"
            }

    private fun mapDate(date: Date?): String = date?.let { dateFormatter formatYear it }
            ?: resources.getString(R.string.no_release_date)

    infix fun mapError(it: Throwable): String =
            resources.getString((it as? DataOriginException)?.let {
                R.string.data_origin_error
            } ?: R.string.generic_error)

}