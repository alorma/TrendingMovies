package com.alorma.data.net

import kotlinx.coroutines.experimental.Deferred
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {
    @GET("movie/popular")
    fun listAll(): Deferred<PagedResponse<MovieDto>>

    @GET("movie/popular")
    fun listPage(@Query("page") page: Int): Deferred<PagedResponse<MovieDto>>

    @GET("search/movie")
    fun search(@Query("query") query: String): Deferred<PagedResponse<MovieDto>>

    @GET("search/movie")
    fun searchPage(@Query("query") query: String, @Query("page") page: Int): Deferred<PagedResponse<MovieDto>>

    @GET("movie/{movie_id}/similar")
    fun similar(@Path("movie_id") id: Int): Deferred<PagedResponse<MovieDto>>

    @GET("movie/{movie_id}/similar")
    fun similarPage(@Path("movie_id") id: Int, @Query("page") page: Int): Deferred<PagedResponse<MovieDto>>

    @GET("movie/{movie_id}")
    fun item(@Path("movie_id") id: Int): Deferred<MovieDto>
}