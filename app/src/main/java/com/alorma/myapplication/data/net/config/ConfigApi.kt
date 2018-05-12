package com.alorma.myapplication.data.net.config

import io.reactivex.Single
import retrofit2.http.GET

interface ConfigApi {
    @GET("configuration")
    fun getConfig(): Single<ConfigurationResponseDto>

    @GET("genre/tv/list")
    fun getGenre(): Single<GenreDtoResponse>

}