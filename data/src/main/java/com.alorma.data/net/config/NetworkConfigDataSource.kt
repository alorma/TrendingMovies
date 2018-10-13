package com.alorma.data.net.config

import com.alorma.domain.model.Configuration

class NetworkConfigDataSource(private val configApi: ConfigApi,
                              private val mapper: ConfigurationMapper) {

    suspend fun get(): Configuration =
            mapper.map(
                    configApi.getConfig().await(),
                    configApi.getGenre().await()
            )
}