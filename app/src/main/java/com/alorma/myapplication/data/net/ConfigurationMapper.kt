package com.alorma.myapplication.data.net

import com.alorma.myapplication.domain.model.Configuration
import javax.inject.Inject

class ConfigurationMapper @Inject constructor() {
    fun map(it: ConfigurationResponseDto) = Configuration(it.response.imagesUrl,
            it.response.imageSize.first(), it.response.posterSize.first())
}