package com.alorma.data.repository

import com.alorma.data.cache.LocalConfigDataSource
import com.alorma.data.common.subscribeOnIO
import com.alorma.data.net.config.NetworkConfigDataSource
import com.alorma.domain.model.Configuration
import com.alorma.domain.repository.ConfigurationRepository
import io.reactivex.Single

class ConfigurationRepositoryImpl(private val network: NetworkConfigDataSource,
                                  private val cache: LocalConfigDataSource) : ConfigurationRepository {

    override fun getConfig(): Single<Configuration> =
            Single.defer {
                cache.get()?.let {
                    Single.just(it)
                } ?: network.get().doOnSuccess { cache.save(it) }
            }.subscribeOnIO()

}