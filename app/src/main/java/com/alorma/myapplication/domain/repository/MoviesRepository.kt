package com.alorma.myapplication.domain.repository

import com.alorma.myapplication.domain.model.Movie
import io.reactivex.Single

interface MoviesRepository {
    fun listAll(): Single<List<Movie>>
    fun listNextPage(): Single<List<Movie>>

    fun similar(id: Int): Single<List<Movie>>
    fun similarPage(id: Int): Single<List<Movie>>

    fun search(query: String): Single<List<Movie>>
    fun searchNextPage(query: String): Single<List<Movie>>

    fun getMovie(id: Int): Single<Movie>
}