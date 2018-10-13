package com.alorma.data.net.config

import com.alorma.domain.exception.DataOriginException
import com.alorma.domain.model.Configuration
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.async

class NetworkConfigDataSource(private val configApi: ConfigApi,
                              private val mapper: ConfigurationMapper) {

    fun get(): Deferred<Configuration> = GlobalScope.async {
        val configurationResponseDto = configApi.getConfigCall().execute().body()
        val genreDtoResponse = configApi.getGenreCall().execute().body()

        configurationResponseDto?.let { config ->
            genreDtoResponse?.let { genres ->
                mapper.map(config, genres)
            } ?: throw DataOriginException()
        } ?: throw DataOriginException()
    }
}