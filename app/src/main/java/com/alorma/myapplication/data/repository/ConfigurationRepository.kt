package com.alorma.myapplication.data.repository

import com.alorma.myapplication.common.subscribeOnIO
import com.alorma.myapplication.data.cache.LocalConfigDataSource
import com.alorma.myapplication.data.net.config.NetworkConfigDataSource
import com.alorma.myapplication.domain.model.Configuration
import io.reactivex.Single

class ConfigurationRepository(private val network: NetworkConfigDataSource,
                              private val cache: LocalConfigDataSource) {

    fun getConfig(): Single<Configuration> =
            Single.defer {
                cache.get()?.let {
                    Single.just(it)
                } ?: network.get().doOnSuccess { cache.save(it) }
            }.subscribeOnIO()

}