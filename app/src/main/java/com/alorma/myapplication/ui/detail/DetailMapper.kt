package com.alorma.myapplication.ui.detail

import com.alorma.myapplication.R
import com.alorma.myapplication.domain.model.Configuration
import com.alorma.myapplication.domain.model.Movie
import com.alorma.myapplication.ui.common.DateFormatter
import com.alorma.myapplication.ui.common.ResourcesProvider
import com.alorma.myapplication.ui.movies.MovieItemVM
import java.util.*
import javax.inject.Inject

class DetailMapper @Inject constructor(private val resources: ResourcesProvider,
                                       private val dateFormatter: DateFormatter) {

    fun success(movie: Movie): MovieDetailVM =
            MovieDetailVM(movie.id, movie.title, movie.overview,
                    getHeroImage(movie),
                    mapDate(movie.date), String.format("%.1f", movie.vote),
                    addGenres(movie.genres, listOf()))

    private fun getHeroImage(movie: Movie) =
            if (movie.images.backdrop.isNullOrBlank() && movie.images.poster.isNullOrBlank()) {
                null
            } else {
                "https://image.tmdb.org/t/p/w500${movie.images.backdrop}"
            }

    fun mapSimilar(movie: Movie, conf: Configuration): MovieItemVM = MovieItemVM(movie.id, movie.title,
            "${conf.imagesUrl}${conf.imageSize}${movie.images.poster}",
            String.format("%.1f", movie.vote))

    private fun addGenres(ids: List<Int>, config: List<Pair<Int, String>>): List<String> =
            config.toMap().filterKeys { ids.contains(it) }.values.toList()

    private fun mapDate(date: Date?): String = date?.let { dateFormatter format it }
            ?: resources.getString(R.string.no_release_date)

    infix fun mapError(it: Throwable): String = resources.getString(R.string.generic_error)
}