package com.alorma.myapplication.data.net

import com.alorma.myapplication.domain.model.Images
import com.alorma.myapplication.domain.model.TvShow
import java.util.*
import javax.inject.Inject

class ShowsMapper @Inject constructor() {

    fun map(items: List<TvShowDto>): List<TvShow> = items.map {
        mapItem(it)
    }

    fun mapItem(it: TvShowDto) = TvShow(it.id, it.title, it.overview, mapImages(it), Date())

    private fun mapImages(it: TvShowDto): Images = Images(it.posterImage, it.backdropImage)
}