package com.alorma.myapplication.data.net

import com.alorma.myapplication.domain.model.Configuration
import javax.inject.Inject

class ConfigurationMapper @Inject constructor() {

    companion object {
        const val POSTER_MIN_SIZE = "w300"
        const val BACKDROP_MIN_SIZE = "w1280"
    }

    fun map(it: ConfigurationResponseDto): Configuration = Configuration(it.response.imagesUrl,
            getImageSize(it.response.imageSize),
            getImageSize(it.response.posterSize, BACKDROP_MIN_SIZE))

    private fun getImageSize(it: List<String>, def: String = POSTER_MIN_SIZE): String {
        val index = it.indexOfFirst { it == def }
        return if (index != -1) {
            it[index]
        } else {
            it.last()
        }
    }
}