package com.alorma.domain.repository

import com.alorma.domain.model.Movie

interface MoviesRepository {
    suspend fun listAll(): List<Movie>
    suspend fun listNextPage(): List<Movie>

    suspend fun similar(id: Int): List<Movie>
    suspend fun similarPage(id: Int): List<Movie>

    suspend fun search(query: String): List<Movie>
    suspend fun searchNextPage(query: String): List<Movie>

    suspend fun getMovie(id: Int): Movie
}