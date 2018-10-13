package com.alorma.data.net.config

import retrofit2.Call
import retrofit2.http.GET

interface ConfigApi {
    @GET("configuration")
    fun getConfigCall(): Call<ConfigurationResponseDto>

    @GET("genre/movie/list")
    fun getGenreCall(): Call<GenreDtoResponse>

}