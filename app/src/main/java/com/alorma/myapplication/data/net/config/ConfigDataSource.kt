package com.alorma.myapplication.data.net.config

import com.alorma.myapplication.domain.model.Configuration
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class ConfigDataSource @Inject constructor(private val configApi: ConfigApi,
                                           private val mapper: ConfigurationMapper) {


    fun get(): Single<Configuration> = Single.zip(configApi.getConfig(), configApi.getGenre(),
            BiFunction { conf, genres -> mapper.map(conf, genres) })
}