package com.alorma.myapplication.data.net

import com.alorma.myapplication.domain.model.Images
import com.alorma.myapplication.domain.model.TvShow
import java.util.*
import javax.inject.Inject

class ShowsMapper @Inject constructor(private val dateParser: DateParser) {
    fun map(items: List<TvShowDto>): List<TvShow> = items.map {
        mapItem(it)
    }

    fun mapItem(it: TvShowDto): TvShow = TvShow(it.id, it.title, it.overview, mapImages(it),
            mapDate(it.airDate), it.vote)

    private fun mapImages(it: TvShowDto): Images = Images(it.posterImage, it.backdropImage)

    private fun mapDate(airDate: String?): Date? = airDate?.let {
        dateParser parse it
    }
}