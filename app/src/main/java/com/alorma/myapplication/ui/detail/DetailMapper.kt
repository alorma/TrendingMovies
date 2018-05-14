package com.alorma.myapplication.ui.detail

import com.alorma.myapplication.R
import com.alorma.myapplication.domain.model.Configuration
import com.alorma.myapplication.domain.model.Movie
import com.alorma.myapplication.ui.common.DateFormatter
import com.alorma.myapplication.ui.common.ResourcesProvider
import com.alorma.myapplication.ui.movies.MoviewItemVM
import java.util.*
import javax.inject.Inject

class DetailMapper @Inject constructor(private val resources: ResourcesProvider,
                                       private val dateFormatter: DateFormatter) {

    fun success(movie: Movie, conf: Configuration): MovieDetailVM =
            MovieDetailVM(movie.id, movie.title, movie.overview,
                    getHeroImage(conf, movie),
                    mapDate(movie.date), String.format("%.1f", movie.vote),
                    addGenres(movie.genres, conf.genres))

    private fun getHeroImage(conf: Configuration, movie: Movie) =
            if (movie.images.backdrop.isNullOrBlank() && movie.images.poster.isNullOrBlank()) {
                null
            } else {
                "${conf.imagesUrl}${conf.imageSize}${movie.images.backdrop
                        ?: movie.images.poster}"
            }

    fun mapSimilar(movie: Movie, conf: Configuration): MoviewItemVM = MoviewItemVM(movie.id, movie.title,
            "${conf.imagesUrl}${conf.imageSize}${movie.images.poster}",
            String.format("%.1f", movie.vote))

    private fun addGenres(ids: List<Int>, config: List<Pair<Int, String>>): List<String> =
            config.toMap().filterKeys { ids.contains(it) }.values.toList()

    private fun mapDate(date: Date?): String = date?.let { dateFormatter format it }
            ?: resources.getString(R.string.no_air_date)

    infix fun mapError(it: Throwable): String = resources.getString(R.string.generic_error)
}