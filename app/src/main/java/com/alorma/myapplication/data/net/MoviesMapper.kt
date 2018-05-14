package com.alorma.myapplication.data.net

import com.alorma.myapplication.domain.model.Images
import com.alorma.myapplication.domain.model.Movie
import java.util.*
import javax.inject.Inject

class MoviesMapper @Inject constructor(private val dateParser: DateParser) {
    fun map(items: List<MovieDto>): List<Movie> = items.map {
        mapItem(it)
    }

    fun mapItem(it: MovieDto): Movie = Movie(it.id, it.title, it.overview, mapImages(it),
            mapDate(it.releaseDate), it.vote, it.genres)

    private fun mapImages(it: MovieDto): Images = Images(it.posterImage, it.backdropImage)

    private fun mapDate(airDate: String?): Date? = airDate?.let {
        dateParser parse it
    }
}