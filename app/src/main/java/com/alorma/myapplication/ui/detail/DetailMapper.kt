package com.alorma.myapplication.ui.detail

import android.graphics.Color
import android.text.*
import android.text.Annotation
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
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

    fun mapSimilars(movies: List<Movie>, conf: Configuration): List<MovieItemVM> = movies.map {
        mapSimilar(it, conf)
    }

    fun mapSimilar(movie: Movie, conf: Configuration): MovieItemVM = MovieItemVM(movie.id, movie.title,
            "${conf.imagesUrl}${conf.imageSize}${movie.images.poster}",
            mapVotes(movie.vote))

    private fun mapVotes(vote: Float): CharSequence {
        val resource = R.string.movie_vote_annotate
        val textResource = resources.getText(R.string.movie_vote_annotate)
        return when (textResource) {
            is SpannedString -> spanString(textResource, vote)
            else -> resources.getString(resource, String.format("%.1f", vote))
        }
    }

    private fun spanString(textResource: SpannedString, vote: Float): SpannableString {
        val annotations = textResource.getSpans(0, textResource.length, Annotation::class.java)

        val voteText = " ${String.format("%.1f", vote)} "

        val start = textResource.indexOf("%1\$s")
        val range = start..(start + 4)

        val spannableString = SpannableString(textResource.replaceRange(range, voteText))

        annotations.forEach {
            val spanRange = textResource.getSpanStart(it)..textResource.getSpanEnd(it)
            when {
                it.key == "rtColor" -> {
                    val colorValue = Color.parseColor(it.value)
                    spannableString.setSpan(ForegroundColorSpan(colorValue), spanRange)
                }
                it.key == "rtSize" -> {
                    val sizeValue = it.value
                    spannableString.setSpan(AbsoluteSizeSpan(sizeValue.toInt(), true), spanRange)
                }
                else -> {
                    val textRange = textResource.getSpanStart(it)..textResource.getSpanEnd(it)
                    spannableString.setSpan(when {
                        it.key == "color" -> ForegroundColorSpan(Color.parseColor(it.value))
                        it.key == "size" -> AbsoluteSizeSpan(it.value.toInt(), true)
                        else -> RelativeSizeSpan(1f)
                    }, textRange)
                }
            }
        }

        return spannableString
    }

    private fun SpannableString.setSpan(span: ParcelableSpan, spanRange: IntRange) {
        setSpan(span, spanRange.start, spanRange.endInclusive,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    private fun addGenres(ids: List<Int>, config: List<Pair<Int, String>>): List<String> =
            config.toMap().filterKeys { ids.contains(it) }.values.toList()

    private fun mapDate(date: Date?): String = date?.let { dateFormatter format it }
            ?: resources.getString(R.string.no_release_date)

    infix fun mapError(it: Throwable): String = resources.getString(R.string.generic_error)
}