package com.alorma.myapplication.ui.search

import com.alorma.myapplication.R
import com.alorma.myapplication.domain.model.Configuration
import com.alorma.myapplication.domain.model.Movie
import com.alorma.myapplication.ui.common.DateFormatter
import com.alorma.myapplication.ui.common.ResourcesProvider
import java.util.*
import javax.inject.Inject

class SearchMapper @Inject constructor(private val dateFormatter: DateFormatter,
                                       private val resources: ResourcesProvider) {

    fun map(movie: Movie, conf: Configuration): MovieSearchItemVM = MovieSearchItemVM(movie.id, movie.title,
            movie.overview,
            getPosterImage(conf, movie), mapDate(movie.date))

    private fun getPosterImage(conf: Configuration, movie: Movie) =
            if (movie.images.poster.isNullOrBlank()) {
                null
            } else {
                "${conf.imagesUrl}${conf.imageSize}${movie.images.poster}"
            }

    private fun mapDate(date: Date?): String = date?.let { dateFormatter formatYear it }
            ?: resources.getString(R.string.no_release_date)

    infix fun mapError(it: Throwable): String = resources.getString(R.string.generic_error)

}