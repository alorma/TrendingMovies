package com.alorma.myapplication.data.net.config

import com.alorma.domain.exception.DataOriginException
import com.alorma.domain.model.Configuration
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import java.io.IOException

class NetworkConfigDataSource(private val configApi: ConfigApi,
                              private val mapper: ConfigurationMapper) {


    fun get(): Single<Configuration> = Single.zip(
            configApi.getConfig(),
            configApi.getGenre(),
            createZipper()
    ).onErrorResumeNext { mapError(it) }

    private fun createZipper(): BiFunction<ConfigurationResponseDto, GenreDtoResponse, Configuration> =
            BiFunction { conf, genres -> mapper.map(conf, genres) }

    private fun mapError(it: Throwable): Single<Configuration> = (it as? IOException)?.let {
        Single.error<Configuration>(DataOriginException())
    } ?: Single.error(it)

}