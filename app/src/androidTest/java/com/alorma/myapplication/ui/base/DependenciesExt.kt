package com.alorma.myapplication.ui.base

import com.alorma.domain.exception.DataOriginException
import com.alorma.domain.model.Configuration
import com.alorma.domain.model.Images
import com.alorma.domain.model.Movie
import com.alorma.domain.repository.ConfigurationRepository
import com.alorma.domain.repository.MoviesRepository
import com.alorma.myapplication.ui.detail.MovieDetailActivityTest
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.given
import io.reactivex.Single
import org.mockito.ArgumentMatchers.anyInt
import java.util.*

fun ConfigurationRepository.asValidConfig() {
    fun generateConfig(): Configuration = Configuration("url", "500", "500",
            listOf(1 to "Comedy", 2 to "Drama"))
    given(getConfig()).willReturn(Single.just(generateConfig()))
}

fun ConfigurationRepository.asDataOriginError() {
    given(getConfig()).willReturn(Single.error(DataOriginException()))
}

fun ConfigurationRepository.asError() {
    given(getConfig()).willReturn(Single.error(Exception()))
}

fun MoviesRepository.asListValidData(number: Int) {
    fun generateItem(id: Int): Movie = Movie(id, "Title $id", "", Images("", ""), Date(), 0f, listOf())
    fun generateItems(number: Int): List<Movie> = (1..number).map { generateItem(it) }

    val items = generateItems(number)
    given(listAll()).willReturn(Single.just(items))
}

fun MoviesRepository.asSingleItem() {
    fun generateItem(id: Int): Movie = Movie(id, "Title $id", "", Images("", ""), Date(), 0f, listOf())

    val item = generateItem(1)
    given(listAll()).willReturn(Single.just(listOf(item)))
}

fun MoviesRepository.asEmptyList() {
    given(listAll()).willReturn(Single.just(emptyList()))
}

fun MoviesRepository.asListNextPageValidData(number: Int) {
    fun generateItem(id: Int): Movie = Movie(id, "Title $id", "", Images("", ""), Date(), 0f, listOf())
    fun generateItems(number: Int): List<Movie> = (1..number).map { generateItem(it) }

    val items = generateItems(number)
    given(listNextPage()).willReturn(Single.just(items))
}

fun MoviesRepository.asSimilarEmptyList(id: Int) {
    given(similar(eq(id))).willReturn(Single.just(emptyList()))
}

fun MoviesRepository.asSimilarListValidData(id: Int, number: Int) {
    fun generateSimilarItem(id: Int): Movie = Movie(id, "Similar $id", MovieDetailActivityTest.OVERVIEW,
            Images("", ""), Date(), MovieDetailActivityTest.VOTE, listOf(1, 2))

    fun generateSimilarItems(number: Int): List<Movie> = (1..number).map { generateSimilarItem(it) }

    given(similar(eq(id))).willReturn(Single.just(generateSimilarItems(number)))
}

fun MoviesRepository.asMovieValidData(id: Int? = null) {
    fun generateItem(id: Int): Movie = Movie(id, "Title $id", MovieDetailActivityTest.OVERVIEW,
            Images("", ""), Date(), MovieDetailActivityTest.VOTE, listOf(1, 2))

    given(getMovie(id?.let { eq(id) } ?: anyInt())).willReturn(Single.just(generateItem(id ?: 0)))
}

fun MoviesRepository.asError() {
    given(listAll()).willReturn(Single.error(Exception()))
}

