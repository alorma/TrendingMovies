package com.alorma.myapplication.data.net

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ShowsApi {
    @GET("tv/popular")
    fun listAll(): Single<PagedResponse<TvShowDto>>

    @GET("tv/popular")
    fun listPage(@Query("page") page: Int): Single<PagedResponse<TvShowDto>>

    @GET("tv/{tv_id}/similar")
    fun similar(@Path("tv_id") id: Int): Single<PagedResponse<TvShowDto>>

    @GET("tv/{tv_id}/similar")
    fun similarPage(@Path("tv_id") id: Int, @Query("page") page: Int): Single<PagedResponse<TvShowDto>>

    @GET("tv/{tv_id}")
    fun item(@Path("tv_id") id: Int): Single<TvShowDto>
}