package com.alorma.myapplication.data.net

import com.alorma.myapplication.domain.model.Configuration
import io.reactivex.Single
import javax.inject.Inject

class ConfigDataSource @Inject constructor(private val configApi: ConfigApi,
                                           private val mapper: ConfigurationMapper) {


    fun get(): Single<Configuration> = configApi.getConfig().map { mapper.map(it) }

}