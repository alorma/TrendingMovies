package com.alorma.myapplication.ui.movies

import com.alorma.myapplication.R
import com.alorma.domain.exception.DataOriginException
import com.alorma.domain.model.Configuration
import com.alorma.domain.model.Movie
import com.alorma.myapplication.ui.common.ResourcesProvider

class MoviesMapper(private val resources: ResourcesProvider) {

    fun mapSuccess(movie: Movie, conf: Configuration): MovieItemVM = MovieItemVM(movie.id, movie.title,
            getPosterImage(conf, movie),
            String.format("%.1f", movie.vote))

    private fun getPosterImage(conf: Configuration, movie: Movie) =
            if (movie.images.poster.isNullOrBlank()) {
                null
            } else {
                "${conf.imagesUrl}${conf.imageSize}${movie.images.poster}"
            }

    infix fun mapError(it: Throwable): String =
            resources.getString((it as? DataOriginException)?.let {
                R.string.data_origin_error
            } ?: R.string.generic_error)
}