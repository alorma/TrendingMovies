package com.alorma.myapplication.ui.detail

import com.alorma.myapplication.R
import com.alorma.myapplication.domain.model.Configuration
import com.alorma.myapplication.domain.model.Movie
import com.alorma.myapplication.ui.common.DateFormatter
import com.alorma.myapplication.ui.common.ResourcesProvider
import com.alorma.myapplication.ui.movies.MovieItemVM
import java.util.*

class DetailMapper(private val resources: ResourcesProvider,
                   private val dateFormatter: DateFormatter) {

    fun success(movie: Movie, conf: Configuration): MovieDetailVM =
            MovieDetailVM(movie.id, movie.title, movie.overview,
                    getHeroImage(conf, movie),
                    mapDate(movie.date), mapVotes(movie.vote),
                    addGenres(movie.genres, conf.genres))

    private fun getHeroImage(conf: Configuration, movie: Movie) =
            if (movie.images.backdrop.isNullOrBlank() && movie.images.poster.isNullOrBlank()) {
                null
            } else {
                "${conf.imagesUrl}${conf.imageSize}${movie.images.backdrop
                        ?: movie.images.poster}"
            }

    fun mapSimilar(movies: List<Movie>, conf: Configuration): List<MovieItemVM> = movies.map {
        mapSimilar(it, conf)
    }

    fun mapSimilar(movie: Movie, conf: Configuration): MovieItemVM = MovieItemVM(movie.id, movie.title,
            "${conf.imagesUrl}${conf.imageSize}${movie.images.poster}",
            mapVotes(movie.vote))

    private fun mapVotes(vote: Float): String = String.format("%.1f", vote)

    private fun addGenres(ids: List<Int>, config: List<Pair<Int, String>>): List<String> =
            config.toMap().filterKeys { ids.contains(it) }.values.toList()

    private fun mapDate(date: Date?): String = date?.let { dateFormatter format it }
            ?: resources.getString(R.string.no_release_date)

    infix fun mapError(it: Throwable): String = resources.getString(R.string.generic_error)
}