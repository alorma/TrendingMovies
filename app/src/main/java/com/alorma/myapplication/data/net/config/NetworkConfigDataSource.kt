package com.alorma.myapplication.data.net.config

import com.alorma.myapplication.domain.exception.DataOriginException
import com.alorma.myapplication.domain.model.Configuration
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import java.io.IOException
import javax.inject.Inject

class NetworkConfigDataSource @Inject constructor(private val configApi: ConfigApi,
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