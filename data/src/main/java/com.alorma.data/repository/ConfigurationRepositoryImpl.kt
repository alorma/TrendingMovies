package com.alorma.data.repository

import com.alorma.data.cache.LocalConfigDataSource
import com.alorma.data.net.config.NetworkConfigDataSource
import com.alorma.domain.model.Configuration
import com.alorma.domain.repository.ConfigurationRepository

class ConfigurationRepositoryImpl(private val network: NetworkConfigDataSource,
                                  private val cache: LocalConfigDataSource) : ConfigurationRepository {

    override suspend fun getConfig(): Configuration =
            cache.get() ?: network.get().await().also {
                cache.save(it)
            }
}