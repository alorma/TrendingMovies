package com.alorma.domain.model

data class MovieDetail(val config: Configuration, val movie: Movie, val similar: List<Movie>)