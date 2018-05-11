package com.alorma.myapplication.data.net

import com.alorma.myapplication.domain.model.Configuration
import javax.inject.Inject

class ConfigurationMapper @Inject constructor() {

    companion object {
        const val MIN_SIZE = "w300"
    }

    fun map(it: ConfigurationResponseDto) = Configuration(it.response.imagesUrl,
            getImageSize(it.response.imageSize), getImageSize(it.response.posterSize))

    private fun getImageSize(it: List<String>): String {
        val index = it.indexOfFirst { it == MIN_SIZE }
        return if (index != -1) {
            it[index]
        } else {
            it.last()
        }
    }
}