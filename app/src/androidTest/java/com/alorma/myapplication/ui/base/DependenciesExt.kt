package com.alorma.myapplication.ui.base

import com.alorma.domain.exception.DataOriginException
import com.alorma.domain.model.Configuration
import com.alorma.domain.model.Images
import com.alorma.domain.model.Movie
import com.alorma.domain.repository.ConfigurationRepository
import com.alorma.domain.repository.MoviesRepository
import com.alorma.myapplication.ui.detail.MovieDetailActivityTest
import com.nhaarman.mockito_kotlin.doAnswer
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.whenever
import kotlinx.coroutines.experimental.runBlocking
import org.mockito.ArgumentMatchers.anyInt
import java.util.*

fun ConfigurationRepository.asValidConfig() {
    fun generateConfig(): Configuration = Configuration("url", "500", "500",
            listOf(1 to "Comedy", 2 to "Drama"))
    runBlocking {
        given(getConfig()).willReturn(generateConfig())
    }
}

fun ConfigurationRepository.asDataOriginError() {
    runBlocking {
        doAnswer { throw DataOriginException() }.whenever(this@asDataOriginError).getConfig()
    }
}

fun ConfigurationRepository.asError() {
    runBlocking {
        doAnswer { throw Exception() }.whenever(this@asError).getConfig()
    }
}

fun MoviesRepository.asListValidData(number: Int) {
    fun generateItem(id: Int): Movie = Movie(id, "Title $id", "", Images("", ""), Date(), 0f, listOf())
    fun generateItems(number: Int): List<Movie> = (1..number).map { generateItem(it) }

    runBlocking {
        val items = generateItems(number)
        given(listAll()).willReturn(items)

    }
}

fun MoviesRepository.asSingleItem() {
    fun generateItem(id: Int): Movie = Movie(id, "Title $id", "", Images("", ""), Date(), 0f, listOf())

    runBlocking {
        val item = generateItem(1)
        given(listAll()).willReturn(listOf(item))
    }
}

fun MoviesRepository.asEmptyList() {
    runBlocking {
        given(listAll()).willReturn(emptyList())
    }
}

fun MoviesRepository.asListNextPageValidData(number: Int) {
    fun generateItem(id: Int): Movie = Movie(id, "Title $id", "", Images("", ""), Date(), 0f, listOf())
    fun generateItems(number: Int): List<Movie> = (1..number).map { generateItem(it) }

    runBlocking {
        val items = generateItems(number)
        given(listNextPage()).willReturn(items)
    }
}

fun MoviesRepository.asSimilarEmptyList(id: Int) {
    runBlocking {
        given(similar(eq(id))).willReturn(emptyList())
    }
}

fun MoviesRepository.asSimilarListValidData(id: Int? = null, number: Int) {
    fun generateSimilarItem(id: Int): Movie = Movie(id, "Similar $id", MovieDetailActivityTest.OVERVIEW,
            Images("", ""), Date(), MovieDetailActivityTest.VOTE, listOf(1, 2))

    fun generateSimilarItems(number: Int): List<Movie> = (1..number).map { generateSimilarItem(it) }

    runBlocking {
        val list = generateSimilarItems(number)
        given(similar(id?.let { eq(id) } ?: anyInt())).willReturn(list)
    }
}

fun MoviesRepository.asMovieValidData(id: Int? = null) {
    fun generateItem(id: Int): Movie = Movie(id, "Title $id", MovieDetailActivityTest.OVERVIEW,
            Images("", ""), Date(), MovieDetailActivityTest.VOTE, listOf(1, 2))

    runBlocking {
        val movie = generateItem(id ?: 0)
        given(getMovie(id?.let { eq(id) } ?: anyInt())).willReturn(movie)
    }
}

fun MoviesRepository.asError() {
    runBlocking {
        doAnswer { throw java.lang.Exception() }.whenever(this@asError).listAll()
    }
}


fun MoviesRepository.asDataOriginError() {
    runBlocking {
        doAnswer { throw DataOriginException() }.whenever(this@asDataOriginError).listAll()
    }
}

