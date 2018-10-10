package com.alorma.data.net.config

import com.alorma.domain.model.Configuration

class ConfigurationMapper{

    companion object {
        const val POSTER_MIN_SIZE = "w500"
        const val BACKDROP_MIN_SIZE = "w1280"
    }

    fun map(conf: ConfigurationResponseDto, genres: GenreDtoResponse): Configuration =
            Configuration(conf.response.imagesUrl,
                    getImageSize(conf.response.imageSize),
                    getImageSize(conf.response.posterSize, BACKDROP_MIN_SIZE),
                    map(genres))

    private fun getImageSize(it: List<String>, def: String = POSTER_MIN_SIZE): String {
        val index = it.indexOfFirst { it == def }
        return if (index != -1) {
            it[index]
        } else {
            it.last()
        }
    }

    private fun map(it: GenreDtoResponse): List<Pair<Int, String>> = it.results.map { it.id to it.name }
}