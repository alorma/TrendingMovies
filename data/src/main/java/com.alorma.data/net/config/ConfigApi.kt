package com.alorma.data.net.config

import kotlinx.coroutines.experimental.Deferred
import retrofit2.http.GET

interface ConfigApi {
    @GET("configuration")
    fun getConfig(): Deferred<ConfigurationResponseDto>

    @GET("genre/movie/list")
    fun getGenre(): Deferred<GenreDtoResponse>

}