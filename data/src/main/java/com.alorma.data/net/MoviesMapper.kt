package com.alorma.data.net

import com.alorma.domain.model.Images
import com.alorma.domain.model.Movie
import java.util.*

class MoviesMapper(private val dateParser: DateParser) {
    fun map(items: List<MovieDto>): List<Movie> = items.map {
        mapItem(it)
    }

    fun mapItem(it: MovieDto): Movie = Movie(it.id, it.title, it.overview, mapImages(it),
            mapDate(it.releaseDate), it.vote, it.genres)

    private fun mapImages(it: MovieDto): Images = Images(it.posterImage, it.backdropImage)

    private fun mapDate(airDate: String?): Date? = airDate?.takeIf { it.isNotEmpty() }?.let {
        dateParser parse it
    }
}