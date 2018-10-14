package com.alorma.myapplication.common

import com.alorma.domain.model.Configuration
import com.alorma.domain.model.Images
import com.alorma.domain.model.Movie
import com.alorma.presentation.movies.MovieItemVM
import com.alorma.presentation.search.MovieSearchItemVM
import java.util.*

fun getConfig(): Configuration = Configuration("", "", "", listOf())
fun getMovies(number: Int): List<Movie> = (1..number).map { getMovie(it) }
fun getMovie(id: Int = 0): Movie = Movie(id, "", "", Images("", ""), Date(), 0f, listOf())
fun getMovieVM(id: Int = 0): MovieItemVM = MovieItemVM(id, "", "", "5.4")
fun getMovieSearchVM(id: Int = 0): MovieSearchItemVM = MovieSearchItemVM(id, "", "", "", "", "")