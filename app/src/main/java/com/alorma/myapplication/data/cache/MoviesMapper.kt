package com.alorma.myapplication.data.cache

import com.alorma.myapplication.domain.model.Images
import com.alorma.myapplication.domain.model.Movie
import java.util.*
import javax.inject.Inject

class MoviesMapper @Inject constructor() {
    fun map(movie: Movie): MovieEntity = MovieEntity(movie.id, movie.title, movie.overview,
            movie.date?.time, movie.vote, mapImages(movie))

    private fun mapImages(movie: Movie): ImagesEntity =
            ImagesEntity(movie.images.poster, movie.images.backdrop)

    fun mapEntity(movie: MovieEntity): Movie = Movie(movie.id, movie.title, movie.overview,
            mapImagesEntity(movie), movie.date?.let { Date(it) }, movie.vote, listOf())

    private fun mapImagesEntity(movie: MovieEntity): Images =
            Images(movie.images.poster, movie.images.backdrop)
}