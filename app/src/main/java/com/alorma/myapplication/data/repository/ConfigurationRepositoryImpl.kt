package com.alorma.myapplication.data.repository

import com.alorma.myapplication.common.subscribeOnIO
import com.alorma.myapplication.data.cache.LocalConfigDataSource
import com.alorma.myapplication.data.net.config.NetworkConfigDataSource
import com.alorma.myapplication.domain.model.Configuration
import com.alorma.myapplication.domain.repository.ConfigurationRepository
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