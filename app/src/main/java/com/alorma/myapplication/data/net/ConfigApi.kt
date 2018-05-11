package com.alorma.myapplication.data.net

import io.reactivex.Single
import retrofit2.http.GET

interface ConfigApi {
    @GET("configuration")
    fun getConfig(): Single<ConfigurationResponseDto>

}