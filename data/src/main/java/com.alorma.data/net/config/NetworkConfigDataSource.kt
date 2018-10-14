package com.alorma.data.net.config

import com.alorma.domain.exception.DataOriginException
import com.alorma.domain.model.Configuration

class NetworkConfigDataSource(private val configApi: ConfigApi,
                              private val mapper: ConfigurationMapper) {

    suspend fun get(): Configuration =
            try {
                mapper.map(
                        configApi.getConfig().await(),
                        configApi.getGenre().await()
                )
            } catch (e: Exception) {
                throw DataOriginException()
            }
}